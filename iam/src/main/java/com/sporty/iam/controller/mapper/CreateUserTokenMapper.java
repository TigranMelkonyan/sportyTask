package com.sporty.iam.controller.mapper;

import com.sporty.iam.controller.model.request.OauthTokenRequest;
import com.sporty.iam.domain.model.common.token.CreateUserTokenModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 3/20/25
 * Time: 11:21 AM
 */
@Mapper(componentModel = "spring")
public interface CreateUserTokenMapper {
    CreateUserTokenModel toCreateUserTokenModel(OauthTokenRequest request);
}

