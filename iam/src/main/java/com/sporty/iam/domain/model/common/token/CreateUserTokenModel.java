package com.sporty.iam.domain.model.common.token;

import jakarta.validation.constraints.NotNull;

/**
 * Created by Tigran Melkonyan
 * Date: 2/19/25
 * Time: 12:20 PM
 */

public record CreateUserTokenModel(@NotNull(message = "userName required") String userName,
                                   @NotNull(message = "userId required") String password) {
}
