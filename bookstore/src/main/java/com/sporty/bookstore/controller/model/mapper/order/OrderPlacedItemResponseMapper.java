package com.sporty.bookstore.controller.model.mapper.order;

import com.sporty.bookstore.controller.model.response.order.OrderPlacedItemResponse;
import com.sporty.bookstore.domain.model.order.OrderPlaceItemModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 9:13 PM
 */
@Mapper(componentModel = "spring")
public interface OrderPlacedItemResponseMapper {
    OrderPlacedItemResponse toResponse(OrderPlaceItemModel model);
}
