package com.kkrap.Service.Users;

import com.kkrap.Entity.Users;
import com.kkrap.Exception.DuplicateNickNameException;
import com.kkrap.Exception.UsersNotFoundException;
import com.kkrap.Repository.UsersRepository;
import com.kkrap.RequestDTO.UsersCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Users findById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() ->UsersNotFoundException.from("해당 사용자를 찾을 수 없습니다."));
    }

    public void findByNickname(String nickname){
        Optional<Users> user = usersRepository.findByNickname(nickname);
        user.ifPresent(u -> {
            throw DuplicateNickNameException.from("중복되는 닉네임이 있습니다.");
        });
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

    public Optional<Users> findByKakaoId(Long id) {
        return usersRepository.findByKaKaoId(id);
    }

    public List<Users> findByNicknameContaining(String nickname){
        return usersRepository.findByNicknameContaining(nickname);
    }

}
