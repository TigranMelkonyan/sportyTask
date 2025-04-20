package com.sporty.bookstore.controller.book;

import com.sporty.bookstore.controller.AbstractResponseController;
import com.sporty.bookstore.controller.model.mapper.book.BookTypeResponseMapper;
import com.sporty.bookstore.controller.model.response.book.BookTypeResponse;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.service.book.BookTypeService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 11:15 AM
 */
@RestController
@RequestMapping("api/book-types")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Book Type Api", description = "APIs for getting book types")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class BookTypeController extends AbstractResponseController {

    private final BookTypeService bookTypeService;
    private final BookTypeResponseMapper bookTypeResponseMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get book type by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book type found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Book type not found")
            }
    )
    public ResponseEntity<BookTypeResponse> getById(@PathVariable final UUID id) {
        log.info("Received request to get book type by id - {}", id);
        BookType bookType = bookTypeService.getById(id);
        return respondOK(bookTypeResponseMapper.toResponse(bookType));
    }

    @GetMapping
    @Operation(
            summary = "Get all book types",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book types retrieved successfully")
            }
    )
    public ResponseEntity<List<BookTypeResponse>> getAll() {
        log.info("Received request to get all book types");
        List<BookType> bookTypes = bookTypeService.getAll();
        List<BookTypeResponse> response = bookTypes.stream()
                .map(bookTypeResponseMapper::toResponse)
                .collect(Collectors.toList());
        return respondOK(response);
    }
}
