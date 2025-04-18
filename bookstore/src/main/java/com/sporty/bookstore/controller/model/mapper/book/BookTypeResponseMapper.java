package com.sporty.bookstore.controller.model.mapper.book;

import com.sporty.bookstore.controller.model.response.book.BookTypeResponse;
import com.sporty.bookstore.domain.entity.book.BookType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:21 AM
 */

@Mapper(componentModel = "spring")
public interface BookTypeResponseMapper {

    @Mapping(source = "priceMultiplier", target = "pricePercentage", qualifiedByName = "toPercentage")
    @Mapping(source = "bundleDiscount", target = "bundleDiscountPercentage", qualifiedByName = "toPercentage")
    BookTypeResponse toResponse(BookType model);

    @Named("toPercentage")
    static double toPercentage(double fraction) {
        return fraction * 100.0;
    }
}
