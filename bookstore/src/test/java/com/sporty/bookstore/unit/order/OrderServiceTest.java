package com.sporty.bookstore.unit.order;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.order.CreateOrderModel;
import com.sporty.bookstore.repository.order.OrderRepository;
import com.sporty.bookstore.service.mapper.order.OrderMapper;
import com.sporty.bookstore.service.order.OrderService;
import com.sporty.bookstore.service.validator.ModelValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 13:05 PM
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private ModelValidator validator;

    @InjectMocks
    private OrderService orderService;

    private UUID orderId;
    private UUID customerId;
    private Order order;
    private CreateOrderModel createModel;
    private PageableModel pageableModel;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        customerId = UUID.randomUUID();

        order = new Order();
        order.setId(orderId);
        order.setStatus(ModelStatus.ACTIVE);

        createModel = new CreateOrderModel();
        createModel.setCustomerId(customerId);

        pageableModel = new PageableModel();
        pageableModel.setPage(0);
        pageableModel.setSize(10);
    }

    @Test
    void getById_WhenOrderExists_ShouldReturnOrder() {
        when(repository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(repository).findById(orderId);
    }

    @Test
    void getById_WhenOrderNotExists_ShouldThrowException() {
        when(repository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> orderService.getById(orderId));
        verify(repository).findById(orderId);
    }

    @Test
    void create_WhenValidModel_ShouldCreateOrder() {
        when(mapper.createModelToEntity(createModel)).thenReturn(order);
        when(repository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.create(createModel);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(validator).validate(createModel);
        verify(repository).save(order);
    }

    @Test
    void delete_WhenOrderExists_ShouldMarkAsDeleted() {
        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenReturn(order);

        orderService.delete(orderId);

        assertEquals(ModelStatus.DELETED, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void getCustomerOrderPages_ShouldReturnPageModel() {
        Pageable pageable = PageRequest.of(pageableModel.getPage(), pageableModel.getSize());
        Page<Order> page = new PageImpl<>(List.of(order));
        when(repository.findOrdersByCustomer(pageable, customerId)).thenReturn(page);

        PageModel<Order> result = orderService.getCustomerOrderPages(pageableModel, customerId);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        verify(validator).validate(pageableModel);
        verify(repository).findOrdersByCustomer(pageable, customerId);
    }

    @Test
    void getPages_ShouldReturnPageModel() {
        Pageable pageable = PageRequest.of(pageableModel.getPage(), pageableModel.getSize());
        Page<Order> page = new PageImpl<>(List.of(order));
        when(repository.findOrders(pageable)).thenReturn(page);

        PageModel<Order> result = orderService.getPages(pageableModel);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        verify(validator).validate(pageableModel);
        verify(repository).findOrders(pageable);
    }
} 