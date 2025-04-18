package com.sporty.bookstore.service.book;

import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookTypeModel;
import com.sporty.bookstore.domain.model.common.exception.ErrorCode;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.repository.book.BookTypeRepository;
import com.sporty.bookstore.service.mapper.book.BookTypeMapper;
import com.sporty.bookstore.service.validator.ModelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 12:31 AM
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class BookTypeService {

    private final BookTypeMapper mapper;
    private final ModelValidator modelValidator;
    private final BookTypeRepository repository;

    @Transactional(readOnly = true)
    public BookType getById(final UUID id) {
        log.info("Retrieving BookType with id {}", id);
        BookType result = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("Book bookTypeId with id - %s not exists", id)));
        log.info("Successfully retrieved book bookTypeId with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional
    public BookType create(final UUID id, final CreateBookTypeModel model) {
        log.info("Creating book bookTypeId with bookTypeId - {} ", model);
        modelValidator.validate(model);
        assertNotExistsByName(model.name());
        BookType bookType = mapper.createModelToEntity(model);
        BookType result = repository.save(bookType);
        log.info("Successfully created book bookTypeId with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional
    public BookType update(final UUID id, final UpdateBookTypeModel model) {
        log.info("Updating book bookTypeId with bookTypeId - {} ", model);
        modelValidator.validate(model);
        BookType type = getById(id);
        if (!Objects.equals(model.name(), type.getName())) {
            assertNotExistsByName(model.name());
        }
        BookType bookType = mapper.updateModelToEntity(model, type);
        BookType result = repository.save(bookType);
        log.info("Successfully updated book bookTypeId with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<BookType> getAll() {
        log.info("Retrieving book bookTypeId list");
        List<BookType> result = repository.findAll();
        log.info("Successfully retrieved book bookTypeId list, result -{}", result);
        return result;
    }

    private void assertNotExistsByName(final String name) {
        if (repository.existsByName(name)) {
            throw new RecordConflictException(String.format("Book bookTypeId with name - %s already exists", name),
                    ErrorCode.EXISTS_EXCEPTION);
        }
    }

}
