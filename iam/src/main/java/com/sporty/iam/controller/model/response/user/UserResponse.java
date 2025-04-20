package com.sporty.iam.controller.model.response.user;

import java.util.UUID;

public record UserResponse(UUID id, String username, int loyaltyPoints) {
} 