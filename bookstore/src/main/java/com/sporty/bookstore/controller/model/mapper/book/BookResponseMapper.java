package com.sporty.bookstore.controller.model.mapper.book;

import com.sporty.bookstore.controller.model.response.book.BookResponse;
import com.sporty.bookstore.domain.entity.book.Book;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:27 PM
 */
@Mapper(componentModel = "spring")
public interface BookResponseMapper {
    BookResponse toResponse(Book model);
}
