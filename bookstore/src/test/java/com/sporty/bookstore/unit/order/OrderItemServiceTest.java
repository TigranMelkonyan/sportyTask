package com.sporty.bookstore.unit.order;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.entity.order.OrderItem;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.order.item.CreateOrderItemModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemsPageModel;
import com.sporty.bookstore.repository.order.OrderItemRepository;
import com.sporty.bookstore.service.mapper.order.OrderItemMapper;
import com.sporty.bookstore.service.order.OrderItemService;
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
 * Time: 13:25 PM
 */
@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository repository;

    @Mock
    private OrderItemMapper mapper;

    @Mock
    private ModelValidator validator;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderItemService orderItemService;

    private UUID orderItemId;
    private UUID orderId;
    private OrderItem orderItem;
    private Order order;
    private CreateOrderItemModel createModel;
    private OrderItemsPageModel pageModel;

    @BeforeEach
    void setUp() {
        orderItemId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        order = new Order();
        order.setId(orderId);
        order.setStatus(ModelStatus.ACTIVE);

        orderItem = new OrderItem();
        orderItem.setId(orderItemId);
        orderItem.setStatus(ModelStatus.ACTIVE);
        orderItem.setOrder(order);

        createModel = new CreateOrderItemModel();
        createModel.setOrderId(orderId);

        pageModel = new OrderItemsPageModel(orderId, 0, 10);
    }

    @Test
    void getById_WhenOrderItemExists_ShouldReturnOrderItem() {
        when(repository.findById(orderItemId)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.getById(orderItemId);

        assertNotNull(result);
        assertEquals(orderItemId, result.getId());
        verify(repository).findById(orderItemId);
    }

    @Test
    void getById_WhenOrderItemNotExists_ShouldThrowException() {
        when(repository.findById(orderItemId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> orderItemService.getById(orderItemId));
        verify(repository).findById(orderItemId);
    }

    @Test
    void create_WhenValidModel_ShouldCreateOrderItem() {
        when(orderService.getById(orderId)).thenReturn(order);
        when(mapper.createModelToEntity(createModel)).thenReturn(orderItem);
        when(repository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem result = orderItemService.create(createModel);

        assertNotNull(result);
        assertEquals(orderItemId, result.getId());
        assertEquals(order, result.getOrder());
        verify(validator).validate(createModel);
        verify(orderService).getById(orderId);
        verify(repository).save(orderItem);
    }

    @Test
    void delete_WhenOrderItemExists_ShouldMarkAsDeleted() {
        when(repository.findById(orderItemId)).thenReturn(Optional.of(orderItem));
        when(repository.save(any(OrderItem.class))).thenReturn(orderItem);

        orderItemService.delete(orderItemId);

        assertEquals(ModelStatus.DELETED, orderItem.getStatus());
        verify(repository).save(orderItem);
    }

    @Test
    void findAllByOrderId_ShouldReturnPageModel() {
        Pageable pageable = PageRequest.of(pageModel.page(), pageModel.size());
        Page<OrderItem> page = new PageImpl<>(List.of(orderItem));
        when(repository.findAllForOrderId(orderId, pageable)).thenReturn(page);

        PageModel<OrderItem> result = orderItemService.findAllByOrderId(pageModel);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        verify(validator).validate(pageModel);
        verify(repository).findAllForOrderId(orderId, pageable);
    }
} 