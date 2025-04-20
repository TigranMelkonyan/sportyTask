package com.sporty.bookstore.controller.model.resource;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 2:58 PM
 */
public record UserResponse(UUID id, String username, int loyaltyPoints) {
} 
