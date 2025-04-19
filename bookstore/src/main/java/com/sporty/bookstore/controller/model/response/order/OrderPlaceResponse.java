package com.sporty.bookstore.controller.model.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 3:06 PM
 */
@Data
@NoArgsConstructor  
@AllArgsConstructor
public class OrderPlaceResponse {

    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private int loyaltyPoints;
    private List<OrderPlacedItemResponse> items;
            
}
