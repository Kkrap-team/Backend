package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.UsersAPISpec;
import com.Kkrap.RequestDTO.ProfileUpdateRequest;
import com.Kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.Kkrap.ResponseDTO.MessageResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.Users.UsersManagerService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId) {
        return usersManagerService.getUserProfile(userId);
    }

    @Override
    public ResponseEntity<UsersProfileResponse> getUserProfileToken(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return usersManagerService.getUserProfile(userId);
    }

    @Override
    public ResponseEntity<FoldersUserProfileResponse> getFoldersUserProfile(Long userId) {
        return usersManagerService.getFoldersUserProfile(userId);
    }

    @Override
    public ResponseEntity<UsersProfileResponse> updateUserProfile(Long userId, ProfileUpdateRequest request) {
        return usersManagerService.updateUserProfile(userId, request.getNickname(), request.getBio());
    }

    @Override
    public ResponseEntity<UsersProfileResponse> uploadUserProfileImage(Long userId, MultipartFile file) {
        return usersManagerService.uploadUserProfileImage(userId, file);
    }

    @Override
    public ResponseEntity<MessageResponse> isNicknameDuplicate(String nickname) {
        return usersManagerService.checkNicknameAvailable(nickname);
    }
}
