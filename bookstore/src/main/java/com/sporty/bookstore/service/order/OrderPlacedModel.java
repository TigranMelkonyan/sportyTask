package com.sporty.bookstore.service.order;

import com.sporty.bookstore.domain.model.order.place.OrderPlaceItemModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:15 PM
 */
public record OrderPlacedModel(
        BigDecimal totalPrice,
        BigDecimal totalDiscount,
        int loyaltyPoints,
        List<OrderPlaceItemModel> items) {
}
