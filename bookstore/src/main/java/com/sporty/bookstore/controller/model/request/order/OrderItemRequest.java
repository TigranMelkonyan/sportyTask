package com.sporty.bookstore.controller.model.request.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 2:44 PM
 */
public record OrderItemRequest(
        @NotNull(message = "required") UUID bookId,
        @Min(value = 1, message = "At least one item is required") int quantity) {
}
