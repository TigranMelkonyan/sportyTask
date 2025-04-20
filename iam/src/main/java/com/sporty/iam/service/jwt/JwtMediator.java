package com.sporty.iam.service.jwt;

import com.sporty.iam.domain.model.common.exception.ErrorCode;
import com.sporty.iam.domain.model.common.exception.RecordConflictException;
import com.sporty.iam.domain.model.common.token.CreateUserTokenModel;
import com.sporty.iam.domain.model.user.UserInfoDetails;
import com.sporty.iam.domain.model.user.UserModel;
import com.sporty.iam.service.mapper.UserModelMapper;
import com.sporty.iam.service.user.UserService;
import com.sporty.iam.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 9:00 PM
 */
@Service
@RequiredArgsConstructor
public class JwtMediator {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserModelMapper userDtoMapper;


    public String grantToken(final CreateUserTokenModel request) {
        UserModel userModel = userDtoMapper.userToUserDto(userService.getByUserName(request.userName()));
        if (!PasswordUtils.isPasswordMatch(request.password(), userModel.password())) {
            throw new RecordConflictException("Invalid credentials", ErrorCode.INVALID_CREDENTIALS);
        }
        return jwtService.createJwt(new UserInfoDetails(userModel.id().toString(), userModel.email(), userModel.role()));
    }
}
