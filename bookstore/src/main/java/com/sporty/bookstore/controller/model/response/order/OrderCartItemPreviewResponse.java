package com.sporty.bookstore.controller.model.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 4:24 PM
 */
public record OrderCartItemPreviewResponse(
        UUID bookId,
        String title,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal totalPrice,
        BigDecimal totalPriceAfterDiscount,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean oneForFree
) {
}
