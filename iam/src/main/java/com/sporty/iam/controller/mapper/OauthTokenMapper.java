package com.sporty.iam.controller.mapper;

import com.sporty.iam.controller.model.response.oauth.OauthTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Tigran Melkonyan
 * Date: 3/20/25
 * Time: 11:19 AM
 */
@Mapper(componentModel = "spring")
public interface OauthTokenMapper {

    @Mapping(target = "token", source = "token")
    OauthTokenResponse toResponse(String token);
}
