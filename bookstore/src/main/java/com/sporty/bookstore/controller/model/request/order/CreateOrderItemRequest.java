package com.sporty.bookstore.controller.model.request.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:18 PM
 */
public record CreateOrderItemRequest(
        @NotNull(message = "Book ID must not be null") UUID bookId,
        @NotNull(message = "Order ID must not be null") UUID orderId,
        @Min(value = 1, message = "Quantity must be at least 1") int quantity,
        @Min(value = 0, message = "unitPrice must be non-negative") BigDecimal unitPrice,
        @Min(value = 0, message = "totalPrice must be non-negative") BigDecimal totalPrice
) {
} 