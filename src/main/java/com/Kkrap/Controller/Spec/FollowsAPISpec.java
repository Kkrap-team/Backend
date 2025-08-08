package com.Kkrap.Controller.Spec;
import com.Kkrap.RequestDTO.FollowsRequest;
import com.Kkrap.ResponseDTO.FollowsResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Follows", description = "팔로우 관련 API Endpoint")
public interface FollowsAPISpec {

    //현재 팔로우 중인 리스트 조회 -> 권한 부여를 위해 필요함
    @GetMapping("/following")
    @Operation(summary = "팔로우 조회", description = "내가 팔로우하고 있는 사용자 리스트를 조회합니다.")
    ResponseEntity<List<FollowsResponse>> getFollowingList(
            Authentication authentication
    );

    //사용자가 팔로우 진행
    @PostMapping("/follow")
    @Operation(summary = "팔로우 하기", description = "특정 사용자를 팔로우합니다.")
    ResponseEntity<FollowsResponse> followUser(
            Authentication authentication,
            @RequestBody FollowsRequest request);

    //사용자 팔로우 삭제
    @DeleteMapping("/unfollow")
    @Operation(summary = "팔로우 취소", description = "특정 사용자의 팔로우를 취소합니다.")
    ResponseEntity<FollowsResponse> unFollowUser(
            Authentication authentication,
            @RequestBody FollowsRequest request);

    //팔로우 기능을 위한 사용자 조회
    @GetMapping("/search")
    @Operation(summary = "닉네임 포함 사용자 검색", description = "입력한 닉네임 문자열이 포함된 사용자들을 조회합니다.")
    ResponseEntity<List<UsersProfileResponse>> searchUsersByNicknameContains(
            Authentication authentication,
            @RequestParam("nickname") String nickname);
}
