package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.AuthAPISpec;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.AuthService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Timed(value = "http.controller", extraTags = {"controller","Auth"})
@RequestMapping("/api/auth")
public class AuthController implements AuthAPISpec {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //토큰 관리 방식
    @Override
    public ResponseEntity<UsersProfileResponse> kakaoLogin(@RequestBody KaKaoTokenRequest request){
        var response = authService.prepare(request);
        return ResponseEntity.ok(response);
    }
}
