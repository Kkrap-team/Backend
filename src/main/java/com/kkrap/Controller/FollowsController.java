package com.kkrap.Controller;


import com.kkrap.Controller.Spec.FollowsAPISpec;
import com.kkrap.RequestDTO.FollowsRequest;
import com.kkrap.ResponseDTO.FollowsResponse;
import com.kkrap.ResponseDTO.MutualFollowResponse;
import com.kkrap.ResponseDTO.UserSearchWithFollowResponse;
import com.kkrap.Service.FollowsFoldersPermission.FollowsManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Timed(value = "http.controller", extraTags = {"controller","Follows"})
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
    public ResponseEntity<List<UserSearchWithFollowResponse>> searchUsersByNicknameContains(Authentication authentication, String nickname) {
        Long userId = Long.parseLong(authentication.getName());
        return followsManagerService.findUsersByNicknameContains(nickname, userId);
    }

    @Override
    public ResponseEntity<MutualFollowResponse> checkMutualFollow(Authentication authentication, Long targetUserId) {
        Long userId = Long.parseLong(authentication.getName());
        return followsManagerService.checkFollow(userId, targetUserId);
    }
}
