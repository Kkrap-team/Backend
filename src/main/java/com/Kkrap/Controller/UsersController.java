package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.UsersAPISpec;
import com.Kkrap.RequestDTO.ProfileUpdateRequest;
import com.Kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.Kkrap.ResponseDTO.MessageResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.Users.UsersManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UsersController implements UsersAPISpec{

    private final UsersManagerService usersManagerService;

    public UsersController(UsersManagerService usersManagerService){
        this.usersManagerService = usersManagerService;
    }



    @Override
    @Timed(value = "http.users.getUserProfile", extraTags = {"controller","Users","endpoint","GET /users/{userId}"})
    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId) {
        return usersManagerService.getUserProfile(userId);
    }

    @Override
    @Timed(value = "http.users.getFoldersUserProfile", extraTags = {"controller","Users","endpoint","GET /users/{userId}/folders"})
    public ResponseEntity<FoldersUserProfileResponse> getFoldersUserProfile(Long userId) {
        return usersManagerService.getFoldersUserProfile(userId);
    }

    @Override
    @Timed(value = "http.users.updateUserProfile", extraTags = {"controller","Users","endpoint","PATCH /users/{userId}/profile"})
    public ResponseEntity<UsersProfileResponse> updateUserProfile(Long userId, ProfileUpdateRequest request) {
        return usersManagerService.updateUserProfile(userId, request.getNickname(), request.getBio());
    }

    @Override
    @Timed(value = "http.users.uploadUserProfileImage", extraTags = {"controller","Users","endpoint","POST /users/{userId}/profile-image"})
    public ResponseEntity<UsersProfileResponse> uploadUserProfileImage(Long userId, MultipartFile file) {
        return usersManagerService.uploadUserProfileImage(userId, file);
    }

    @Override
    @Timed(value = "http.users.isNicknameDuplicate", extraTags = {"controller","Users","endpoint","GET /users/check-nickname"})
    public ResponseEntity<MessageResponse> isNicknameDuplicate(String nickname) {
        return usersManagerService.checkNicknameAvailable(nickname);
    }
}
