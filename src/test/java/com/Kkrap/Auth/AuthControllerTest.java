package com.Kkrap.Auth;

import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void accessToken_is_empty() throws Exception {
        String accessToken = "";
        KaKaoTokenRequest request = new KaKaoTokenRequest(accessToken);
        String content =  objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/auth/kakao-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(result).isNotNull();

    }

    // 카카오 유저 정보 요청

}
