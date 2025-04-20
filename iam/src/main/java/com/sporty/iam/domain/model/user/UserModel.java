package com.sporty.iam.domain.model.user;

import com.sporty.iam.domain.model.common.role.Role;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 9:03 PM
 */
public record UserModel(
        UUID id,
        String email,
        String password,
        Role role) {
}
