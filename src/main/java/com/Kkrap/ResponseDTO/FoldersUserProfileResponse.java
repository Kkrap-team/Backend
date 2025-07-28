package com.Kkrap.ResponseDTO;

import com.Kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoldersUserProfileResponse {
    private Long userId;
    private String nickname;
    private String profile;
    private String bio;

    private Long totalViewCount;
    private Long followingCount;

    public static FoldersUserProfileResponse of(
            Users user,
            Long totalViewCount,
            Long followingCount
    ) {
        return new FoldersUserProfileResponse(
                user.getUserId(),
                user.getNickname(),
                user.getProfile(),
                user.getBio(),
                totalViewCount,
                followingCount
                );
    }
}
