package com.sporty.bookstore.controller.order;

import com.sporty.bookstore.config.security.audit.ApplicationAuditorAware;
import com.sporty.bookstore.controller.AbstractResponseController;
import com.sporty.bookstore.controller.model.mapper.order.OrderResponseMapper;
import com.sporty.bookstore.controller.model.response.common.PageResponse;
import com.sporty.bookstore.controller.model.response.order.OrderResponse;
import com.sporty.bookstore.domain.entity.order.Order;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.service.order.OrderService;
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

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 8:55 PM
 */
@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Order Api", description = "APIs for getting orders")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class OrderController extends AbstractResponseController {

    private final OrderService customerOrderService;
    private final OrderResponseMapper orderResponseMapper;
    private final ApplicationAuditorAware applicationAuditorAware;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get order by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    public ResponseEntity<OrderResponse> getById(@PathVariable final UUID id) {
        log.info("Received request to get order by id - {}", id);
        Order order = customerOrderService.getById(id);
        return respondOK(orderResponseMapper.toResponse(order));
    }

    @GetMapping("{page}/{size}/pages")
    @Operation(
            summary = "Search order with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid search parameters")
            }
    )
    public ResponseEntity<PageResponse<OrderResponse>> search(
            @PathVariable final int page,
            @PathVariable final int size) {
        log.info("Received request to search orders with page - {} and size - {}", page, size);
        PageModel<Order> result = customerOrderService.getCustomerOrderPages(new PageableModel(page, size), applicationAuditorAware.getCurrentAccountId());
        PageResponse<OrderResponse> response = new PageResponse<>(result
                .items()
                .stream()
                .map(orderResponseMapper::toResponse)
                .collect(Collectors.toList()),
                result.totalCount());
        return respondOK(response);
    }
    
}
