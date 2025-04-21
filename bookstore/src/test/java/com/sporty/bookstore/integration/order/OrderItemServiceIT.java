package com.sporty.bookstore.integration.order;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.entity.order.OrderItem;
import com.sporty.bookstore.domain.entity.order.OrderStatus;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.order.CreateOrderModel;
import com.sporty.bookstore.domain.model.order.item.CreateOrderItemModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemsPageModel;
import com.sporty.bookstore.repository.book.BookRepository;
import com.sporty.bookstore.repository.book.BookTypeRepository;
import com.sporty.bookstore.repository.order.OrderItemRepository;
import com.sporty.bookstore.service.order.OrderItemService;
import com.sporty.bookstore.service.order.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 13:55 PM
 */
@SpringBootTest
@Transactional
public class OrderItemServiceIT {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookTypeRepository bookTypeRepository;

    private UUID orderId;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        BookType bookType = new BookType();
        bookType.setName("Test Type");
        bookType.setPriceMultiplier(1.0);
        bookType.setBundleDiscount(0.0);
        bookType = bookTypeRepository.save(bookType);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setBasePrice(BigDecimal.valueOf(25.00));
        book.setStockQuantity(10);
        book.setType(bookType);
        book = bookRepository.save(book);
        bookId = book.getId();

        CreateOrderModel orderModel = new CreateOrderModel();
        orderModel.setCustomerId(UUID.randomUUID());
        orderModel.setTotalPrice(BigDecimal.valueOf(100.00));
        orderModel.setTotalItems(1);
        orderModel.setLoyaltyPointsApplied(false);

        Order order = orderService.create(orderModel);
        orderId = order.getId();
    }

    @Test
    void create_WhenValidModel_ShouldCreateOrderItem() {
        CreateOrderItemModel model = new CreateOrderItemModel();
        model.setOrderId(orderId);
        model.setBookId(bookId);
        model.setQuantity(2);
        model.setUnitPrice(BigDecimal.valueOf(25.00));
        model.setTotalPrice(BigDecimal.valueOf(50.00));
        model.setOrderStatus(OrderStatus.PENDING);

        OrderItem result = orderItemService.create(model);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(orderId, result.getOrder().getId());
        Assertions.assertEquals(bookId, result.getBookId());
        Assertions.assertEquals(2, result.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(25.00), result.getUnitPrice());
        Assertions.assertEquals(BigDecimal.valueOf(50.00), result.getTotalPrice());
        Assertions.assertEquals(ModelStatus.ACTIVE, result.getStatus());

        OrderItem saved = orderItemRepository.findById(result.getId()).orElseThrow();
        Assertions.assertEquals(result, saved);
    }

    @Test
    void getById_WhenOrderItemExists_ShouldReturnOrderItem() {
        CreateOrderItemModel model = new CreateOrderItemModel();
        model.setOrderId(orderId);
        model.setBookId(bookId);
        model.setQuantity(2);
        model.setUnitPrice(BigDecimal.valueOf(25.00));
        model.setTotalPrice(BigDecimal.valueOf(50.00));
        model.setOrderStatus(OrderStatus.PENDING);

        OrderItem created = orderItemService.create(model);

        OrderItem result = orderItemService.getById(created.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals(orderId, result.getOrder().getId());
        Assertions.assertEquals(bookId, result.getBookId());
        Assertions.assertEquals(2, result.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(25.00), result.getUnitPrice());
        Assertions.assertEquals(BigDecimal.valueOf(50.00), result.getTotalPrice());
    }

    @Test
    void getById_WhenOrderItemNotExists_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        Assertions.assertThrows(RecordNotFoundException.class, () -> orderItemService.getById(nonExistentId));
    }

    @Test
    void delete_WhenOrderItemExists_ShouldMarkAsDeleted() {
        CreateOrderItemModel model = new CreateOrderItemModel();
        model.setOrderId(orderId);
        model.setBookId(bookId);
        model.setQuantity(2);
        model.setUnitPrice(BigDecimal.valueOf(25.00));
        model.setTotalPrice(BigDecimal.valueOf(50.00));
        model.setOrderStatus(OrderStatus.PENDING);

        OrderItem created = orderItemService.create(model);

        orderItemService.delete(created.getId());

        OrderItem deleted = orderItemRepository.findById(created.getId()).orElseThrow();
        Assertions.assertEquals(ModelStatus.DELETED, deleted.getStatus());
    }

    @Test
    void findAllByOrderId_ShouldReturnPageModel() {
        CreateOrderItemModel model = new CreateOrderItemModel();
        model.setOrderId(orderId);
        model.setBookId(bookId);
        model.setQuantity(2);
        model.setUnitPrice(BigDecimal.valueOf(25.00));
        model.setTotalPrice(BigDecimal.valueOf(50.00));
        model.setOrderStatus(OrderStatus.PENDING);
        
        orderItemService.create(model);
        
        OrderItemsPageModel pageModel = new OrderItemsPageModel(orderId, 0, 10);
        PageModel<OrderItem> result = orderItemService.findAllByOrderId(pageModel);

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(result.totalCount(), 0);
    }
} 