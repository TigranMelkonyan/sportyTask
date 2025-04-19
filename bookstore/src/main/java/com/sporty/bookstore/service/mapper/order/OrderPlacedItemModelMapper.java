package com.sporty.bookstore.service.mapper.order;

import com.sporty.bookstore.domain.model.order.OrderPlaceItemModel;
import com.sporty.bookstore.domain.model.order.preview.OrderPreviewItemModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:16 PM
 */
@Mapper(componentModel = "spring")
public interface OrderPlacedItemModelMapper {
    OrderPlaceItemModel toModelOrderPlacedModel(OrderPreviewItemModel model);
}
