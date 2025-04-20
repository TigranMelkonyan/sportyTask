package com.sporty.iam.controller.model.response.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Tigran Melkonyan
 * Date: 2/19/25
 * Time: 12:45 PM
 */
@Getter
@RequiredArgsConstructor
public class OauthTokenResponse {

    private final String token;
    
}
