package com.Kkrap.Controller.Spec;


import com.Kkrap.RequestDTO.*;
import com.Kkrap.ResponseDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "kakao 로그인 관련 API Endpoint")
public interface AuthAPISpec {
    //토큰 관리 방식
    @PostMapping("/kakao-login")
    @Operation(summary = "카카오 로그인", description = "카카오 서버에서 인가 코드를 통해 로그인 진행하시고 토큰 요청을 보내 받은 걸 이 api에 던져주시면 됩니다")
    ResponseEntity<TokenUsersProfileResponse> kakaoLogin(@RequestBody KaKaoTokenRequest request, HttpServletResponse cookie);

    @GetMapping("/refresh")
    @Operation(summary = "AccessToken 재발급", description = "쿠키에 담긴 RefreshToken을 통해 accessToken을 재발급합니다.")
    ResponseEntity<TokenResponse> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenFromCookie,
            HttpServletResponse response
    );
}
