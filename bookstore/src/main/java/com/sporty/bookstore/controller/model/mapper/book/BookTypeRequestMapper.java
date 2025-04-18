package com.sporty.bookstore.controller.model.mapper.book;

import com.sporty.bookstore.controller.model.request.book.CreateBookTypeRequest;
import com.sporty.bookstore.controller.model.request.book.UpdateBookTypeRequest;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:57 AM
 */
@Mapper(componentModel = "spring")
public interface BookTypeRequestMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "pricePercentage", target = "priceMultiplier", qualifiedByName = "toFraction")
    @Mapping(source = "bundleDiscountPercentage", target = "bundleDiscount", qualifiedByName = "toFraction")
    CreateBookTypeModel toCreateModel(CreateBookTypeRequest request);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "pricePercentage", target = "priceMultiplier", qualifiedByName = "toFraction")
    @Mapping(source = "bundleDiscountPercentage", target = "bundleDiscount", qualifiedByName = "toFraction")
    UpdateBookTypeModel toUpdateModel(UpdateBookTypeRequest request);

    @Named("toFraction")
    static double toFraction(double percentage) {
        return percentage / 100.0;
    }
}
