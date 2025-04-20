package com.sporty.bookstore.service.mapper.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.book.CreateBookModel;
import com.sporty.bookstore.domain.model.book.UpdateBookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:27 PM
 */
@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "type", source = "bookTypeId", qualifiedByName = "mapBookType")
    Book createModelToEntity(CreateBookModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "type", source = "bookTypeId", qualifiedByName = "mapBookType")
    Book updateModelToEntity(UpdateBookModel model, @MappingTarget Book entity);

    @Named("mapBookType")
    default BookType mapBookType(UUID bookTypeId) {
        BookType bookType = new BookType();
        bookType.setId(bookTypeId);
        return bookType;
    }
}
