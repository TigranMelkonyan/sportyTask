package com.sporty.bookstore.integration.order;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.order.CreateOrderModel;
import com.sporty.bookstore.repository.order.OrderRepository;
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
 * Time: 13:44 PM
 */
@SpringBootTest
@Transactional
public class OrderServiceIT {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
    }

    @Test
    void create_WhenValidModel_ShouldCreateOrder() {
        CreateOrderModel model = new CreateOrderModel();
        model.setCustomerId(customerId);
        model.setTotalPrice(BigDecimal.valueOf(100.00));
        model.setTotalItems(2);
        model.setLoyaltyPointsApplied(false);

        Order result = orderService.create(model);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(customerId, result.getCustomerId());
        Assertions.assertEquals(BigDecimal.valueOf(100.00), result.getTotalPrice());
        Assertions.assertEquals(2, result.getTotalItems());
        Assertions.assertFalse(result.getLoyaltyPointsApplied());
        Assertions.assertEquals(ModelStatus.ACTIVE, result.getStatus());

        Order saved = orderRepository.findById(result.getId()).orElseThrow();
        Assertions.assertEquals(result, saved);
        Assertions.assertEquals(ModelStatus.ACTIVE, saved.getStatus());
    }

    @Test
    void getById_WhenOrderExists_ShouldReturnOrder() {
        CreateOrderModel model = new CreateOrderModel();
        model.setCustomerId(customerId);
        model.setTotalPrice(BigDecimal.valueOf(100.00));
        model.setTotalItems(2);
        model.setLoyaltyPointsApplied(false);

        Order created = orderService.create(model);

        Order result = orderService.getById(created.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals(customerId, result.getCustomerId());
        Assertions.assertEquals(BigDecimal.valueOf(100.00), result.getTotalPrice());
        Assertions.assertEquals(2, result.getTotalItems());
        Assertions.assertFalse(result.getLoyaltyPointsApplied());
    }

    @Test
    void getById_WhenOrderNotExists_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        Assertions.assertThrows(RecordNotFoundException.class, () -> orderService.getById(nonExistentId));
    }

    @Test
    void delete_WhenOrderExists_ShouldMarkAsDeleted() {
        CreateOrderModel model = new CreateOrderModel();
        model.setCustomerId(customerId);
        model.setTotalPrice(BigDecimal.valueOf(100.00));
        model.setTotalItems(2);
        model.setLoyaltyPointsApplied(false);

        Order created = orderService.create(model);

        orderService.delete(created.getId());

        Order deleted = orderRepository.findById(created.getId()).orElseThrow();
        Assertions.assertEquals(ModelStatus.DELETED, deleted.getStatus());
    }

    @Test
    void getCustomerOrderPages_ShouldReturnPageModel() {
        CreateOrderModel model = new CreateOrderModel();
        model.setCustomerId(customerId);
        model.setTotalPrice(BigDecimal.valueOf(100.00));
        model.setTotalItems(2);
        model.setLoyaltyPointsApplied(false);

        orderService.create(model);

        PageableModel pageable = new PageableModel();
        pageable.setPage(0);
        pageable.setSize(10);

        PageModel<Order> result = orderService.getCustomerOrderPages(pageable, customerId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.items().size());
        Assertions.assertEquals(1L, result.totalCount());
    }

    @Test
    void getPages_ShouldReturnPageModel() {
        CreateOrderModel model = new CreateOrderModel();
        model.setCustomerId(customerId);
        model.setTotalPrice(BigDecimal.valueOf(100.00));
        model.setTotalItems(2);
        model.setLoyaltyPointsApplied(false);

        orderService.create(model);

        PageableModel pageable = new PageableModel();
        pageable.setPage(0);
        pageable.setSize(10);

        PageModel<Order> result = orderService.getPages(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(result.totalCount(), 0);
    }
}
