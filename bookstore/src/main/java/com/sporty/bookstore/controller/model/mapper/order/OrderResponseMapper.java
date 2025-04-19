package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.response.order.OrderResponse;
import com.sporty.bookstore.domain.entity.order.Order;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 10:01 PM
 */
@Mapper(componentModel = "spring")
public interface OrderResponseMapper {
    OrderResponse toResponse(Order order);
}
