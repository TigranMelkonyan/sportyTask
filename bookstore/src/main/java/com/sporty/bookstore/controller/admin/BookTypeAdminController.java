package com.sporty.bookstore.controller.admin;

import com.sporty.bookstore.controller.AbstractResponseController;
import com.sporty.bookstore.controller.model.mapper.book.BookTypeRequestMapper;
import com.sporty.bookstore.controller.model.mapper.book.BookTypeResponseMapper;
import com.sporty.bookstore.controller.model.request.book.CreateBookTypeRequest;
import com.sporty.bookstore.controller.model.request.book.UpdateBookTypeRequest;
import com.sporty.bookstore.controller.model.response.book.BookTypeResponse;
import com.sporty.bookstore.domain.entity.book.BookType;
import com.sporty.bookstore.domain.model.book.CreateBookTypeModel;
import com.sporty.bookstore.domain.model.book.UpdateBookTypeModel;
import com.sporty.bookstore.service.book.BookTypeService;
import com.sporty.bookstore.service.validator.ModelValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("api/admin/book-types")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Book Type Management", description = "APIs for managing book types")
public class BookTypeAdminController extends AbstractResponseController {

    private final ModelValidator validator;
    private final BookTypeService bookTypeService;
    private final BookTypeRequestMapper bookTypeRequestMapper;
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

    @PostMapping
    @Operation(
            summary = "Create a new book type",
            description = """
                    Creates a new book type with the following pricing and discount rules:
                    - **Price Multiplier**: Specifies the book's price as a percentage of the base price. For example:
                        - If set to **80**, the book's price will be 80% of the base price.
                    - **Bundle Discount**: Specifies the percentage discount applied to the book's price after the price multiplier is applied. For example:
                        - If set to **95**, the book's price after applying the multiplier will be reduced by 5%.
                    - Both values are in percentage format (0 to 100).
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book type created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Book type with same name already exists")
            }
    )
    public ResponseEntity<BookTypeResponse> create(@RequestBody final CreateBookTypeRequest request) {
        log.info("Received request to create book type with request - {}", request);
        validator.validate(request);
        CreateBookTypeModel model = bookTypeRequestMapper.toCreateModel(request);
        BookType bookType = bookTypeService.create(UUID.randomUUID(), model);
        return respondOK(bookTypeResponseMapper.toResponse(bookType));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing book type",
            description = """
                    Creates a new book type with the following pricing and discount rules:
                    - **Price Multiplier**: Specifies the book's price as a percentage of the base price. For example:
                        - If set to **80**, the book's price will be 80% of the base price.
                    - **Bundle Discount**: Specifies the percentage discount applied to the book's price after the price multiplier is applied. For example:
                        - If set to **95**, the book's price after applying the multiplier will be reduced by 5%.
                    - Both values are in percentage format (0 to 100).
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book type updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Book type not found"),
                    @ApiResponse(responseCode = "409", description = "Book type with same name already exists")
            }
    )
    public ResponseEntity<BookTypeResponse> update(
            @PathVariable final UUID id,
            @RequestBody final UpdateBookTypeRequest request) {
        log.info("Received request to update book type with id - {} and request - {}", id, request);
        validator.validate(request);
        UpdateBookTypeModel model = bookTypeRequestMapper.toUpdateModel(request);
        BookType bookType = bookTypeService.update(id, model);
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
