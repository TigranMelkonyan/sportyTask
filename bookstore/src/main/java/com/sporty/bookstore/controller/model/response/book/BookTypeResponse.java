package com.sporty.bookstore.controller.model.response.book;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:18 AM
 */
public record BookTypeResponse(
        UUID id,
        String name,
        double pricePercentage,
        double bundleDiscountPercentage) {
}
