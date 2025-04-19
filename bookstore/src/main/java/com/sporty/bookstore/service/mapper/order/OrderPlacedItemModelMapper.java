package com.sporty.bookstore.service.mapper.order;

import com.sporty.bookstore.domain.model.order.place.OrderPlaceItemModel;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewItemModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:16 PM
 */
@Mapper(componentModel = "spring")
public interface OrderPlacedItemModelMapper {
    OrderPlaceItemModel toModelOrderPlacedModel(OrderCartPreviewItemModel model);
}
