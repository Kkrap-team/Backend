package com.Kkrap.Controller;


import com.Kkrap.Controller.Spec.FollowsAPISpec;
import com.Kkrap.RequestDTO.FollowsRequest;
import com.Kkrap.ResponseDTO.FollowsResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FollowsFoldersPermission.FollowsManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowsController implements FollowsAPISpec {

    private final FollowsManagerService followsManagerService;

    public FollowsController(FollowsManagerService followsManagerService){
        this.followsManagerService = followsManagerService;
    }

    @Override
    public ResponseEntity<List<FollowsResponse>> getFollowingList(Authentication authentication) {
        Long followerId = Long.parseLong(authentication.getName());
        return followsManagerService.getFollowingList(followerId);
    }

    @Override
    public ResponseEntity<FollowsResponse> followUser(Authentication authentication, FollowsRequest request) {
        Long followerId = Long.parseLong(authentication.getName());
        return followsManagerService.followUser(followerId, request.getFollowingId());
    }

    @Override
    public ResponseEntity<FollowsResponse> unFollowUser(Authentication authentication, FollowsRequest request) {
        Long followerId = Long.parseLong(authentication.getName());
        return followsManagerService.unFollowUser(followerId, request.getFollowingId());
    }

    @Override
    public ResponseEntity<List<UsersProfileResponse>> searchUsersByNicknameContains(Authentication authentication, String nickname) {
        return followsManagerService.findUsersByNicknameContains(nickname);
    }



}
