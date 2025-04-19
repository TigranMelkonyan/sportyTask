package com.sporty.bookstore.domain.model.order.place;

import com.sporty.bookstore.domain.model.order.item.OrderItemModel;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 10:21 PM
 */
public record OrderPlaceModel(@NotEmpty List<OrderItemModel> items) {
}
