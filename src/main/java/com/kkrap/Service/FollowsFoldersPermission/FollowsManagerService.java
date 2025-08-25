package com.kkrap.Service.FollowsFoldersPermission;

import com.kkrap.Entity.Follows;
import com.kkrap.Entity.Users;
import com.kkrap.ResponseDTO.FollowsResponse;
import com.kkrap.ResponseDTO.UserSearchWithFollowResponse;
import com.kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FollowsManagerService {

    private final FollowsService followsService;
    private final UsersService usersService;

    public FollowsManagerService(FollowsService followsService, UsersService usersService){
        this.followsService = followsService;
        this.usersService = usersService;
    }

    public ResponseEntity<List<FollowsResponse>> getFollowingList(Long followerId){
        Users users = usersService.findById(followerId);
        List<Follows> response = followsService.findByFollower(users);
        List<FollowsResponse> responseList = response.stream()
                .map(FollowsResponse::of)
                .toList();
        return ResponseEntity.ok(responseList);
    }


    public ResponseEntity<FollowsResponse> followUser(Long followerId, Long followingId){
        Users follower = usersService.findById(followerId);
        Users following = usersService.findById(followingId);
        return ResponseEntity.ok(FollowsResponse.of(followsService.save(follower, following)));
    }

    public ResponseEntity<FollowsResponse> unFollowUser(Long followerId, Long followingId){
        Users follower = usersService.findById(followerId);
        Users following = usersService.findById(followingId);

        Follows response = followsService.delete(follower, following);
        return ResponseEntity.ok(FollowsResponse.of(response));
    }

    //팔로우 기능 닉네임 조회
    public ResponseEntity<List<UserSearchWithFollowResponse>> findUsersByNicknameContains(String nickname, Long userId) {

        Set<Long> followingSet = new HashSet<>(followsService.findFollowingIdsByFollowerId(userId));
        List<Users> users = usersService.findByNicknameContaining(nickname);

        List<UserSearchWithFollowResponse> responseList = users.stream()
                .filter(u -> !u.getUserId().equals(userId))
                .map(u -> UserSearchWithFollowResponse.of(u, followingSet.contains(u.getUserId())))
                .toList();

        return ResponseEntity.ok(responseList);
    }



}
