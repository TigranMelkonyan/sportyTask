package com.sporty.bookstore.domain.model.order.item;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 8:47 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemModel {
    
    @NotNull
    public UUID orderId;

    @NotNull(message = "Book ID is required")
    public UUID bookId;

    @Min(value = 1, message = "Quantity must be at least 1")
    public Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Unit price must not be negative")
    public BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.00", message = "Total price must not be negative")
    public BigDecimal totalPrice;
        
}
