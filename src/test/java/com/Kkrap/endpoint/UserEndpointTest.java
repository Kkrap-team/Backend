package com.Kkrap.endpoint;

import autoparams.AutoSource;
import com.Kkrap.Controller.UsersController;
import com.Kkrap.Exception.DuplicateNickNameException;
import com.Kkrap.Exception.GlobalExceptionHandler;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.RequestDTO.ProfileUpdateRequest;
import com.Kkrap.ResponseDTO.MessageResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.Users.UsersManagerService;
import com.Kkrap.Service.Users.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UsersController.class)
@Import(GlobalExceptionHandler.class)
public class UserEndpointTest {
    @Autowired private MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean UsersManagerService usersManagerService;

    // 닉네임 중복확인 테스트들
    @Test
    @DisplayName("중복된 닉네임으로 중복확인을 요청하면, 409 에러가 발생한다.")
    void checkDuplicateNickname() throws Exception {
        // given
        String duplicateNickname = "중복닉네임";
        when(usersManagerService.checkNicknameAvailable(duplicateNickname))
                .thenThrow(DuplicateNickNameException.from("중복되는 닉네임이 있습니다."));

        // when & then
        mockMvc.perform(get("/users/check-nickname")
                .param("nickname", duplicateNickname)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("중복되는 닉네임이 있습니다."));
    }

    @Test
    @DisplayName("사용 가능한 닉네임으로 중복확인을 요청하면, 성공 메시지를 반환한다.")
    void checkAvailableNickname() throws Exception {
        // given
        String availableNickname = "사용가능닉네임";
        MessageResponse successResponse = MessageResponse.of(200, "사용 가능한 닉네임입니다.");
        when(usersManagerService.checkNicknameAvailable(availableNickname))
                .thenReturn(ResponseEntity.ok(successResponse));

        // when & then
        mockMvc.perform(get("/users/check-nickname")
                .param("nickname", availableNickname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("사용 가능한 닉네임입니다."));
    }

    // 사용자 프로필 조회 테스트들
    @Test
    @DisplayName("존재하는 사용자 ID로 프로필을 조회하면, 사용자 정보를 반환한다.")
    void getUserProfileSuccess() throws Exception {
        // given
        Long userId = 1L;
        UsersProfileResponse userProfile = UsersProfileResponse.of(
                userId, "test@example.com", "테스트유저", "profile.jpg", 12345L, "안녕하세요"
        );
        when(usersManagerService.getUserProfile(userId))
                .thenReturn(ResponseEntity.ok(userProfile));

        // when & then
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"))
                .andExpect(jsonPath("$.profile").value("profile.jpg"))
                .andExpect(jsonPath("$.bio").value("안녕하세요"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 프로필을 조회하면, 404 에러가 발생한다.")
    void getUserProfileNotFound() throws Exception {
        // given
        Long nonExistentUserId = 999L;
        when(usersManagerService.getUserProfile(nonExistentUserId))
                .thenThrow(UsersNotFoundException.from("해당 사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/users/{userId}", nonExistentUserId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("해당 사용자를 찾을 수 없습니다."));
    }

    // 프로필 업데이트 테스트들
    @Test
    @DisplayName("유효한 데이터로 프로필을 수정하면, 수정된 프로필 정보를 반환한다.")
    void updateUserProfileSuccess() throws Exception {
        // given
        Long userId = 1L;
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();
        updateRequest.setNickname("새로운닉네임");
        updateRequest.setBio("새로운 소개");

        UsersProfileResponse updatedProfile = UsersProfileResponse.of(
                userId, "test@example.com", "새로운닉네임", "profile.jpg", 12345L, "새로운 소개"
        );

        when(usersManagerService.updateUserProfile(userId, "새로운닉네임", "새로운 소개"))
                .thenReturn(ResponseEntity.ok(updatedProfile));

        String requestContent = objectMapper.writeValueAsString(updateRequest);

        // when & then
        mockMvc.perform(patch("/users/{userId}/profile", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("새로운닉네임"))
                .andExpect(jsonPath("$.bio").value("새로운 소개"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 프로필을 수정하려 하면, 404 에러가 발생한다.")
    void updateUserProfileNotFound() throws Exception {
        // given
        Long nonExistentUserId = 999L;
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();
        updateRequest.setNickname("새로운닉네임");
        updateRequest.setBio("새로운 소개");

        when(usersManagerService.updateUserProfile(anyLong(), anyString(), anyString()))
                .thenThrow(UsersNotFoundException.from("해당 사용자를 찾을 수 없습니다."));

        String requestContent = objectMapper.writeValueAsString(updateRequest);

        // when & then
        mockMvc.perform(patch("/users/{userId}/profile", nonExistentUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 사용자를 찾을 수 없습니다."));
    }

    // 프로필 이미지 업로드 테스트들
    @Test
    @DisplayName("유효한 이미지 파일을 업로드하면, 업데이트된 프로필 정보를 반환한다.")
    void uploadProfileImageSuccess() throws Exception {
        // given
        Long userId = 1L;
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test-image.jpg", "image/jpeg", "test image content".getBytes()
        );

        UsersProfileResponse updatedProfile = UsersProfileResponse.of(
                userId, "test@example.com", "테스트유저", "/profile/new-image.jpg", 12345L, "안녕하세요"
        );

        when(usersManagerService.uploadUserProfileImage(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(updatedProfile));

        // when & then
        mockMvc.perform(multipart("/users/{userId}/profile-image", userId)
                .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile").value("/profile/new-image.jpg"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 이미지를 업로드하려 하면, 404 에러가 발생한다.")
    void uploadProfileImageUserNotFound() throws Exception {
        // given
        Long nonExistentUserId = 999L;
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test-image.jpg", "image/jpeg", "test image content".getBytes()
        );

        when(usersManagerService.uploadUserProfileImage(anyLong(), any()))
                .thenThrow(UsersNotFoundException.from("해당 사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(multipart("/users/{userId}/profile-image", nonExistentUserId)
                .file(imageFile))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 사용자를 찾을 수 없습니다."));
    }

    // 파라미터화된 테스트
    @AutoSource
    @ParameterizedTest
    @DisplayName("다양한 닉네임으로 중복확인을 요청한다.")
    void checkNicknameWithVariousInputs(String nickname) throws Exception {
        // given
        MessageResponse successResponse = MessageResponse.of(200, "사용 가능한 닉네임입니다.");
        when(usersManagerService.checkNicknameAvailable(anyString()))
                .thenReturn(ResponseEntity.ok(successResponse));

        // when & then
        mockMvc.perform(get("/users/check-nickname")
                .param("nickname", nickname))
                .andExpect(status().isOk());

        System.out.println("테스트 닉네임: " + nickname);
    }

    // 경계값 테스트들
    @Test
    @DisplayName("빈 문자열 닉네임으로 중복확인을 요청하면, 적절한 에러가 발생한다.")
    void checkEmptyNickname() throws Exception {
        // given
        String emptyNickname = "";
        when(usersManagerService.checkNicknameAvailable(emptyNickname))
                .thenThrow(new IllegalArgumentException("닉네임은 필수입니다."));

        // when & then
        mockMvc.perform(get("/users/check-nickname")
                .param("nickname", emptyNickname))
                .andExpect(status().is4xxClientError());
    }

}
