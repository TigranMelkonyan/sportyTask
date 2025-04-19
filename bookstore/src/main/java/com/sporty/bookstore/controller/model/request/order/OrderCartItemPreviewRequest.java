package com.sporty.bookstore.controller.model.request.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:04 PM
 */
public record OrderCartItemPreviewRequest(
        @NotNull(message = "required") UUID bookId,
        @Min(value = 1, message = "At least one item is required") int quantity) {
}