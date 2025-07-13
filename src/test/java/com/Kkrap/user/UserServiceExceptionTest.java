package com.Kkrap.user;

import autoparams.AutoSource;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.DuplicateNickNameException;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.Service.Users.UsersService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceExceptionTest {
    @Mock private UsersRepository usersRepository;
    @InjectMocks private UsersService usersService;

    @AutoSource
    @ParameterizedTest
    @DisplayName("찾을 수 없는 사용자의 Id를 부여하면, UsersNotFoundException이 발생한다.")
    void findById() {
        Long id = 9999998L; // 찾을 수 없는 사용자의 id는 어떻게 부여할까?

        Assertions.assertThrows(UsersNotFoundException.class, () -> usersService.findById(id));
    }

    @AutoSource
    @ParameterizedTest
    @Transactional
    @DisplayName("중복되는 닉네임을 부여하면, DuplicateNickNameException이 발생한다.")
    void findByNickname() {
        UsersCreateRequest usersCreateRequest = UsersCreateRequest.of("email", "amazon", "", 1L, "");
        Users users = Users.from(usersCreateRequest);

        given(usersRepository.save(any()))
                .willThrow(new DuplicateNickNameException());

        UsersService usersService1 = new UsersService(usersRepository);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> usersService.findByNickname(usersCreateRequest.getNickname()))
                .isInstanceOf(DuplicateNickNameException.class);
    }
}
