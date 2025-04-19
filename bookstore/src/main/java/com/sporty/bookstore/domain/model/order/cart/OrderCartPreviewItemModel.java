package com.sporty.bookstore.domain.model.order.cart;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 4:24 PM
 */
public record OrderCartPreviewItemModel(
        UUID bookId,
        String title,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal totalPrice,
        BigDecimal totalPriceAfterDiscount,
        Boolean oneForFree
) {
}
