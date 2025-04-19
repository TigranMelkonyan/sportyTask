package com.sporty.bookstore.domain.model.order.preview;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 4:26 PM
 */
public record OrderCartPreview(
        BigDecimal totalPrice,
        BigDecimal totalDiscount,
        int loyaltyPoints,
        List<OrderPreviewItemModel> items) {
}