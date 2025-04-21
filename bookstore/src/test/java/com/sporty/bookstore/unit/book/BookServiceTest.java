package com.sporty.bookstore.unit.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.model.book.CreateBookModel;
import com.sporty.bookstore.domain.model.book.UpdateBookModel;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import com.sporty.bookstore.repository.book.BookRepository;
import com.sporty.bookstore.service.book.BookService;
import com.sporty.bookstore.service.book.BookTypeService;
import com.sporty.bookstore.service.mapper.book.BookMapper;
import com.sporty.bookstore.service.validator.ModelValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 12:50 PM
 */

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper mapper;

    @Mock
    private ModelValidator validator;

    @Mock
    private BookTypeService bookTypeService;

    @InjectMocks
    private BookService bookService;

    private UUID bookId;
    private UUID bookTypeId;
    private Book book;
    private BookType bookType;
    private CreateBookModel createModel;
    private UpdateBookModel updateModel;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        bookTypeId = UUID.randomUUID();

        bookType = new BookType();
        bookType.setId(bookTypeId);
        bookType.setName("Test Book Type");

        book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setType(bookType);
        book.setStatus(ModelStatus.ACTIVE);

        createModel = new CreateBookModel();
        createModel.setTitle("Test Book");
        createModel.setAuthor("Test Author");
        createModel.setBookTypeId(bookTypeId);

        updateModel = new UpdateBookModel();
        updateModel.setTitle("Updated Book");
        updateModel.setAuthor("Updated Author");
        updateModel.setBookTypeId(bookTypeId);
    }

    @Test
    void getById_WhenBookExists_ShouldReturnBook() {
        when(repository.findByIdAndStatus(bookId, ModelStatus.ACTIVE)).thenReturn(Optional.of(book));

        Book result = bookService.getById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(repository).findByIdAndStatus(bookId, ModelStatus.ACTIVE);
    }

    @Test
    void getById_WhenBookNotExists_ShouldThrowException() {
        when(repository.findByIdAndStatus(bookId, ModelStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookService.getById(bookId));
        verify(repository).findByIdAndStatus(bookId, ModelStatus.ACTIVE);
    }

    @Test
    void create_WhenValidModel_ShouldCreateBook() {
        when(mapper.createModelToEntity(createModel)).thenReturn(book);
        when(bookTypeService.getById(bookTypeId)).thenReturn(bookType);
        when(repository.save(any(Book.class))).thenReturn(book);
        when(repository.existsByAuthorAndTitle(createModel.getAuthor(), createModel.getTitle())).thenReturn(false);

        Book result = bookService.create(createModel);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(validator).validate(createModel);
        verify(bookTypeService).getById(bookTypeId);
        verify(repository).save(book);
    }

    @Test
    void create_WhenAuthorAndTitleExists_ShouldThrowException() {
        when(repository.existsByAuthorAndTitle(createModel.getAuthor(), createModel.getTitle())).thenReturn(true);

        assertThrows(RecordConflictException.class, () -> bookService.create(createModel));
        verify(repository).existsByAuthorAndTitle(createModel.getAuthor(), createModel.getTitle());
    }

    @Test
    void update_WhenValidModel_ShouldUpdateBook() {
        when(repository.findByIdAndStatus(bookId, ModelStatus.ACTIVE)).thenReturn(Optional.of(book));
        when(bookTypeService.getById(bookTypeId)).thenReturn(bookType);
        when(mapper.updateModelToEntity(updateModel, book)).thenReturn(book);
        when(repository.save(any(Book.class))).thenReturn(book);
        when(repository.existsByAuthorAndTitle(updateModel.getAuthor(), updateModel.getTitle())).thenReturn(false);

        Book result = bookService.update(bookId, updateModel);

        assertNotNull(result);
        verify(validator).validate(updateModel);
        verify(bookTypeService).getById(bookTypeId);
        verify(repository).save(book);
    }

    @Test
    void delete_WhenBookExists_ShouldMarkAsDeleted() {
        when(repository.findByIdAndStatus(bookId, ModelStatus.ACTIVE)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenReturn(book);

        bookService.delete(bookId);

        assertEquals(ModelStatus.DELETED, book.getStatus());
        verify(repository).save(book);
    }

    @Test
    void search_ShouldReturnPageModel() {
        SearchProperties searchProperties = new SearchProperties();
        PageModel<Book> expectedPageModel = new PageModel<>(List.of(book), 1L);
        when(repository.search(searchProperties)).thenReturn(expectedPageModel);

        PageModel<Book> result = bookService.search(searchProperties);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        verify(validator).validate(searchProperties);
        verify(repository).search(searchProperties);
    }

    @Test
    void save_WhenValidBook_ShouldSaveBook() {
        when(repository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(repository).save(book);
    }

    @Test
    void save_WhenDataIntegrityViolation_ShouldThrowException() {
        when(repository.save(book)).thenThrow(new DataIntegrityViolationException("Test exception"));

        assertThrows(RecordConflictException.class, () -> bookService.save(book));
        verify(repository).save(book);
    }
} 