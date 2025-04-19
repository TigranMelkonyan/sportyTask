package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.request.order.OrderItemRequest;
import com.sporty.bookstore.domain.model.order.item.OrderItemModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 2:46 PM
 */
@Mapper(componentModel = "spring")
public interface OrderItemRequestMapper {
    OrderItemModel toModelOrderItemModel(OrderItemRequest item);
}

