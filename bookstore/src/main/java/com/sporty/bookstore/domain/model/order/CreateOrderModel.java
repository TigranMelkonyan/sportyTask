package com.sporty.bookstore.domain.model.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:22 PM
 */
@Data
public class CreateOrderModel {

    @NotNull(message = "Customer ID is required")
    public UUID customerId;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.00", message = "Total price must not be negative")
    public BigDecimal totalPrice;

    @Min(value = 1, message = "At least one item is required")
    public Integer totalItems;

    @NotNull(message = "Loyalty points flag is required")
    public Boolean loyaltyPointsApplied;
    
}
