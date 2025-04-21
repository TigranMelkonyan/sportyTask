package com.sporty.bookstore.service.order;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.entity.order.OrderItem;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.order.item.CreateOrderItemModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemsPageModel;
import com.sporty.bookstore.repository.order.OrderItemRepository;
import com.sporty.bookstore.service.mapper.order.OrderItemMapper;
import com.sporty.bookstore.service.validator.ModelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:23 PM
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper mapper;
    private final ModelValidator validator;
    private final OrderService orderService;
    private final OrderItemRepository repository;

    @Transactional(readOnly = true)
    public OrderItem getById(final UUID id) {
        log.info("Retrieving order item with id - {} ", id);
        Assert.notNull(id, "id must not be null");
        OrderItem result = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("Order item with id - %s not exists", id)));
        log.info("Successfully retrieved order item with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional
    public OrderItem create(final CreateOrderItemModel model) {
        log.info("Creating order item with model - {} ", model);
        validator.validate(model);
        Order order = orderService.getById(model.getOrderId());
        OrderItem entity = mapper.createModelToEntity(model);
        entity.setOrder(order);
        OrderItem result = repository.save(entity);
        log.info("Successfully created order item with model - {}, result - {}", model, result);
        return result;
    }

    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting order item with id - {} ", id);
        Assert.notNull(id, "id must not be null");
        OrderItem item = getById(id);
        item.setStatus(ModelStatus.DELETED);
        repository.save(item);
        log.info("Successfully deleted order item with id - {}", id);
    }

    @Transactional(readOnly = true)
    public PageModel<OrderItem> findAllByOrderId(final OrderItemsPageModel model) {
        UUID orderId = model.orderId();
        log.info("Getting order items for order with id - {}", orderId);
        validator.validate(model);
        Pageable pageable = PageRequest.of(model.page(), model.size());
        Page<OrderItem> result = repository.findAllForOrderId(orderId, pageable);
        long totalCount = result.getTotalElements();
        log.info("Successfully retrieved order items for order with id result - {}", result);
        return new PageModel<>(result.getContent(), totalCount);
    }
} 