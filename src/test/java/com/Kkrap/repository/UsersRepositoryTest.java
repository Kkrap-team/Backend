package com.Kkrap.repository;

import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    private Users createUser(String email, String nickname, String profile, Long kakaoId, String bio) {
        return Users.builder()
                .email(email)
                .nickname(nickname)
                .profile(profile)
                .kakaoId(kakaoId)
                .bio(bio)
                .build();
    }

    @Test
    @DisplayName("닉네임으로 사용자 조회가 가능하다.")
    void findByNickname() {
        Users saved = usersRepository.save(createUser("a@a.com", "아마존", "/p.png", 1L, "bio"));

        Optional<Users> found = usersRepository.findByNickname("아마존");

        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
    }

    @Test
    @DisplayName("카카오 ID로 사용자 조회가 가능하다.")
    void findByKakaoId() {
        usersRepository.save(createUser("b@a.com", "사용자", "/p2.png", 123L, ""));

        Optional<Users> found = usersRepository.findByKaKaoId(123L);
        assertThat(found).isPresent();
        assertThat(found.get().getKakaoId()).isEqualTo(123L);
    }

    @Test
    @DisplayName("닉네임 부분 일치로 다건 조회가 가능하다.")
    void findByNicknameContaining() {
        usersRepository.save(createUser("c@a.com", "홍길동", "/p3.png", 10L, ""));
        usersRepository.save(createUser("d@a.com", "홍길자", "/p4.png", 11L, ""));
        usersRepository.save(createUser("e@a.com", "이몽룡", "/p5.png", 12L, ""));

        List<Users> results = usersRepository.findByNicknameContaining("홍");

        assertThat(results).hasSize(2);
        assertThat(results).extracting(Users::getNickname).containsExactlyInAnyOrder("홍길동", "홍길자");
    }
}