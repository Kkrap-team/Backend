package com.kkrap.ResponseDTO;


import com.kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchWithFollowResponse {
    private Long userId;
    private String nickname;
    private String profile;
    private String email;
    private boolean following;

    public static UserSearchWithFollowResponse of(Users user, boolean invited) {
        return new UserSearchWithFollowResponse(
                user.getUserId(),
                user.getNickname(),
                user.getProfile(),
                user.getEmail(),
                invited
        );
    }

}
