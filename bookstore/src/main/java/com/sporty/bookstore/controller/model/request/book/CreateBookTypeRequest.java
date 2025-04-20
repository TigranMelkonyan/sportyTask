package com.sporty.bookstore.controller.model.request.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:30 AM
 */
public record CreateBookTypeRequest(
        @NotBlank(message = "required")
        String name,
        @DecimalMin("0.0") @DecimalMax("100.0")
        @Schema(description = "Multiplier as percentage (e.g., 80 = 80% of base price)")
        double pricePercentage,
        @DecimalMin("0.0") @DecimalMax("100.0")
        @Schema(description = "Bundle discount as percentage (e.g., 95 = 5% discount)")
        double bundleDiscountPercentage,
        @NotNull(message = "required")
        boolean eligibleForDiscount
) {
}
