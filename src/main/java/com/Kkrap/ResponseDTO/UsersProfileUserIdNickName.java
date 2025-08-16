package com.Kkrap.ResponseDTO;

import com.Kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsersProfileUserIdNickName {
    private Long userId;

    private String nickname;

    private String profile;

    private UsersProfileUserIdNickName(){ } // 외부에서 생성하지 못하게 함

    public static UsersProfileUserIdNickName of(Long userId, String nickname, String profile){
        return new UsersProfileUserIdNickName(userId, nickname, profile);
    }

    public static UsersProfileUserIdNickName from(Users users){
        return new UsersProfileUserIdNickName(users.getUserId(), users.getNickname(), users.getProfile());
    }
}
