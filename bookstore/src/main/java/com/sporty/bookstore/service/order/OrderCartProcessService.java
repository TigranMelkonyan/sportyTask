package com.sporty.bookstore.service.order;

import com.sporty.bookstore.controller.resource.IamServiceClient;
import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.entity.order.OrderStatus;
import com.sporty.bookstore.domain.model.common.exception.ErrorCode;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.order.CreateOrderModel;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreview;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewItemModel;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewModel;
import com.sporty.bookstore.domain.model.order.item.CreateOrderItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlacedModel;
import com.sporty.bookstore.service.book.BookService;
import com.sporty.bookstore.service.mapper.order.OrderPlacedItemModelMapper;
import com.sporty.bookstore.service.validator.ModelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 4:18 PM
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class OrderCartProcessService {

    private final static int acceptableLoyaltyPoints = 10;

    private final BookService bookService;
    private final ModelValidator validator;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final IamServiceClient iamServiceClient;
    private final OrderPlacedItemModelMapper orderPlacedModelMapper;

    @Transactional
    public OrderCartPreview calculateCartPreview(final List<OrderCartPreviewModel> items, final UUID customerId) {
        log.info("Calculating order preview for customer with id {}", customerId);
        validator.validate(items);
        Assert.notNull(customerId, "customerId cannot be null");
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<OrderCartPreviewItemModel> previewItems = new ArrayList<>();
        int currentLoyaltyPoints = Optional.ofNullable(iamServiceClient.getUserLoyaltyPoints(customerId).getBody()).orElse(0);
        boolean loyaltyApplied = false;
        boolean forFree = false;
        OrderCartPreviewModel loyaltyItem = null;
        if (currentLoyaltyPoints >= acceptableLoyaltyPoints) {
            for (OrderCartPreviewModel item : items) {
                Book book = bookService.getById(item.bookId());
                if (isEligibleForDiscount(book)) {
                    loyaltyItem = item;
                    break;
                }
            }
        }
        for (OrderCartPreviewModel item : items) {
            Book book = bookService.getById(item.bookId());
            int bookQuantity = item.quantity();
            checkAvailableQuantity(book, bookQuantity);
            BigDecimal itemsPriceAfterDiscount;
            if (Objects.nonNull(loyaltyItem) && !loyaltyApplied && loyaltyItem.bookId().equals(item.bookId())) {
                loyaltyApplied = true;
                forFree = true;
            }
            itemsPriceAfterDiscount = calculateBooksPriceWithDiscount(book, bookQuantity, loyaltyApplied);
            BigDecimal prices = book.getBasePrice().multiply(BigDecimal.valueOf(item.quantity()));
            BigDecimal discount = prices.subtract(itemsPriceAfterDiscount);
            totalDiscount = itemsPriceAfterDiscount.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : totalDiscount.add(discount);
            totalPrice = totalPrice.add(itemsPriceAfterDiscount);
            addPreviewItems(previewItems, book, item.quantity(), discount, prices, itemsPriceAfterDiscount, forFree);
            if (forFree) {
                forFree = false;
            }

        }
        log.info("Successfully calculated order preview for customer with id {}", customerId);
        //cart preview data can be saved in db for further perches checkout
        return new OrderCartPreview(totalPrice, totalDiscount, currentLoyaltyPoints, previewItems);
    }

    @Transactional
    public OrderPlacedModel placeOrder(final OrderPlaceModel placeModel, final UUID customerId) {
        log.info("Place order for customer with id {}", customerId);
        Assert.notNull(customerId, "customerId cannot be null");
        validator.validate(placeModel);
        int currentLoyaltyPoints = Optional.ofNullable(iamServiceClient.getUserLoyaltyPoints(customerId).getBody()).orElse(0);
        boolean loyaltyApplied;
        OrderCartPreview preview = calculateCartPreview(placeModel
                .items()
                .stream()
                .map(i -> new OrderCartPreviewModel(i.bookId(), i.quantity()))
                .toList(), customerId
        );
        //some other pre-checks can be applied here for user balance etc...
        int totalCount = calculateTotalItemCount(preview.items());
        loyaltyApplied = currentLoyaltyPoints >= acceptableLoyaltyPoints;
        int updatedPoints = updateCustomerLoyaltyPointsByPurchasedBooks(currentLoyaltyPoints, loyaltyApplied, totalCount);
        List<OrderPlaceItemModel> mappedItems = mapToOrderPlaceItems(preview.items());
        OrderPlacedModel model = new OrderPlacedModel(
                preview.totalPrice(), preview.totalDiscount(),
                updatedPoints, mappedItems);
        updateBookStockQuantity(preview.items());
        iamServiceClient.updateUserLoyaltyPoints(customerId, updatedPoints);
        createOrder(totalCount, preview.totalPrice(), customerId, loyaltyApplied, preview.items());
        log.info("Successfully placed order for customer with id {}", customerId);
        return model;
    }

    @Transactional
    protected void updateBookStockQuantity(final List<OrderCartPreviewItemModel> items) {
        for (OrderCartPreviewItemModel item : items) {
            Book book = bookService.getById(item.bookId());
            int stockQuantity = book.getStockQuantity() - item.quantity();
            checkAvailableQuantity(book, stockQuantity);
            book.setStockQuantity(stockQuantity);
            bookService.save(book);
        }
    }

    @Transactional
    public void checkAvailableQuantity(final Book book, final int quantity) {
        if (book.getStockQuantity() < quantity) {
            throw new RecordConflictException(String.format("Not enough stock for book with id %s ", book.getId()), ErrorCode.RECORD_CONFLICT);
        }
    }

    @Transactional
    protected void createOrder(
            int totalCount, final BigDecimal totalPrice,
            final UUID customerId, final boolean loyaltyApplied,
            final List<OrderCartPreviewItemModel> items) {
        CreateOrderModel createOrderModel = new CreateOrderModel();
        createOrderModel.setTotalItems(totalCount);
        createOrderModel.setTotalPrice(totalPrice);
        createOrderModel.setCustomerId(customerId);
        createOrderModel.setLoyaltyPointsApplied(loyaltyApplied);
        Order order = orderService.create(createOrderModel);
        for (OrderCartPreviewItemModel item : items) {
            CreateOrderItemModel itemModel = new CreateOrderItemModel(
                    order.getId(),
                    item.bookId(),
                    item.quantity(),
                    item.unitPrice(),
                    item.totalPrice(),
                    OrderStatus.COMPLETED);
            orderItemService.create(itemModel);
        }
    }

    @Transactional
    protected int updateCustomerLoyaltyPointsByPurchasedBooks(
            final int currentPoints,
            final boolean loyaltyApplied,
            int purchasedCount) {
        int earnedPoints = loyaltyApplied ? purchasedCount - 1 : purchasedCount;
        int pointsAfterApplied = loyaltyApplied ? 0 : currentPoints;
        int updatedPoints = pointsAfterApplied + earnedPoints;
        updatedPoints = Math.max(updatedPoints, 0);
        return updatedPoints;
    }


    private int calculateTotalItemCount(List<OrderCartPreviewItemModel> orderItems) {
        int totalQuantity = 0;
        for (OrderCartPreviewItemModel item : orderItems) {
            totalQuantity += item.quantity();
        }
        return totalQuantity;
    }

    private List<OrderPlaceItemModel> mapToOrderPlaceItems(final List<OrderCartPreviewItemModel> items) {
        List<OrderPlaceItemModel> placeItems = new ArrayList<>();
        for (OrderCartPreviewItemModel item : items) {
            placeItems.add(orderPlacedModelMapper.toModelOrderPlacedModel(item));
        }
        return placeItems;
    }

    private void addPreviewItems(
            final List<OrderCartPreviewItemModel> previewItems, final Book book,
            final int quantity, final BigDecimal discount, final BigDecimal prices,
            final BigDecimal itemsPriceAfterDiscount, boolean forFree) {
        previewItems.add(new OrderCartPreviewItemModel(
                book.getId(),
                book.getTitle(),
                quantity,
                book.getBasePrice(),
                discount,
                prices,
                itemsPriceAfterDiscount,
                forFree));
    }

    private boolean isEligibleForDiscount(final Book book) {
        BookType type = book.getType();
        return type.isEligibleForDiscount();
    }

    private BigDecimal calculateBooksPriceWithDiscount(final Book book, int quantity, final boolean loyaltyApplied) {
        if (quantity < 3) {
            return calculateBooksPriceWithQuantity(book, quantity, loyaltyApplied);
        }
        quantity = loyaltyApplied ? quantity - 1 : quantity;
        BigDecimal basePrice = book.getBasePrice()
                .multiply(BigDecimal.valueOf(book.getType().getPriceMultiplier()))
                .multiply(BigDecimal.valueOf(quantity));
        return basePrice.multiply(BigDecimal.valueOf(book.getType().getBundleDiscount()));
    }

    private BigDecimal calculateBooksPriceWithQuantity(final Book book, int quantity, final boolean loyaltyApplied) {
        quantity = loyaltyApplied ? quantity - 1 : quantity;
        return book.getBasePrice().multiply(BigDecimal.valueOf(quantity));
    }

}
