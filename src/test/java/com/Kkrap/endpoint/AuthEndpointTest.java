package com.Kkrap.endpoint;

import com.Kkrap.Controller.AuthController;
import com.Kkrap.Exception.GlobalExceptionHandler;
import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.Service.AuthService;
import com.Kkrap.Service.LoginUserPort;
import com.Kkrap.Service.SocialLogin.ClientProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
@Import(GlobalExceptionHandler.class)
public class AuthEndpointTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean AuthService authService;
    @Mock ClientProvider clientProvider;

    String content = "";

    @BeforeEach
    void setUp() {
        KaKaoTokenRequest request = new KaKaoTokenRequest();
        String accessToken = UUID.randomUUID().toString();
        request.setAccesstoken(accessToken);

        try {
            content = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("유효하지 않은 토큰이 전달되었을 경우, 403 에러를 반환한다.")
    void success() throws Exception {
        var result = mockMvc.perform(post("/api/auth/kakao-login").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("토큰이 빈 문자열일때, 에러를 반환한다.")
    void tokenEmpty() throws Exception {
        KaKaoTokenRequest emptyTokenRequest = new KaKaoTokenRequest();
        emptyTokenRequest.setAccesstoken("");  // 빈 문자열 설정

        given(authService.prepare(any(KaKaoTokenRequest.class)))
                .willThrow(new NotValidTokenException("유효하지 않은 토큰입니다."));

        // when & then
        var result = mockMvc.perform(post("/api/auth/kakao-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }
}