package com.Kkrap.Auth;

import autoparams.AutoSource;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Service.AuthService;
import com.Kkrap.Service.LoginUserPort;
import com.Kkrap.Service.SocialLoginRefreshToken.ClientProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

     @InjectMocks AuthService authService;
     @Mock private UsersRepository usersRepository;
     @Mock private FoldersRepository foldersRepository;
     @Mock private ClientProvider clientProvider;
     @Mock private LoginUserPort loginUserPort;

     @AutoSource
     @ParameterizedTest
     @DisplayName("카카오 콜백 응답을 받았을 경우, 사용자 프로필 데이터 폼을 반환한다.")
     void callbackResponse(KaKaoTokenRequest request, UsersProfileResponse response) {
         // given
         Map<String, Object> mockClientResponse = new HashMap<>();
         Map<String, Object> kakaoAccount = new HashMap<>();
         Map<String, Object> profile = new HashMap<>();
         kakaoAccount.put("id", 123456789L);
         kakaoAccount.put("email", "test@example.com");
         profile.put("nickname", "홍길동");
         profile.put("profile_image_url", UUID.randomUUID().toString());
         mockClientResponse.put("kakao_account", kakaoAccount);
         kakaoAccount.put("profile", profile);

         request.setAccesstoken(UUID.randomUUID().toString());
         given(clientProvider.getClient(request.getAccesstoken())).willReturn(mockClientResponse);

         //when
         Map<String, Object> result = clientProvider.getClient(request.getAccesstoken());

          //then
         Assertions.assertThat(result).isNotNull();
         Assertions.assertThat(result.get("kakao_account")).isNotNull();
     }

}
