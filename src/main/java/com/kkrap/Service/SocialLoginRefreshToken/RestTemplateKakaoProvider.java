package com.kkrap.Service.SocialLoginRefreshToken;

import com.kkrap.Exception.NotValidTokenException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestTemplateKakaoProvider implements ClientProvider {

    public static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public Map<String, Object> getClient(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = new RestTemplate().exchange(
                    KAKAO_USER_INFO_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw NotValidTokenException.from("유효하지 않은 카카오 액세스 토큰입니다.");
        }
    }
}
