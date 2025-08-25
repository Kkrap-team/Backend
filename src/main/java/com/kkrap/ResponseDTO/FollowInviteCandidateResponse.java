package com.kkrap.ResponseDTO;

import com.kkrap.Entity.Follows;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowInviteCandidateResponse {
    private Long followingId;
    private String nickname;
    private String profile;
    private String email;
    private boolean invited;

    public static FollowInviteCandidateResponse of(Follows f, boolean invited) {
        return new FollowInviteCandidateResponse(
                f.getFollowingId(),
                f.getNickname(),
                f.getProfile(),
                f.getEmail(),
                invited
        );
    }
}
