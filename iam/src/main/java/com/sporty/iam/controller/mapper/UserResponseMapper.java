package com.sporty.iam.controller.mapper;

import com.sporty.iam.controller.model.response.user.UserResponse;
import com.sporty.iam.domain.entity.user.User;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 3/20/25
 * Time: 11:21 AM
 */
@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse toResponse(User user);
}

