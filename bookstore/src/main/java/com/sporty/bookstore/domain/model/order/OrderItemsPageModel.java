package com.sporty.bookstore.domain.model.order;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 9:49 PM
 */
public record OrderItemsPageModel(UUID orderId, int page, int size) {
}
