package com.sporty.bookstore.integration.book;

import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookTypeModel;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.repository.book.BookTypeRepository;
import com.sporty.bookstore.service.book.BookTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 13:20 PM
 */
@SpringBootTest
@Transactional
class BookTypeServiceIT {

    @Autowired
    private BookTypeService bookTypeService;

    @Autowired
    private BookTypeRepository bookTypeRepository;

    @Test
    void create_WhenValidModel_ShouldCreateBookType() {
        CreateBookTypeModel model = new CreateBookTypeModel("Test Book Type", 0.8, 0.9);

        BookType result = bookTypeService.create(UUID.randomUUID(), model);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test Book Type", result.getName());
        assertEquals(0.8, result.getPriceMultiplier());
        assertEquals(0.9, result.getBundleDiscount());

        BookType saved = bookTypeRepository.findById(result.getId()).orElseThrow();
        assertEquals(result, saved);
    }

    @Test
    void create_WhenNameExists_ShouldThrowException() {
        CreateBookTypeModel model = new CreateBookTypeModel("Test Book Type", 0.8, 0.9);
        bookTypeService.create(UUID.randomUUID(), model);

        assertThrows(RecordConflictException.class, () ->
                bookTypeService.create(UUID.randomUUID(), model));
    }

    @Test
    void getById_WhenBookTypeExists_ShouldReturnBookType() {
        CreateBookTypeModel model = new CreateBookTypeModel("Test Book Type", 0.8, 0.9);
        BookType created = bookTypeService.create(UUID.randomUUID(), model);

        BookType result = bookTypeService.getById(created.getId());

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals("Test Book Type", result.getName());
    }

    @Test
    void getById_WhenBookTypeNotExists_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(RecordNotFoundException.class, () ->
                bookTypeService.getById(nonExistentId));
    }

    @Test
    void update_WhenValidModel_ShouldUpdateBookType() {
        CreateBookTypeModel createModel = new CreateBookTypeModel("Test Book Type", 0.8, 0.9);
        BookType created = bookTypeService.create(UUID.randomUUID(), createModel);

        UpdateBookTypeModel updateModel = new UpdateBookTypeModel("Updated Book Type", 0.85, 0.95);

        BookType result = bookTypeService.update(created.getId(), updateModel);

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals("Updated Book Type", result.getName());
        assertEquals(0.85, result.getPriceMultiplier());
        assertEquals(0.95, result.getBundleDiscount());

        BookType saved = bookTypeRepository.findById(result.getId()).orElseThrow();
        assertEquals(result, saved);
    }

    @Test
    void update_WhenNameExists_ShouldThrowException() {
        CreateBookTypeModel model1 = new CreateBookTypeModel("Test Book Type 1", 0.8, 0.9);
        CreateBookTypeModel model2 = new CreateBookTypeModel("Test Book Type 2", 0.8, 0.9);

        BookType created1 = bookTypeService.create(UUID.randomUUID(), model1);
        bookTypeService.create(UUID.randomUUID(), model2);

        UpdateBookTypeModel updateModel = new UpdateBookTypeModel("Test Book Type 2", 0.85, 0.95);

        assertThrows(RecordConflictException.class, () ->
                bookTypeService.update(created1.getId(), updateModel));
    }

    @Test
    void getAll_ShouldReturnAllBookTypes() {
        CreateBookTypeModel model1 = new CreateBookTypeModel("Test Book Type 1", 0.8, 0.9);
        CreateBookTypeModel model2 = new CreateBookTypeModel("Test Book Type 2", 0.8, 0.9);

        bookTypeService.create(UUID.randomUUID(), model1);
        bookTypeService.create(UUID.randomUUID(), model2);

        List<BookType> result = bookTypeService.getAll();

        assertNotNull(result);
        assertNotEquals(result.size(), 0);
    }
} 