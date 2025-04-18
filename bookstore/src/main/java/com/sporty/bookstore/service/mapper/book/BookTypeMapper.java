package com.sporty.bookstore.service.mapper.book;

import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:27 PM
 */
@Mapper(componentModel = "spring")
public interface BookTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    BookType createModelToEntity(CreateBookTypeModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "deletedOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    BookType updateModelToEntity(UpdateBookTypeModel model, @MappingTarget BookType entity);
}

