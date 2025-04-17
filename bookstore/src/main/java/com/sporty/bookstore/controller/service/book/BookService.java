package com.sporty.bookstore.controller.service.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.model.book.CreateBookModel;
import com.sporty.bookstore.domain.model.book.UpdateBookModel;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.exception.ErrorCode;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import com.sporty.bookstore.repository.book.BookRepository;
import com.sporty.bookstore.controller.service.mapper.book.BookMapper;
import com.sporty.bookstore.controller.service.validator.ModelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 11:41 AM
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class BookService {

    private final BookMapper mapper;
    private final ModelValidator validator;
    private final BookRepository repository;

    @Transactional(readOnly = true)
    public Book getById(final UUID id) {
        log.info("Retrieving book with id - {} ", id);
        Assert.notNull(id, "id must not be null");
        Book result = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("Book with id - %s not exists", id)));
        log.info("Successfully retrieved book with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional
    public Book create(final CreateBookModel model) {
        log.info("Creating book with model - {} ", model);
        validator.validate(model);
        assertNotExistsWithAuthorAndTitle(model.getAuthor(), model.getTitle());
        Book entity = mapper.createModelToEntity(model);
        Book result = repository.save(entity);
        log.info("Successfully created book with model - {}, result - {}", model, result);
        return result;
    }

    @Transactional
    public Book update(final UUID id, final UpdateBookModel model) {
        log.info("Updating book with id - {}", id);
        validator.validate(model);
        Book book = getById(id);
        boolean isAuthorChanged = !Objects.equals(book.getAuthor(), model.getAuthor());
        boolean isTitleChanged = !Objects.equals(book.getTitle(), model.getTitle());
        if (isAuthorChanged || isTitleChanged) {
            assertNotExistsWithAuthorAndTitle(model.getAuthor(), model.getTitle());
        }
        Book entity = mapper.updateModelToEntity(model, book);
        Book result = repository.save(entity);
        log.info("Successfully updated book with id - {}", id);
        return result;
    }

    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting book with id - {} ", id);
        Assert.notNull(id, "id must not be null");
        Book book = getById(id);
        book.setStatus(ModelStatus.DELETED);
        repository.save(book);
        log.info("Successfully deleted book with id - {}", id);
    }

    @Transactional(readOnly = true)
    public PageModel<Book> search(final SearchProperties searchProperties) {
        log.info("Searching books with request - {}", searchProperties);
        validator.validate(searchProperties);
        PageModel<Book> book = repository.search(searchProperties);
        log.info("Successfully retrieved books - {}", book);
        return new PageModel<>(book.items(), book.totalCount());
    }

    private void assertNotExistsWithAuthorAndTitle(final String author, final String title) {
        if (repository.existsByAuthorAndTitle(author, title)) {
            throw new RecordConflictException(
                    String.format("Book exists with author '%s' and title '%s'", author, title),
                    ErrorCode.RECORD_CONFLICT
            );
        }
    }
}
