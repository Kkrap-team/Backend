package com.Kkrap.Controller;


import com.Kkrap.RequestDTO.FollowsRequest;
import com.Kkrap.ResponseDTO.FollowsResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FollowsFoldersPermission.FollowsManagerService;
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

    //현재 팔로우 중인 리스트 조회 -> 권한 부여를 위해 필요함
    @GetMapping("/{userId}/following")
    @Operation(summary = "팔로우 조회", description = "팔로우 조회")
    public ResponseEntity<List<FollowsResponse>> getFollowingList(
            @PathVariable("userId") Long followerId) {
        return followsManagerService.getFollowingList(followerId);
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
