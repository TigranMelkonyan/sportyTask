package com.sporty.bookstore.controller.model.response.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:11 PM
 */
@Data
public class OrderCartPreviewResponse {

    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private int loyaltyPoints;
    private List<OrderCartItemPreviewResponse> items;

}
