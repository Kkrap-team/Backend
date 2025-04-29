package com.Kkrap.Auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

@WebMvcTest
@AutoConfigureMockMvc
public class OauthTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName("카카오 로그인 성공 테스트")
    void kakaoLogin() {
        OAuth2User user = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("id", "123456", "email", ""),
                "id"
        );

        // 작업 예정
    }

}
