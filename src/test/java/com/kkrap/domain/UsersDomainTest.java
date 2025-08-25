package com.kkrap.domain;

import com.kkrap.Entity.Users;
import com.kkrap.RequestDTO.UsersCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsersDomainTest {

    @Test
    @DisplayName("정적 팩토리 from(UsersCreateRequest)로 엔티티를 생성할 수 있다.")
    void createFromRequest() {
        UsersCreateRequest request = UsersCreateRequest.of("a@a.com", "닉", "/p.png", 99L, "bio");

        Users users = Users.from(request);

        assertThat(users.getEmail()).isEqualTo("a@a.com");
        assertThat(users.getNickname()).isEqualTo("닉");
        assertThat(users.getProfile()).isEqualTo("/p.png");
        assertThat(users.getKakaoId()).isEqualTo(99L);
        assertThat(users.getBio()).isEqualTo("bio");
    }

    @Test
    @DisplayName("@Builder로 엔티티를 생성할 수 있다.")
    void createWithBuilder() {
        Users users = Users.builder()
                .email("b@a.com")
                .nickname("빌더")
                .profile("/p2.png")
                .kakaoId(100L)
                .bio("소개")
                .build();

        assertThat(users.getEmail()).isEqualTo("b@a.com");
        assertThat(users.getNickname()).isEqualTo("빌더");
        assertThat(users.getProfile()).isEqualTo("/p2.png");
        assertThat(users.getKakaoId()).isEqualTo(100L);
        assertThat(users.getBio()).isEqualTo("소개");
    }
}


