package com.sporty.bookstore.controller.service.mapper.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.model.book.CreateBookModel;
import com.sporty.bookstore.domain.model.book.UpdateBookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    Book createModelToEntity(CreateBookModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    Book updateModelToEntity(UpdateBookModel model, @MappingTarget Book entity);
}
