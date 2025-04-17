package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.request.order.CreateOrderItemRequest;
import com.sporty.bookstore.domain.model.order.CreateOrderItemModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:00 PM
 */
@Mapper(componentModel = "spring")
public interface OrderItemRequestMapper {
    
    CreateOrderItemModel toCreateOrderItemModel(CreateOrderItemRequest request);
}
