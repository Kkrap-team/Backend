package com.Kkrap.dto;

import com.Kkrap.Entity.Users;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsersDtoTest {

    @Test
    @DisplayName("UsersProfileResponse.of 로 생성된 값이 기대값과 일치한다.")
    void ofFactory() {
        UsersProfileResponse res = UsersProfileResponse.of(1L, "e@a.com", "닉", "/p.png", 7L, "bio");

        assertThat(res.getUserId()).isEqualTo(1L);
        assertThat(res.getEmail()).isEqualTo("e@a.com");
        assertThat(res.getNickname()).isEqualTo("닉");
        assertThat(res.getProfile()).isEqualTo("/p.png");
        assertThat(res.getKakaoId()).isEqualTo(7L);
        assertThat(res.getBio()).isEqualTo("bio");
    }

    @Test
    @DisplayName("UsersProfileResponse.from(Users) 로 매핑된다.")
    void fromUsers() {
        Users user = Users.builder()
                .email("e@a.com")
                .nickname("닉")
                .profile("/p.png")
                .kakaoId(7L)
                .bio("bio")
                .build();
        // userId는 null이어도 from이 동작해야 함

        UsersProfileResponse res = UsersProfileResponse.from(user);

        assertThat(res.getEmail()).isEqualTo("e@a.com");
        assertThat(res.getKakaoId()).isEqualTo(7L);
    }

    @Test
    @DisplayName("UsersProfileResponse 직렬화 시 JSON 필드가 포함된다.")
    void jsonSerialization() throws JsonProcessingException {
        UsersProfileResponse res = UsersProfileResponse.of(2L, "e@a.com", "닉", "/p.png", 7L, "bio");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(res);

        assertThat(json).contains("\"userId\":2");
        assertThat(json).contains("\"email\":\"e@a.com\"");
        assertThat(json).contains("\"nickname\":\"닉\"");
        assertThat(json).contains("\"profile\":\"/p.png\"");
        assertThat(json).contains("\"kakaoId\":7");
        assertThat(json).contains("\"bio\":\"bio\"");
    }
}


