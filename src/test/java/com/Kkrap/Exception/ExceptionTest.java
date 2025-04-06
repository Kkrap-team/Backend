package com.Kkrap.Exception;



import com.Kkrap.Fixture.Users;
import com.Kkrap.Handler.ObjectStatusHandler;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.Service.LinksService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ObjectStatusHandler.class})
public class ExceptionTest {



    @Test
    @DisplayName("예외 발생으로 반환값이 제대로 동작하는지 테스트")
    void SpecificNotFoundException_을_반환한다 () {

        assertThrows(SpecificNotFoundException.class, () -> {
            throw new SpecificNotFoundException("특정 행동에 대한 결과를 찾을 수 없음");
        });

    }

    @Test
    @DisplayName("공통된 객체가 비었으면 예외가 발생하고, 404 응답을 반환하는지 테스트")
    void 예외응답핸들러_테스트() {
        Users users = new Users(1L, "hello");


        assertThrows(SpecificNotFoundException.class, () -> {
            ObjectStatusHandler.objectStatusHandler(Optional.of(users), "사용자를 찾을 수 없음");
        });

    }
}
