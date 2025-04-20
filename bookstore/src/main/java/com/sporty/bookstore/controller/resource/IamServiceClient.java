package com.sporty.bookstore.controller.resource;

import com.sporty.bookstore.controller.model.resource.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 12:19 PM
 */
@FeignClient(name = "iam-service", url = "${feign.client.iam-service.url}")
public interface IamServiceClient {

    @GetMapping("/api/users-info/{id}/loyalty-points")
    ResponseEntity<Integer> getUserLoyaltyPoints(@PathVariable("id") UUID userId);
    
    @PutMapping("/api/users-info/{id}/loyalty-points")
    ResponseEntity<UserResponse> updateUserLoyaltyPoints(@PathVariable("id") UUID userId, @RequestParam final int loyaltyPoints);
}
