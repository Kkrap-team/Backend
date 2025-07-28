package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //토큰 관리 방식
    @PostMapping("/kakao-login")
    @Operation(summary = "카카오 로그인", description = "카카오 서버에서 인가 코드를 통해 로그인 진행하시고 토큰 요청을 보내 받은 걸 이 api에 던져주시면 됩니다")
    public ResponseEntity<UsersProfileResponse> kakaoLogin(@RequestBody KaKaoTokenRequest request){
        var response = authService.prepare(request);
        return ResponseEntity.ok(response);
    }
}
