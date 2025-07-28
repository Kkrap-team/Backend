package com.Kkrap.Controller;


import com.Kkrap.Controller.Spec.FollowsAPISpec;
import com.Kkrap.RequestDTO.FollowsRequest;
import com.Kkrap.ResponseDTO.FollowsResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FollowsFoldersPermission.FollowsManagerService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<FollowsResponse>> getFollowingList(Long followerId) {
        return followsManagerService.getFollowingList(followerId);
    }

    @Override
    public ResponseEntity<FollowsResponse> followUser(Long followerId, FollowsRequest request) {
        return followsManagerService.followUser(followerId, request.getFollowingId());
    }

    @Override
    public ResponseEntity<FollowsResponse> unFollowUser(Long followerId, FollowsRequest request) {
        return followsManagerService.unFollowUser(followerId, request.getFollowingId());
    }

    @Override
    public ResponseEntity<List<UsersProfileResponse>> searchUsersByNicknameContains(String nickname) {
        return followsManagerService.findUsersByNicknameContains(nickname);
    }



}
