package com.sporty.bookstore.integration.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.model.book.CreateBookModel;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookModel;
import com.sporty.bookstore.domain.model.common.exception.RecordConflictException;
import com.sporty.bookstore.domain.model.common.exception.RecordNotFoundException;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import com.sporty.bookstore.repository.book.BookRepository;
import com.sporty.bookstore.service.book.BookService;
import com.sporty.bookstore.service.book.BookTypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/21/25
 * Time: 13:11 PM
 */
@SpringBootTest
@Transactional
class BookServiceIT {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookTypeService bookTypeService;
    
    private UUID bookTypeId;
    private BookType bookType;

    @BeforeEach
    void setUp() {
        CreateBookTypeModel bookTypeModel = new CreateBookTypeModel(
                "Test Book Type", 0.8, 0.9);
        bookType = bookTypeService.create(UUID.randomUUID(), bookTypeModel);
        bookTypeId = bookType.getId();
    }

    @Test
    void create_WhenValidModel_ShouldCreateBook() {
        CreateBookModel model = new CreateBookModel();
        model.setTitle("Test Book Title");
        model.setAuthor("Test Author");
        model.setBasePrice(BigDecimal.valueOf(50));
        model.setStockQuantity(40);
        model.setBookTypeId(bookTypeId);

        Book result = bookService.create(model);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("Test Book Title", result.getTitle());
        Assertions.assertEquals("Test Author", result.getAuthor());
        Assertions.assertEquals(bookType, result.getType());
        Assertions.assertEquals(ModelStatus.ACTIVE, result.getStatus());

        Book saved = bookRepository.findByIdAndStatus(result.getId(), ModelStatus.ACTIVE).orElseThrow();
        Assertions.assertEquals(result, saved);
    }

    @Test
    void create_WhenAuthorAndTitleExists_ShouldThrowException() {
        CreateBookModel model = new CreateBookModel();
        model.setTitle("Test Book Title");
        model.setAuthor("Test Author");
        model.setBasePrice(BigDecimal.valueOf(50));
        model.setStockQuantity(40);
        model.setBookTypeId(bookTypeId);

        bookService.create(model);

        Assertions.assertThrows(RecordConflictException.class, () -> bookService.create(model));
    }

    @Test
    void getById_WhenBookExists_ShouldReturnBook() {
        CreateBookModel model = new CreateBookModel();
        model.setTitle("Test Book Title");
        model.setAuthor("Test Author");
        model.setBasePrice(BigDecimal.valueOf(50));
        model.setStockQuantity(40);
        model.setBookTypeId(bookTypeId);

        Book created = bookService.create(model);

        Book result = bookService.getById(created.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals("Test Book Title", result.getTitle());
        Assertions.assertEquals("Test Author", result.getAuthor());
    }

    @Test
    void getById_WhenBookNotExists_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        Assertions.assertThrows(RecordNotFoundException.class, () -> bookService.getById(nonExistentId));
    }

    @Test
    void update_WhenValidModel_ShouldUpdateBook() {
        CreateBookModel created = new CreateBookModel();
        created.setTitle("Test Book Title");
        created.setAuthor("Test Author");
        created.setBasePrice(BigDecimal.valueOf(50));
        created.setStockQuantity(40);
        created.setBookTypeId(bookTypeId);

        Book result = bookService.create(created);

        UpdateBookModel updateModel = new UpdateBookModel();
        updateModel.setTitle("Updated Book");
        updateModel.setAuthor("Updated Author");
        updateModel.setBasePrice(BigDecimal.valueOf(10));
        updateModel.setStockQuantity(50);
        updateModel.setBookTypeId(bookTypeId);

        Book updatedResult = bookService.update(result.getId(), updateModel);

        Assertions.assertNotNull(updatedResult);
        Assertions.assertEquals(result.getId(), updatedResult.getId());
        Assertions.assertEquals("Updated Book", updatedResult.getTitle());
        Assertions.assertEquals("Updated Author", updatedResult.getAuthor());
        Assertions.assertEquals(bookType, updatedResult.getType());

        Book saved = bookRepository.findByIdAndStatus(updatedResult.getId(), ModelStatus.ACTIVE).orElseThrow();
        Assertions.assertEquals(updatedResult, saved);
    }

    @Test
    void delete_WhenBookExists_ShouldMarkAsDeleted() {
        CreateBookModel model = new CreateBookModel();
        model.setTitle("Test Book Title");
        model.setAuthor("Test Author");
        model.setBasePrice(BigDecimal.valueOf(50));
        model.setStockQuantity(40);
        model.setBookTypeId(bookTypeId);

        Book created = bookService.create(model);

        bookService.delete(created.getId());

        Book deleted = bookRepository.findById(created.getId()).orElseThrow();
        Assertions.assertEquals(ModelStatus.DELETED, deleted.getStatus());
    }

    @Test
    void search_ShouldReturnPageModel() {
        CreateBookModel model = new CreateBookModel();
        model.setTitle("Test Book Title");
        model.setAuthor("Test Author");
        model.setBasePrice(BigDecimal.valueOf(50));
        model.setStockQuantity(40);
        model.setBookTypeId(bookTypeId);

        bookService.create(model);

        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setStatus(ModelStatus.ACTIVE);
        PageableModel pageable = new PageableModel();
        pageable.setPage(0);
        pageable.setSize(10);
        searchProperties.setPageable(pageable);

        PageModel<Book> result = bookService.search(searchProperties);

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(result.totalCount(), 0);
    }

    @Test
    void save_WhenValidBook_ShouldSaveBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setType(bookType);
        book.setStatus(ModelStatus.ACTIVE);

        Book result = bookService.save(book);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("Test Book", result.getTitle());
        Assertions.assertEquals("Test Author", result.getAuthor());
        Assertions.assertEquals(bookType, result.getType());
        Assertions.assertEquals(ModelStatus.ACTIVE, result.getStatus());

        Book saved = bookRepository.findById(result.getId()).orElseThrow();
        Assertions.assertEquals(saved, result);
    }
} 