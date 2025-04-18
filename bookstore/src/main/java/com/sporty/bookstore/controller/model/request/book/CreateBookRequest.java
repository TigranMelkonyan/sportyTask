package com.sporty.bookstore.controller.model.request.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:25 PM
 */
@Data
public class CreateBookRequest {

    @NotBlank(message = "required")
    private String title;

    @NotBlank(message = "required")
    private String author;

    @NotNull
    @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0")
    private BigDecimal basePrice;

    @NotNull(message = "required")
    private UUID bookTypeId;

    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stockQuantity;
}
