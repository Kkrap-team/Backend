package com.Kkrap.Controller.Spec;


import com.Kkrap.RequestDTO.ProfileUpdateRequest;
import com.Kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.Kkrap.ResponseDTO.MessageResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Users", description = "사용자 관리 API Endpoint")
public interface UsersAPISpec {

    //users 프로필 조회
    @GetMapping("/me")
    @Operation(summary = "내 프로필 조회", description = "JWT Access Token으로 현재 로그인한 사용자의 프로필을 조회합니다.")
    ResponseEntity<UsersProfileResponse> getUserProfileToken(Authentication authentication);

    @GetMapping("/folders/{userId}")
    @Operation(summary = "내 폴더 전용 사용자 프로필 조회", description = "내 폴더 전용 사용자의 프로필 조회")
    ResponseEntity<FoldersUserProfileResponse> getFoldersUserProfile(Authentication authentication);

    //닉네임 변경
    @PatchMapping("/profile")
    @Operation(summary = "사용자 프로필 닉네임, 소개 변경", description = "사용자의 프로필의 닉네임과 소개를 수정")
    ResponseEntity<UsersProfileResponse> updateUserProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequest request);


//    //프로필 사진 변경
//    @PostMapping(path = "/{userId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(summary = "프로필 이미지 업로드", description = "사용자의 프로필 이미지를 업로드")
//    ResponseEntity<UsersProfileResponse> uploadUserProfileImage(
//            @PathVariable("userId") Long userId,
//            @RequestPart("file") @Parameter(
//                    description = "업로드할 이미지 파일",
//                    required = true,
//                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
//            ) MultipartFile file);

    //닉네임 중복확인
    @GetMapping("/check-nickname")
    @Operation(summary = "사용자 닉네임 중복확인", description = "사용자의 닉네임의 중복을 확인")
    ResponseEntity<MessageResponse> isNicknameDuplicate(
            Authentication authentication,
            @Parameter(name = "nickname", description = "닉네임", required = true, example = "째유니")
            @RequestParam("nickname") String nickname);
}
