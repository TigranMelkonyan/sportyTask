package com.sporty.bookstore.controller.order;

import com.sporty.bookstore.config.security.audit.ApplicationAuditorAware;
import com.sporty.bookstore.controller.AbstractResponseController;
import com.sporty.bookstore.controller.model.mapper.order.OrderCartItemPreviewRequestMapper;
import com.sporty.bookstore.controller.model.mapper.order.OrderCartItemPreviewResponseMapper;
import com.sporty.bookstore.controller.model.mapper.order.OrderCartPreviewResponseMapper;
import com.sporty.bookstore.controller.model.mapper.order.OrderItemRequestMapper;
import com.sporty.bookstore.controller.model.mapper.order.OrderPlacedItemResponseMapper;
import com.sporty.bookstore.controller.model.request.order.OrderCartItemPreviewRequest;
import com.sporty.bookstore.controller.model.request.order.OrderItemRequest;
import com.sporty.bookstore.controller.model.request.order.OrderPlaceRequest;
import com.sporty.bookstore.controller.model.response.order.OrderCartItemPreviewResponse;
import com.sporty.bookstore.controller.model.response.order.OrderCartPreviewResponse;
import com.sporty.bookstore.controller.model.response.order.OrderPlaceResponse;
import com.sporty.bookstore.controller.model.response.order.OrderPlacedItemResponse;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreview;
import com.sporty.bookstore.domain.model.order.cart.OrderCartPreviewModel;
import com.sporty.bookstore.domain.model.order.item.OrderItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceItemModel;
import com.sporty.bookstore.domain.model.order.place.OrderPlaceModel;
import com.sporty.bookstore.service.order.OrderCartProcessService;
import com.sporty.bookstore.domain.model.order.place.OrderPlacedModel;
import com.sporty.bookstore.service.validator.ModelValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 8:55 PM
 */
@RestController
@RequestMapping("api/order_process")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Order Checkout APi", description = "APIs for order preview and process")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class OrderProcessController extends AbstractResponseController {

    private final ModelValidator validator;
    private final OrderCartProcessService orderProcessService;
    private final OrderItemRequestMapper orderPlaceRequestMapper;
    private final ApplicationAuditorAware applicationAuditorAware;
    private final OrderCartPreviewResponseMapper orderPreviewResponseMapper;
    private final OrderPlacedItemResponseMapper orderPlacedItemResponseMapper;
    private final OrderCartItemPreviewRequestMapper orderItemPreviewRequestMapper;
    private final OrderCartItemPreviewResponseMapper orderItemPreviewResponseMapper;

    @PostMapping("/cart_preview")
    @Operation(
            summary = "Calculate order cart preview",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order preview calculated and returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
            }
    )
    public ResponseEntity<OrderCartPreviewResponse> getOrderPreview(@RequestBody final List<OrderCartItemPreviewRequest> request) {
        log.info("Received request to calculate order with request - {}", request);
        validator.validate(request);
        List<OrderCartPreviewModel> list = new ArrayList<>();
        request.forEach(o -> list.add(orderItemPreviewRequestMapper.toOrderItemPreviewModel(o)));
        UUID customerId = applicationAuditorAware.getCurrentAccountId();
        OrderCartPreview orderItem = orderProcessService.calculateCartPreview(list, customerId);
        OrderCartPreviewResponse orderPreviewResponse = orderPreviewResponseMapper.toResponse(orderItem);
        List<OrderCartItemPreviewResponse> orderItemPreviewResponses = new ArrayList<>();
        orderItem.items().forEach(o -> orderItemPreviewResponses.add(orderItemPreviewResponseMapper.toResponse(o)));
        orderPreviewResponse.setItems(orderItemPreviewResponses);
        return respondOK(orderPreviewResponse);
    }

    @PostMapping("/place")
    @Operation(
            summary = "Place an order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order placed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
            }
    )
    public ResponseEntity<OrderPlaceResponse> placeOrder(@RequestBody final OrderPlaceRequest request) {
        log.info("Received request to place an order with request - {}", request);
        validator.validate(request);
        List<OrderItemRequest> items = request.items();
        List<OrderItemModel> orderItemModels = new ArrayList<>();
        items.forEach(o -> orderItemModels.add(orderPlaceRequestMapper.toModelOrderItemModel(o)));
        UUID customerId = applicationAuditorAware.getCurrentAccountId();
        OrderPlacedModel model = orderProcessService.placeOrder(new OrderPlaceModel(orderItemModels), customerId);
        List<OrderPlacedItemResponse> orderPlacedItemResponses = new ArrayList<>();
        List<OrderPlaceItemModel> orderPlaceItemModels = model.items();
        orderPlaceItemModels.forEach(o -> orderPlacedItemResponses.add(orderPlacedItemResponseMapper.toResponse(o)));
        OrderPlaceResponse response = new OrderPlaceResponse(
                model.totalPrice(), model.totalDiscount(),
                model.loyaltyPoints(), orderPlacedItemResponses);
        return respondOK(response);
    }

}
