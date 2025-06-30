package com.Kkrap.endpoint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RestTemplateTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.provider.kakao.authorization-uri}")
    private String authUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Test
    @DisplayName("카카오 콜백 로그인 요청")
    void request() {
        ResponseEntity<String> response = restTemplate.getForEntity(authUri + "/{response_type}" + "/{client_id}" + "/{redirect_uri}",
                String.class,
                "code", kakaoClientId, authUri + kakaoRedirectUri);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
