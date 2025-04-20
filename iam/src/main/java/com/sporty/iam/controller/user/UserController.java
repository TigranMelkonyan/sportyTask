package com.sporty.iam.controller.user;

import com.sporty.iam.controller.AbstractResponseController;
import com.sporty.iam.controller.mapper.UserResponseMapper;
import com.sporty.iam.controller.model.response.user.UserResponse;
import com.sporty.iam.domain.entity.user.User;
import com.sporty.iam.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/users-info")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "User Api", description = "APIs for getting users info")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class UserController extends AbstractResponseController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    @GetMapping("/{id}/loyalty-points")
    @Operation(
            summary = "Get user loyalty points by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User loyalty points retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<Integer> getLoyaltyPoints(@PathVariable final UUID id) {
        log.info("Received request to get user loyalty points by id - {}", id);
        int loyaltyPoints = userService.getLoyaltyPoints(id);
        return respondOK(loyaltyPoints);
    }


    @PutMapping("/{id}/loyalty-points")
    @Operation(
            summary = "Update user loyalty points",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User loyalty points updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<UserResponse> updateLoyaltyPoints(
            @PathVariable final UUID id,
            @RequestParam final int loyaltyPoints) {
        log.info("Received request to update user loyalty points for id - {} with points - {}", id, loyaltyPoints);
        User user = userService.updateLoyaltyPoints(id, loyaltyPoints);
        return respondOK(userResponseMapper.toResponse(user));
    }
} 