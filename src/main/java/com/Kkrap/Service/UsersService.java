package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.ResponseDto.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public UserProfileResponse getUserProfile(Long userId){
        Optional<Users> optionalUsers = usersRepository.findById(userId);

        if (optionalUsers.isPresent()){
            Users user = optionalUsers.get();
            return new UserProfileResponse(user.getUserId(),user.getEmail(), user.getNickname(), user.getProfile(), user.getKakaoId());
        }
        else {
            return  null;
        }
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
}
