package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.request.order.OrderCartItemPreviewRequest;
import com.sporty.bookstore.domain.model.order.preview.OrderCartItemPreviewModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:06 PM
 */
@Mapper(componentModel = "spring")
public interface OrderCartItemPreviewRequestMapper {
    OrderCartItemPreviewModel toOrderItemPreviewModel(OrderCartItemPreviewRequest orderItemPreviewRequest);
}
