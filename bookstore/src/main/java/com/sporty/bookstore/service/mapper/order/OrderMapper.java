package com.sporty.bookstore.service.mapper.order;

import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.model.order.CreateOrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 9:04 PM
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    Order createModelToEntity(CreateOrderModel model);
}
