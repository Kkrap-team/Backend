package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.ErrorCode;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId){
        Users users = get(userId);
        return ResponseEntity.ok(UsersProfileResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId()));
    }

    public ResponseEntity<UsersProfileResponse> updateNickName(Long userId, String newNickname){
        Users users = get(userId);
        users.setNickname(newNickname);
        save(users);
        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    public Users get(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() ->UsersNotFoundException.of("해당 사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));
    }

    //유저 만들기
    public Users save(UsersCreateRequest usersCreateRequest){
        Users users = Users.from(usersCreateRequest);
        usersRepository.save(users);
        return users;
    }

    //유저 이미 있는데 저장
    public Users save(Users users){
        return usersRepository.save(users);
    }

}
