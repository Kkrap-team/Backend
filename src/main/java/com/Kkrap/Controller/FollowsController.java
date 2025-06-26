package com.Kkrap.Controller;


import com.Kkrap.RequestDTO.FollowsRequest;
import com.Kkrap.ResponseDto.FollowsResponse;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Service.Follows.FollowsManagerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowsController {

    private final FollowsManagerService followsManagerService;

    public FollowsController(FollowsManagerService followsManagerService){
        this.followsManagerService = followsManagerService;
    }

    //사용자가 팔로우 진행
    @PostMapping("/{userId}/follow")
    @Operation(summary = "팔로우 하기", description = "상대방을 팔로우")
    public ResponseEntity<FollowsResponse> followUser(
            @PathVariable("userId") Long followerId,
            @RequestBody FollowsRequest request) {
        return followsManagerService.followUser(followerId, request.getFollowingId());
    }

    //사용자 팔로우 삭제
    @DeleteMapping("/{userId}/unfollow")
    @Operation(summary = "팔로우 취소", description = "해당 사용자의 팔로우를 취소")
    public ResponseEntity<FollowsResponse> unFollowUser(
            @PathVariable("userId") Long followerId,
            @RequestBody FollowsRequest request) {
        return followsManagerService.unFollowUser(followerId, request.getFollowingId());
    }

    //팔로우 기능을 위한 사용자 조회
    @GetMapping("/search")
    @Operation(summary = "닉네임 포함 사용자 검색", description = "입력한 닉네임 문자열이 포함된 사용자들을 조회합니다.")
    public ResponseEntity<List<UsersProfileResponse>> searchUsersByNicknameContains(
            @RequestParam String nickname) {
        return followsManagerService.findUsersByNicknameContains(nickname);
    }



}
