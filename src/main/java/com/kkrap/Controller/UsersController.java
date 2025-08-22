package com.kkrap.Controller;

import com.kkrap.Controller.Spec.UsersAPISpec;
import com.kkrap.RequestDTO.ProfileUpdateRequest;
import com.kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.kkrap.ResponseDTO.MessageResponse;
import com.kkrap.ResponseDTO.UsersProfileResponse;
import com.kkrap.Service.Users.UsersManagerService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController implements UsersAPISpec{

    private final UsersManagerService usersManagerService;

    public UsersController(UsersManagerService usersManagerService){
        this.usersManagerService = usersManagerService;
    }


    @Override
//    @Timed(value = "http.users.getUserProfile", extraTags = {"controller","Users","endpoint","GET /users/{userId}"})
    public ResponseEntity<UsersProfileResponse> getUserProfileToken(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return usersManagerService.getUserProfile(userId);
    }

    @Override
//    @Timed(value = "http.users.getFoldersUserProfile", extraTags = {"controller","Users","endpoint","GET /users/{userId}/folders"})
    public ResponseEntity<FoldersUserProfileResponse> getFoldersUserProfile(Authentication authentication,  Long targetUserId) {
        return usersManagerService.getFoldersUserProfile(targetUserId);
    }

    @Override
//    @Timed(value = "http.users.updateUserProfile", extraTags = {"controller","Users","endpoint","PATCH /users/{userId}/profile"})
    public ResponseEntity<UsersProfileResponse> updateUserProfile(Authentication authentication, ProfileUpdateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return usersManagerService.updateUserProfile(userId, request.getNickname(), request.getBio());
    }

//     @Override
//     @Timed(value = "http.users.uploadUserProfileImage", extraTags = {"controller","Users","endpoint","POST /users/{userId}/profile-image"})
//     public ResponseEntity<UsersProfileResponse> uploadUserProfileImage(Long userId, MultipartFile file) {
//         return usersManagerService.uploadUserProfileImage(userId, file);
//     }

    @Override
//    @Timed(value = "http.users.isNicknameDuplicate", extraTags = {"controller","Users","endpoint","GET /users/check-nickname"})
    public ResponseEntity<MessageResponse> isNicknameDuplicate(Authentication authentication, String nickname) {
              return usersManagerService.checkNicknameAvailable(nickname);
    }



}
