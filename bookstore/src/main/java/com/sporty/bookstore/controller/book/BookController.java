package com.sporty.bookstore.controller.book;

import com.sporty.bookstore.controller.AbstractResponseController;
import com.sporty.bookstore.controller.model.mapper.book.BookResponseMapper;
import com.sporty.bookstore.controller.model.mapper.search.SearchPropertiesRequestMapper;
import com.sporty.bookstore.controller.model.request.common.search.SearchPropertiesRequest;
import com.sporty.bookstore.controller.model.response.book.BookResponse;
import com.sporty.bookstore.controller.model.response.common.PageResponse;
import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import com.sporty.bookstore.service.book.BookService;
import com.sporty.bookstore.service.validator.ModelValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Book Api", description = "APIs for getting books in the bookstore")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class BookController extends AbstractResponseController {

    private final BookService bookService;
    private final ModelValidator validator;
    private final BookResponseMapper bookResponseMapper;
    private final SearchPropertiesRequestMapper searchPropertiesRequestMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    public ResponseEntity<BookResponse> getById(@PathVariable final UUID id) {
        log.info("Received request to get book by id - {}", id);
        Book book = bookService.getById(id);
        return respondOK(bookResponseMapper.toResponse(book));
    }

    @GetMapping("search/{page}/{size}")
    @Operation(
            summary = "Search books with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid search parameters")
            }
    )
    public ResponseEntity<PageResponse<BookResponse>> search(
            @PathVariable final int page,
            @PathVariable final int size,
            final SearchPropertiesRequest searchProperties) {
        log.info("Received request to search books with properties - {}", searchProperties);
        validator.validate(searchProperties);
        SearchProperties properties = searchPropertiesRequestMapper.toSearchProperties(searchProperties, page, size);
        PageModel<Book> result = bookService.search(properties);
        PageResponse<BookResponse> response = new PageResponse<>(result
                .items()
                .stream()
                .map(bookResponseMapper::toResponse)
                .collect(Collectors.toList()),
                result.totalCount());
        return respondOK(response);
    }
    
} 