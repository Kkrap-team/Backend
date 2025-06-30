package com.Kkrap.endpoint;

import autoparams.AutoSource;
import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class AuthEndpointTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @AutoSource
    @ParameterizedTest
    @DisplayName("유효하지 않은 토큰이 전달되었을 경우, 에러를 반환한다.")
    void success(KaKaoTokenRequest request) throws Exception {
        String accessToken = UUID.randomUUID().toString();
        request.setAccesstoken(accessToken);
        String content = objectMapper.writeValueAsString(request);

        var result = mockMvc.perform(post("/api/auth/kakao-login").contentType(MediaType.APPLICATION_JSON).content(content));

        result.andExpect(status().is4xxClientError()).andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()));
    }

    @AutoSource
    @ParameterizedTest
    @DisplayName("토큰이 빈 문자열일때, 에러를 반환한다.")
    void tokenEmpty(KaKaoTokenRequest request) throws Exception {
        String accessToken = "";
        request.setAccesstoken(accessToken);
        String content =  objectMapper.writeValueAsString(request);

        var result = mockMvc.perform(post("/api/auth/kakao-login").contentType(MediaType.APPLICATION_JSON).content(content));

        result.andExpect(status().is4xxClientError()).andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()));
    }

}
