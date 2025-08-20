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
    private List<FollowInviteCandidateResponse> allCandidates;
    private List<FollowInviteCandidateResponse> invited;
    private List<FollowInviteCandidateResponse> notInvited;
    private boolean hasPermission;

    private FollowInviteListResponse(){}

    public static FollowInviteListResponse of(UsersProfileResponse usersProfileResponse, List<FollowInviteCandidateResponse> followInviteCandidateResponses,
                                              List<FollowInviteCandidateResponse> invited, List<FollowInviteCandidateResponse> notInvited, boolean hasPermission){
        return new FollowInviteListResponse(usersProfileResponse, followInviteCandidateResponses, invited, notInvited, hasPermission);
    }

}