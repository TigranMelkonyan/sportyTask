package com.sporty.bookstore.service.order;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.order.CreateOrderModel;
import com.sporty.bookstore.repository.order.OrderRepository;
import com.sporty.bookstore.service.mapper.order.OrderMapper;
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
 * Time: 10:20 PM
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper mapper;
    private final ModelValidator validator;
    private final OrderRepository repository;

    @Transactional(readOnly = true)
    public Order getById(final UUID id) {
        log.info("Retrieving customer order with id - {} ", id);
        Assert.notNull(id, "id must not be null");
        Order result = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("Customer order with id - %s not exists", id)));
        log.info("Successfully retrieved customer order with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional
    public Order create(final CreateOrderModel model) {
        log.info("Creating customer order with model - {} ", model);
        validator.validate(model);
        Order entity = mapper.createModelToEntity(model);
        Order result = repository.save(entity);
        log.info("Successfully created customer order with model - {}, result - {}", model, result);
        return result;
    }
    
    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting customer order with id - {} ", id);
        Assert.notNull(id, "id must not be null");
        Order order = getById(id);
        order.setStatus(ModelStatus.DELETED);
        repository.save(order);
        log.info("Successfully deleted customer order with id - {}", id);
    }

    @Transactional(readOnly = true)
    public PageModel<Order> getCustomerOrderPages(final PageableModel model, final UUID customerId) {
        log.info("Searching customer orders with model - {}", model);
        validator.validate(model);
        Pageable pageable = PageRequest.of(model.getPage(), model.getSize());
        Page<Order> result = repository.findOrdersByCustomer(pageable, customerId);
        long totalCount = result.getTotalElements();
        log.info("Successfully retrieved order items for order with id result - {}", result);
        return new PageModel<>(result.getContent(), totalCount);
    }

    @Transactional(readOnly = true)
    public PageModel<Order> getPages(final PageableModel model) {
        log.info("Searching customer orders with model - {}", model);
        validator.validate(model);
        Pageable pageable = PageRequest.of(model.getPage(), model.getSize());
        Page<Order> result = repository.findOrders(pageable);
        long totalCount = result.getTotalElements();
        log.info("Successfully retrieved order items for order with id result - {}", result);
        return new PageModel<>(result.getContent(), totalCount);
    }
} 