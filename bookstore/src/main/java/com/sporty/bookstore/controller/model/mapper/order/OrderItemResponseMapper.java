package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.response.order.OrderItemResponse;
import com.sporty.bookstore.domain.entity.order.OrderItem;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:02 PM
 */
@Mapper(componentModel = "spring")
public interface OrderItemResponseMapper {
    
    OrderItemResponse toResponse(OrderItem model);
}
