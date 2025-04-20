package com.sporty.bookstore.controller.admin;

import com.sporty.bookstore.controller.AbstractResponseController;
import com.sporty.bookstore.controller.model.mapper.order.OrderItemResponseMapper;
import com.sporty.bookstore.controller.model.response.common.PageResponse;
import com.sporty.bookstore.controller.model.response.order.OrderItemResponse;
import com.sporty.bookstore.domain.entity.order.OrderItem;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemsPageModel;
import com.sporty.bookstore.service.order.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:22 PM
 */
@RestController
@RequestMapping("api/admin/order-items")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Order Item Management for Admin", description = "APIs for Admin managing order items")
@PreAuthorize("hasAuthority('ADMIN')")
public class OrderItemAdminController extends AbstractResponseController {

    private final OrderItemService orderItemService;
    private final OrderItemResponseMapper orderItemResponseMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get order item by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order item found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Order item not found")
            }
    )
    public ResponseEntity<OrderItemResponse> getById(@PathVariable final UUID id) {
        log.info("Received request to get order item by id - {}", id);
        OrderItem orderItem = orderItemService.getById(id);
        return respondOK(orderItemResponseMapper.toResponse(orderItem));
    }

    @GetMapping("/order/{orderId}/{page}/{size}")
    @Operation(
            summary = "Get order items by order ID with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order items found and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<PageResponse<OrderItemResponse>> getByOrderId(
            @PathVariable final UUID orderId,
            @PathVariable final int page,
            @PathVariable final int size) {
        log.info("Received request to get order items for order id - {}", orderId);
        PageModel<OrderItem> result = orderItemService.findAllByOrderId(new OrderItemsPageModel(orderId, page, size));
        PageResponse<OrderItemResponse> response = new PageResponse<>(
                result.items().stream()
                        .map(orderItemResponseMapper::toResponse)
                        .collect(Collectors.toList()),
                result.totalCount()
        );
        return respondOK(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an order item",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order item deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<?> delete(@PathVariable final UUID id) {
        log.info("Received request to delete order item with id - {}", id);
        orderItemService.delete(id);
        return respondEmpty();
    }
} 