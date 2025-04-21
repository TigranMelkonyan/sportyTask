package com.sporty.bookstore.unit.order;

import com.sporty.bookstore.controller.resource.IamServiceClient;
import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreview;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewItemModel;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlacedModel;
import com.sporty.bookstore.service.book.BookService;
import com.sporty.bookstore.service.mapper.order.OrderPlacedItemModelMapper;
import com.sporty.bookstore.service.order.OrderCartProcessService;
import com.sporty.bookstore.service.order.OrderItemService;
import com.sporty.bookstore.service.order.OrderService;
import com.sporty.bookstore.service.validator.ModelValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCartProcessServiceTest {

    @Mock
    private BookService bookService;

    @Mock
    private OrderService orderService;

    @Mock
    private ModelValidator modelValidator;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private IamServiceClient iamServiceClient;

    @Mock
    private OrderPlacedItemModelMapper orderPlacedModelMapper;

    @InjectMocks
    private OrderCartProcessService orderCartProcessService;

    private UUID customerId;
    private UUID bookId1;
    private UUID bookId2;
    private Book book1;
    private Book book2;
    private BookType bookType1;
    private BookType bookType2;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        bookId1 = UUID.randomUUID();
        bookId2 = UUID.randomUUID();

        bookType1 = new BookType();
        bookType1.setPriceMultiplier(0.8);
        bookType1.setBundleDiscount(0.9);

        bookType2 = new BookType();
        bookType2.setPriceMultiplier(1.0);
        bookType2.setBundleDiscount(1.0);

        book1 = new Book();
        book1.setId(bookId1);
        book1.setTitle("Book 1");
        book1.setBasePrice(BigDecimal.valueOf(10));
        book1.setStockQuantity(10);
        book1.setType(bookType1);

        book2 = new Book();
        book2.setId(bookId2);
        book2.setTitle("Book 2");
        book2.setBasePrice(BigDecimal.valueOf(20));
        book2.setStockQuantity(5);
        book2.setType(bookType2);
    }

    @Test
    void calculateCartPreview_WithLoyaltyPoints_ShouldApplyDiscount() {
        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId1, 2),
                new OrderCartPreviewModel(bookId2, 1)
        );

        when(iamServiceClient.getUserLoyaltyPoints(customerId))
                .thenReturn(ResponseEntity.ok(10));
        when(bookService.getById(bookId1)).thenReturn(book1);
        when(bookService.getById(bookId2)).thenReturn(book2);

        OrderCartPreview result = orderCartProcessService.calculateCartPreview(items, customerId);

        assertNotNull(result);
        assertEquals(10, result.loyaltyPoints());
        assertEquals(2, result.items().size());

        OrderCartPreviewItemModel firstItem = result.items().get(0);
        assertEquals(bookId1, firstItem.bookId());
        assertEquals(firstItem.oneForFree(), true);
        assertNotEquals(firstItem.totalPrice(), firstItem.totalPriceAfterDiscount());
        assertTrue(firstItem.oneForFree());

        OrderCartPreviewItemModel secondItem = result.items().get(1);
        assertEquals(bookId2, secondItem.bookId());
        assertEquals(BigDecimal.ZERO, secondItem.discount());
        assertFalse(secondItem.oneForFree());
    }

    @Test
    void calculateCartPreview_WithBundleDiscount_ShouldApplyCorrectDiscount() {
        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId1, 3)
        );

        when(iamServiceClient.getUserLoyaltyPoints(customerId))
                .thenReturn(ResponseEntity.ok(5));
        when(bookService.getById(bookId1)).thenReturn(book1);

        OrderCartPreview result = orderCartProcessService.calculateCartPreview(items, customerId);

        assertNotNull(result);
        assertEquals(1, result.items().size());

        OrderCartPreviewItemModel item = result.items().get(0);

        BigDecimal expectedPrice = BigDecimal.valueOf(10.0)
                .multiply(BigDecimal.valueOf(0.8))
                .multiply(BigDecimal.valueOf(3))
                .multiply(BigDecimal.valueOf(0.9))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(expectedPrice, item.totalPriceAfterDiscount());
    }

    @Test
    void placeOrder_WithInsufficientStock_ShouldThrowException() {
        book1.setStockQuantity(2);
        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId1, 3)
        );

        when(iamServiceClient.getUserLoyaltyPoints(customerId))
                .thenReturn(ResponseEntity.ok(5));
        when(bookService.getById(bookId1)).thenReturn(book1);

        assertThrows(RecordConflictException.class, () ->
                orderCartProcessService.calculateCartPreview(items, customerId));
    }

    @Test
    void placeOrder_ShouldUpdateLoyaltyPointsAndStock() {
        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId1, 2),
                new OrderCartPreviewModel(bookId2, 1)
        );

        when(iamServiceClient.getUserLoyaltyPoints(customerId))
                .thenReturn(ResponseEntity.ok(15));
        when(bookService.getById(bookId1)).thenReturn(book1);
        when(bookService.getById(bookId2)).thenReturn(book2);
        when(orderService.create(any())).thenReturn(new Order());
        when(orderPlacedModelMapper.toModelOrderPlacedModel(any())).thenReturn(
                new OrderPlaceItemModel(
                        bookId1,
                        "Book 1",
                        2,
                        BigDecimal.valueOf(50.00),
                        BigDecimal.ZERO,
                        BigDecimal.valueOf(100.00),
                        BigDecimal.valueOf(100.00),
                        false
                )
        );

        OrderPlacedModel result = orderCartProcessService.placeOrder(
                new OrderPlaceModel(items.stream()
                        .map(i -> new OrderItemModel(
                                i.bookId(),
                                i.quantity()
                        ))
                        .toList()),
                customerId
        );

        assertNotNull(result);
        verify(iamServiceClient).updateUserLoyaltyPoints(customerId, 2);
        verify(bookService, times(2)).save(any());
        verify(orderService).create(any());
        verify(orderItemService, times(2)).create(any());
    }

    @Test
    void calculateCartPreview_WithMultipleEligibleItems_ShouldApplyDiscountToFirstEligible() {
        Book book3 = new Book();
        book3.setId(UUID.randomUUID());
        book3.setTitle("Book 3");
        book3.setBasePrice(BigDecimal.valueOf(40.00));
        book3.setStockQuantity(8);
        book3.setType(bookType1);

        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId1, 1),
                new OrderCartPreviewModel(book3.getId(), 1),
                new OrderCartPreviewModel(bookId2, 1)
        );

        when(iamServiceClient.getUserLoyaltyPoints(customerId))
                .thenReturn(ResponseEntity.ok(15));
        when(bookService.getById(bookId1)).thenReturn(book1);
        when(bookService.getById(book3.getId())).thenReturn(book3);
        when(bookService.getById(bookId2)).thenReturn(book2);

        OrderCartPreview result = orderCartProcessService.calculateCartPreview(items, customerId);

        assertNotNull(result);
        assertEquals(3, result.items().size());

        OrderCartPreviewItemModel firstItem = result.items().get(0);
        assertEquals(bookId1, firstItem.bookId());
        assertTrue(firstItem.oneForFree());

        OrderCartPreviewItemModel secondItem = result.items().get(1);
        assertEquals(book3.getId(), secondItem.bookId());
        assertFalse(secondItem.oneForFree());
    }
} 