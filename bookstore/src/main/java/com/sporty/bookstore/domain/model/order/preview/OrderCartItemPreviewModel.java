package com.sporty.bookstore.domain.model.order.preview;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 4:21 PM
 */
public record OrderCartItemPreviewModel(
        @NotNull(message = "required") UUID bookId,
        @Min(value = 1, message = "Stock quantity must be greater than 0") int quantity) {
}
