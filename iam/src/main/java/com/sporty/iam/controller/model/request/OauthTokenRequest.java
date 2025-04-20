package com.sporty.iam.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tigran Melkonyan
 * Date: 2/19/25
 * Time: 12:59 PM
 */
@Getter
@Setter
public class OauthTokenRequest {

    @NotBlank(message = "required")
    private String userName;

    @NotBlank(message = "required")
    private String password;
}

