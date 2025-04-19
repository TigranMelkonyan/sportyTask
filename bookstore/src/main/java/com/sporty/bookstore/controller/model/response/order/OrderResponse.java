package com.sporty.bookstore.controller.model.response.order;

import com.sporty.bookstore.domain.entity.order.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:59 PM
 */
public record OrderResponse(
        UUID customerId, BigDecimal totalPrice,
        Integer totalItems, Boolean loyaltyPointsApplied,
        OrderStatus orderStatus) {
}
