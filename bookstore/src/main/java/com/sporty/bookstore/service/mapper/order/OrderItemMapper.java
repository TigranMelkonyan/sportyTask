package com.sporty.bookstore.service.mapper.order;

import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.entity.order.OrderItem;
import com.sporty.bookstore.domain.model.order.item.CreateOrderItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 9:04 PM
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "order", source = "orderId") // explicitly tell MapStruct what to do
    OrderItem createModelToEntity(CreateOrderItemModel model);

    default Order map(UUID orderId) {
        if (orderId == null) return null;
        Order order = new Order();
        order.setId(orderId);
        return order;
    }
}

