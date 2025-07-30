package com.Kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@Setter
public class FollowInviteListResponse {
    private UsersProfileResponse owner;
    private List<FollowInviteCandidateResponse> candidates;

    private FollowInviteListResponse(){}

    public static FollowInviteListResponse of(UsersProfileResponse usersProfileResponse, List<FollowInviteCandidateResponse> followInviteCandidateResponses){
        return new FollowInviteListResponse(usersProfileResponse, followInviteCandidateResponses);
    }

}