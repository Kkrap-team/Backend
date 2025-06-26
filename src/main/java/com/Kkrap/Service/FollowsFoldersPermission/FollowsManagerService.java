package com.Kkrap.Service.FollowsFoldersPermission;

import com.Kkrap.Entity.Follows;
import com.Kkrap.Entity.Users;
import com.Kkrap.ResponseDto.FollowsResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<UsersProfileResponse>> findUsersByNicknameContains(String nickname) {
        List<Users> users = usersService.findByNicknameContaining(nickname);
        List<UsersProfileResponse> responseList = users.stream()
                .map(UsersProfileResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }



}
