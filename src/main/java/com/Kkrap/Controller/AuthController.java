package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.Service.AuthService;
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
    public ResponseEntity<?> kakaoLogin(@RequestBody KaKaoTokenRequest request){
        var response = authService.kakaoLogin(request);
        return ResponseEntity.ok(response);
    }
}
