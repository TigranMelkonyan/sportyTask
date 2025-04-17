package com.sporty.bookstore.service.mapper.order;

import com.sporty.bookstore.domain.entity.order.OrderItem;
import com.sporty.bookstore.domain.model.order.CreateOrderItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 9:04 PM
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderItem createModelToEntity(CreateOrderItemModel model);
    
}
