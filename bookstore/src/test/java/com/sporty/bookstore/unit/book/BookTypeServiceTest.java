package com.sporty.bookstore.unit.book;

import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookTypeModel;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.repository.book.BookTypeRepository;
import com.sporty.bookstore.service.book.BookTypeService;
import com.sporty.bookstore.service.mapper.book.BookTypeMapper;
import com.sporty.bookstore.service.validator.ModelValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 12:59 PM
 */
@ExtendWith(MockitoExtension.class)
class BookTypeServiceTest {

    @Mock
    private BookTypeRepository repository;

    @Mock
    private BookTypeMapper mapper;

    @Mock
    private ModelValidator modelValidator;

    @InjectMocks
    private BookTypeService bookTypeService;

    private UUID bookTypeId;
    private BookType bookType;
    private CreateBookTypeModel createModel;
    private UpdateBookTypeModel updateModel;

    @BeforeEach
    void setUp() {
        bookTypeId = UUID.randomUUID();
        bookType = new BookType();
        bookType.setId(bookTypeId);
        bookType.setName("Test Book Type");
        bookType.setPriceMultiplier(0.8);
        bookType.setBundleDiscount(0.9);

        createModel = new CreateBookTypeModel("Test Book Type", 0.8, 0.9);
        updateModel = new UpdateBookTypeModel("Updated Book Type", 0.85, 0.95);
    }

    @Test
    void getById_WhenBookTypeExists_ShouldReturnBookType() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.of(bookType));

        BookType result = bookTypeService.getById(bookTypeId);

        assertNotNull(result);
        assertEquals(bookTypeId, result.getId());
        verify(repository).findById(bookTypeId);
    }

    @Test
    void getById_WhenBookTypeNotExists_ShouldThrowException() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookTypeService.getById(bookTypeId));
        verify(repository).findById(bookTypeId);
    }

    @Test
    void create_WhenValidModel_ShouldCreateBookType() {
        when(mapper.createModelToEntity(createModel)).thenReturn(bookType);
        when(repository.save(any(BookType.class))).thenReturn(bookType);
        when(repository.existsByName(createModel.name())).thenReturn(false);

        BookType result = bookTypeService.create(bookTypeId, createModel);

        assertNotNull(result);
        assertEquals(bookTypeId, result.getId());
        verify(modelValidator).validate(createModel);
        verify(repository).existsByName(createModel.name());
        verify(repository).save(bookType);
    }

    @Test
    void create_WhenNameExists_ShouldThrowException() {
        when(repository.existsByName(createModel.name())).thenReturn(true);

        assertThrows(RecordConflictException.class, () -> bookTypeService.create(bookTypeId, createModel));
        verify(repository).existsByName(createModel.name());
    }

    @Test
    void update_WhenValidModel_ShouldUpdateBookType() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.of(bookType));
        when(repository.existsByName(updateModel.name())).thenReturn(false);
        when(mapper.updateModelToEntity(updateModel, bookType)).thenReturn(bookType);
        when(repository.save(any(BookType.class))).thenReturn(bookType);

        BookType result = bookTypeService.update(bookTypeId, updateModel);

        assertNotNull(result);
        verify(modelValidator).validate(updateModel);
        verify(repository).findById(bookTypeId);
        verify(repository).save(bookType);
    }

    @Test
    void update_WhenNameExists_ShouldThrowException() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.of(bookType));
        when(repository.existsByName(updateModel.name())).thenReturn(true);

        assertThrows(RecordConflictException.class, () -> bookTypeService.update(bookTypeId, updateModel));
        verify(repository).existsByName(updateModel.name());
    }

    @Test
    void getAll_ShouldReturnAllBookTypes() {
        List<BookType> bookTypes = List.of(bookType);
        when(repository.findAll()).thenReturn(bookTypes);

        List<BookType> result = bookTypeService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAll();
    }
} 