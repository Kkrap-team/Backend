package com.Kkrap.service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.Service.Users.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock private UsersRepository usersRepository;
    @InjectMocks private UsersService usersService;

    @Test
    @DisplayName("ID로 사용자를 조회할 수 있다.")
    void findById_success() {
        Users user = Users.builder()
                .email("e@a.com").nickname("닉").profile("/p.png").kakaoId(1L).bio("")
                .build();
        given(usersRepository.findById(1L)).willReturn(Optional.of(user));

        Users found = usersService.findById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("e@a.com");
    }

    @Test
    @DisplayName("없는 ID로 조회하면 UsersNotFoundException 발생")
    void findById_notFound() {
        given(usersRepository.findById(999L)).willReturn(Optional.empty());

        assertThrows(UsersNotFoundException.class, () -> usersService.findById(999L));
    }

    @Test
    @DisplayName("요청 DTO로 사용자 저장이 가능하다.")
    void save_fromRequest() {
        UsersCreateRequest req = UsersCreateRequest.of("e@a.com", "닉", "/p.png", 1L, "bio");
        given(usersRepository.save(any(Users.class))).willAnswer(invocation -> invocation.getArgument(0));

        Users saved = usersService.save(req);

        assertThat(saved.getEmail()).isEqualTo("e@a.com");
        verify(usersRepository).save(any(Users.class));
    }
}


