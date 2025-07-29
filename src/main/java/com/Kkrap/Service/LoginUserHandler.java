package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginUserHandler implements LoginUserPort {
    private final UsersService usersService;
    private final FoldersService foldersService;

    public LoginUserHandler(UsersService usersService, FoldersService foldersService) {
        this.usersService = usersService;
        this.foldersService = foldersService;
    }

    @Override
    public UsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId) {
        Optional<Users> CheckUser = usersService.findByKakaoId(Long.valueOf(kakaoId));
        if (CheckUser.isEmpty()){

            // 처음 로그인 한 사람 사용자 만들기
            UsersCreateRequest usersCreateRequest = UsersCreateRequest.of(email, nickname, profileImage, kakaoId, null);
            Users newUser = usersService.save(usersCreateRequest);

            // 처음 로그인 한 사람은 모든 링크 보기 폴더가 없음 만들어주어야함
            FoldersCreateRequest foldersCreateRequest = FoldersCreateRequest.of("모든 링크", "모든 링크가 저장된 폴더입니다.", false, true);
            foldersService.save(foldersCreateRequest, newUser);
            UsersProfileResponse usersProfileResponse = UsersProfileResponse.of(newUser.getUserId(), newUser.getEmail(), newUser.getNickname(), newUser.getProfile(), newUser.getKakaoId(), newUser.getBio());
            return usersProfileResponse;
        }
        else
        {
            Users existingUser = CheckUser.get();
            UsersProfileResponse usersProfileResponse = UsersProfileResponse.of(existingUser.getUserId(), existingUser.getEmail(), existingUser.getNickname(), existingUser.getProfile(), existingUser.getKakaoId(), existingUser.getBio());
            return usersProfileResponse;
        }
    }
}
