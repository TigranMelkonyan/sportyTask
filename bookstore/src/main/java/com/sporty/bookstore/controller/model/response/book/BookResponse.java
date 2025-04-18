package com.sporty.bookstore.controller.model.response.book;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:33 PM
 */
public record BookResponse(
        UUID id, String title, String author,
        BigDecimal basePrice, BookTypeResponse bookTypeResponse,
        Integer stockQuantity) {
}
