package com.sporty.bookstore.controller.model.request.order;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:18 PM
 */
public record OrderPlaceRequest(
        @NotNull(message = "List must not be empty") List<OrderItemRequest> items
) {
} 