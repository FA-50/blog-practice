package io.backend.blogproject.controller;

import io.backend.blogproject.common.ApiResult;
import io.backend.blogproject.common.JwtCookie;
import io.backend.blogproject.common.JwtProperties;
import io.backend.blogproject.constant.SuccessCode;
import io.backend.blogproject.domain.dto.KeyPair;
import io.backend.blogproject.domain.dto.UserLoginRequest;
import io.backend.blogproject.domain.dto.UserLoginResponse;
import io.backend.blogproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;
    private final String TOKEN_TYPE = "Bearer";

    @PostMapping("/login")
    public ResponseEntity<ApiResult<UserLoginResponse>> signIn(
            @RequestBody UserLoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ){
        KeyPair keyPair = authService.signIn(request);
        httpResponse.addHeader(
                HttpHeaders.SET_COOKIE,
                JwtCookie.accessToken(
                        keyPair.accessToken(),
                        jwtProperties.getValidations().getAccess(),
                        httpRequest.isSecure()
                ).toString()
        );
        return ApiResult.data(
                SuccessCode.LOGIN_SUCCESS,
                new UserLoginResponse(
                        keyPair.accessToken(),
                        keyPair.refreshToken(),
                        TOKEN_TYPE
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                JwtCookie.expired(request.isSecure()).toString()
        );
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/login"))
                .build();
    }
}

