package com.Kkrap.Auth;

import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.Service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthServiceTest {

    @Autowired AuthService authService;


    // 연동 로그인 종류에 맞춰서 확장성을 고려한 추상화가 필요할 수 있음

    @Test
    void isAccessToken() {
        String id = "hello123";
        String emptyToken = "";
        String validToken = Base64.getEncoder().encodeToString(id.getBytes());
        String nullToken = null;
        System.out.println(validToken);

        assertThrows(NotValidTokenException.class, () -> {
            authService.isAccessToken(emptyToken);
        });

        var result = authService.isAccessToken(validToken);
        var result2 = authService.isAccessToken(nullToken);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(false);
        Assertions.assertThat(result2).isNotNull();
    }

}
