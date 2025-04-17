package com.sporty.bookstore.controller.model.mapper.book;

import com.sporty.bookstore.controller.model.request.book.CreateBookRequest;
import com.sporty.bookstore.controller.model.request.book.UpdateBookRequest;
import com.sporty.bookstore.domain.model.book.CreateBookModel;
import com.sporty.bookstore.domain.model.book.UpdateBookModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:27 PM
 */
@Mapper(componentModel = "spring")
public interface BookRequestMapper {

    CreateBookModel toCreateBookModel(CreateBookRequest model);
    
    UpdateBookModel toUpdateBookModel(UpdateBookRequest model);
}
