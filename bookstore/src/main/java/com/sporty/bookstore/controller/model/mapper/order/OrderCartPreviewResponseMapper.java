package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.response.order.OrderCartPreviewResponse;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:13 PM
 */
@Mapper(componentModel = "spring")
public interface OrderCartPreviewResponseMapper {

    @Mapping(source = "loyaltyPoints", target = "currentLoyaltyPoints")
    @Mapping(target = "items", ignore = true)
    OrderCartPreviewResponse toResponse(OrderCartPreview preview);
}

