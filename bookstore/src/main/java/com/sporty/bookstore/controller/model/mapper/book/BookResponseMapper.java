package com.sporty.bookstore.controller.model.mapper.book;

import com.sporty.bookstore.controller.model.response.book.BookResponse;
import com.sporty.bookstore.controller.model.response.book.BookTypeResponse;
import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:27 PM
 */
@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    @Mapping(source = "type", target = "bookTypeResponse")
    BookResponse toResponse(Book model);

    default BookTypeResponse toBookTypeResponse(BookType type) {
        return new BookTypeResponse(
                type.getId(),
                type.getName(),
                type.getPriceMultiplier() * 100,
                type.getBundleDiscount() * 100,
                type.isEligibleForDiscount()
        );
    }
}
