package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.AuthAPISpec;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.RequestDTO.RefreshTokenRequest;
import com.Kkrap.ResponseDTO.TokenResponse;
import com.Kkrap.ResponseDTO.TokenUsersProfileResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Timed(value = "http.controller", extraTags = {"controller","Auth"})
@RequestMapping("/api/auth")
@Slf4j
public class AuthController implements AuthAPISpec {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //토큰 관리 방식
    @Override
    public ResponseEntity<TokenUsersProfileResponse> kakaoLogin(@RequestBody KaKaoTokenRequest request){
        return ResponseEntity.ok(authService.prepare(request));
    }

    @Override
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshAccessToken(request.getRefreshToken());
    }
}
