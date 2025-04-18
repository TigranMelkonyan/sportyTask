package com.sporty.bookstore.domain.model.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 12:48 AM
 */
public record CreateBookTypeModel(
        @NotBlank(message = "required") String name,
        @Min(value = 0, message = "priceMultiplier must not be negative") double priceMultiplier,
        @Min(value = 0, message = "priceMultiplier must not be negative") double bundleDiscount) {
}
