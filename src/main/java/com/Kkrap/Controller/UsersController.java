package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.ProfileUpdateRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    //users 프로필 조회
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 프로필 조회", description = "사용자의 프로필 조회")
    @ResponseBody
    public ResponseEntity<UsersProfileResponse> getUserProfile(
            @Parameter(name = "userId", description = "수정할 사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId)
    {
        return usersService.getUserProfile(userId);
    }

    //닉네임 변경
    @PatchMapping("/{userId}/profile")
    @Operation(summary = "사용자 프로필 닉네임, 소개 변경", description = "사용자의 프로필의 닉네임과 소개를 수정")
    @ResponseBody
    public ResponseEntity<UsersProfileResponse> updateProfile(
            @Parameter(name = "userId", description = "수정할 사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId,
            @RequestBody ProfileUpdateRequest request)
    {
        return usersService.updateProfile(userId, request.getNickname(), request.getBio());
    }

    //닉네임 중복확인
    @GetMapping("/check-nickname")
    @Operation(summary = "사용자 닉네임 중복확인", description = "사용자의 닉네임의 중복을 확인")
    @ResponseBody
    public ResponseEntity<MessageResponse> isNicknameDuplicate(
            @Parameter(name = "nickname", description = "닉네임만 던져주면 됩니다.", required = true, example = "째유니")
            @RequestParam String nickname){
        return usersService.isNicknameDuplicate(nickname);
    }

    //프로필 사진 변경
    @PostMapping(path = "/{userId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 업로드", description = "사용자의 프로필 이미지를 업로드")
    public ResponseEntity<UsersProfileResponse> uploadProfileImage(@PathVariable Long userId,
                                                                   @RequestPart("file") @Parameter(
                                                                           description = "업로드할 이미지 파일",
                                                                           required = true,
                                                                           content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
                                                                   ) MultipartFile file){
        return usersService.uploadProfileImage(userId, file);
    }

}
