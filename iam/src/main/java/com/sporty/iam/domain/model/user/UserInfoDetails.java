package com.sporty.iam.domain.model.user;

import com.sporty.iam.domain.model.common.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 8:56 PM
 */
public record UserInfoDetails(
        @NotBlank String userId,
        @NotBlank String email,
        @NotNull Role role) {
}
