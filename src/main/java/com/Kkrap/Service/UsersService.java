package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.ErrorCode;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UserProfileDefaultFolderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public ResponseEntity<Object> getUserProfile(Long userId){
        Users users = get(userId);
        if (users == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.of(404,"사용자를 찾을 수 없음"));
        }
        return ResponseEntity.ok(UserProfileDefaultFolderResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId()));
    }

    public boolean updateNickName(Long userId, String newNickname){
        Optional<Users> optionalUsers = usersRepository.findById(userId);

        if (optionalUsers.isPresent()){
            Users user = optionalUsers.get();
            user.setNickname(newNickname);
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    //DB에서 카카오으로 로그인한 유저 조회
    public boolean isKakaoUserExists(Long kakaoId) {
        return usersRepository.findByKaKaoId(kakaoId).isPresent();
    }

    public Users save(UsersCreateRequest usersCreateRequest){
        Users users = Users.from(usersCreateRequest);
        usersRepository.save(users);
        return users;
    }

    public Users get(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new UsersNotFoundException("해당 사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));
    }

}
