package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.request.order.OrderCartItemPreviewRequest;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:06 PM
 */
@Mapper(componentModel = "spring")
public interface OrderCartItemPreviewRequestMapper {
    OrderCartPreviewModel toOrderItemPreviewModel(OrderCartItemPreviewRequest orderItemPreviewRequest);
}
