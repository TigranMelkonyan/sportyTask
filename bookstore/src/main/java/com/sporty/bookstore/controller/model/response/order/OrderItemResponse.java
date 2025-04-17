package com.sporty.bookstore.controller.model.response.order;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:21 PM
 */
public record OrderItemResponse(
        UUID id,
        UUID bookId,
        UUID orderId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
} 