package com.sporty.iam.service.mapper;

import com.sporty.iam.domain.entity.user.User;
import com.sporty.iam.domain.model.user.UserModel;
import org.mapstruct.Mapper;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 9:02 PM
 */
@Mapper(componentModel = "spring")
public interface UserModelMapper {
    UserModel userToUserDto(User entity);
}
