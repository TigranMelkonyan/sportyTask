package com.sporty.bookstore.domain.model.order.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 10:28 PM
 */
public record OrderItemModel(
        @NotNull(message = "required") UUID bookId,
        @Min(value = 1, message = "At least one item is required") int quantity) {
}
