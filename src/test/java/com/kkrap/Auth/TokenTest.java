package com.kkrap.Auth;

import com.kkrap.Exception.NotValidTokenException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenTest {

    TokenStubFunction tokenStubFunction;

    // 연동 로그인 종류에 맞춰서 확장성을 고려한 추상화가 필요할 수 있음
    @Test
    void isAccessToken() {
        String id = "hello123";
        String emptyToken = "";
        String validToken = Base64.getEncoder().encodeToString(id.getBytes());
        String nullToken = null;
        System.out.println(validToken);

        assertThrows(NotValidTokenException.class, () -> {
            tokenStubFunction.isAccessToken(emptyToken);
        });

        assertThrows(NotValidTokenException.class, () -> {
            tokenStubFunction.isAccessToken(nullToken);
        });

        var result = tokenStubFunction.isAccessToken(validToken);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(false);
    }

}
