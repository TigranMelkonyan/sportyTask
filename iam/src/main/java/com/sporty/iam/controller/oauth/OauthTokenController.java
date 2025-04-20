package com.sporty.iam.controller.oauth;

import com.sporty.iam.controller.AbstractResponseController;
import com.sporty.iam.controller.mapper.CreateUserTokenMapper;
import com.sporty.iam.controller.mapper.OauthTokenMapper;
import com.sporty.iam.controller.model.request.OauthTokenRequest;
import com.sporty.iam.controller.model.response.oauth.OauthTokenResponse;
import com.sporty.iam.service.jwt.JwtMediator;
import com.sporty.iam.service.validator.ModelValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tigran Melkonyan
 * Date: 2/19/25
 * Time: 12:41 PM
 */
@RestController
@RequestMapping("iam/oauth/token")
@RequiredArgsConstructor
public class OauthTokenController extends AbstractResponseController {

    private final JwtMediator jwtMediator;
    private final ModelValidator modelValidator;
    private final OauthTokenMapper oauthTokenMapper;
    private final CreateUserTokenMapper createUserTokenMapper;


    @PostMapping
    @Operation(
            summary = "Grant jwt token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Granted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Conflict")
            }
    )
    public ResponseEntity<OauthTokenResponse> grantToken(@RequestBody OauthTokenRequest request) {
        modelValidator.validate(request);
        String token = jwtMediator.grantToken(createUserTokenMapper.toCreateUserTokenModel(request));
        return respondOK(oauthTokenMapper.toResponse(token));
    }

}
