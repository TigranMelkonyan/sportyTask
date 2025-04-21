package com.sporty.bookstore.integration.order;

import com.sporty.bookstore.controller.resource.IamServiceClient;
import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreview;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlacedModel;
import com.sporty.bookstore.repository.book.BookRepository;
import com.sporty.bookstore.repository.book.BookTypeRepository;
import com.sporty.bookstore.service.order.OrderCartProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 14:49 PM
 */
@SpringBootTest
@Transactional
public class OrderCartProcessServiceIT {

    @MockitoBean
    private IamServiceClient iamServiceClient;

    @Autowired
    private OrderCartProcessService orderCartProcessService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookTypeRepository bookTypeRepository;


    private UUID customerId;
    private UUID bookId1;
    private UUID bookId2;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();

        // Create book types
        BookType regularType = new BookType();
        regularType.setName("RegularType");
        regularType.setPriceMultiplier(1.0);
        regularType.setBundleDiscount(0.9);
        regularType = bookTypeRepository.save(regularType);

        BookType newRelease = new BookType();
        newRelease.setName("NEW_REL");
        newRelease.setPriceMultiplier(1.0);
        newRelease.setBundleDiscount(1.0);
        newRelease = bookTypeRepository.save(newRelease);

        Book book1 = new Book();
        book1.setTitle("Regular Book");
        book1.setAuthor("Author 1");
        book1.setBasePrice(BigDecimal.valueOf(10.0));
        book1.setStockQuantity(10);
        book1.setType(regularType);
        book1 = bookRepository.save(book1);
        bookId1 = book1.getId();

        Book book2 = new Book();
        book2.setTitle("New Book");
        book2.setAuthor("Author 2");
        book2.setBasePrice(BigDecimal.valueOf(10.00));
        book2.setStockQuantity(10);
        book2.setType(newRelease);
        book2 = bookRepository.save(book2);
        bookId2 = book2.getId();

        when(iamServiceClient.getUserLoyaltyPoints(any())).thenReturn(ResponseEntity.ok(10));
        when(iamServiceClient.updateUserLoyaltyPoints(any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Test
    void calculateCartPreview_WithNewReleaseEligibleBook_ShouldApply_Only_Loyalty() {
        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId2, 5)
        );

        OrderCartPreview preview = orderCartProcessService.calculateCartPreview(items, customerId);

        assertNotNull(preview);
        assertEquals(1, preview.items().size());
        assertEquals(0, BigDecimal.valueOf(40.0).compareTo(preview.totalPrice()));
        assertTrue(preview.items().get(0).oneForFree());
    }

    @Test
    void calculateCartPreview_WithRegularBook_ShouldApplyDiscount() {
        List<OrderCartPreviewModel> items = List.of(
                new OrderCartPreviewModel(bookId1, 3)
        );

        OrderCartPreview preview = orderCartProcessService.calculateCartPreview(items, customerId);

        assertNotNull(preview);
        assertEquals(1, preview.items().size());
        assertEquals(0, BigDecimal.valueOf(18.0).compareTo(preview.totalPrice()));
        assertEquals(0, BigDecimal.valueOf(12).compareTo(preview.totalDiscount()));
        assertTrue(preview.items().get(0).oneForFree());
    }

    @Test
    void placeOrder_WithMultipleBooks_ShouldCreateOrderAndUpdateStock() {
        List<OrderItemModel> items = List.of(
                new OrderItemModel(bookId1, 2),
                new OrderItemModel(bookId2, 1)
        );
        OrderPlaceModel placeModel = new OrderPlaceModel(items);

        OrderPlacedModel result = orderCartProcessService.placeOrder(placeModel, customerId);

        assertNotNull(result);
        assertEquals(2, result.items().size());

        Book book1 = bookRepository.findById(bookId1).orElseThrow();
        Book book2 = bookRepository.findById(bookId2).orElseThrow();
        assertEquals(8, book1.getStockQuantity());
        assertEquals(9, book2.getStockQuantity());
    }

    @Test
    void placeOrder_WithInsufficientStock_ShouldThrowException() {
        List<OrderItemModel> items = List.of(
                new OrderItemModel(bookId1, 15)
        );
        OrderPlaceModel placeModel = new OrderPlaceModel(items);

        assertThrows(Exception.class, () ->
                orderCartProcessService.placeOrder(placeModel, customerId)
        );
    }
} 