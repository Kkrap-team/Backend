package com.Kkrap.ResponseDTO;


import com.Kkrap.Entity.Follows;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowsResponse {
    //나 자신
    private Long followerId;

    // 팔로우를 거는 사람 (나 자신)
    private Long followingId;

    private String nickname;
    private String profile;

    private FollowsResponse(){}

    public static FollowsResponse of(Follows follows){
        return new FollowsResponse(follows.getFollwerId(), follows.getFollowingId(), follows.getNickname(), follows.getProfile());
    }
}
