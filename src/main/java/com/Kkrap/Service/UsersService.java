package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.DuplicateNickNameException;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId){
        Users users = findById(userId);
        return ResponseEntity.ok(UsersProfileResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId(), users.getBio()));
    }

    public ResponseEntity<UsersProfileResponse> updateProfile(Long userId, String newNickname, String bio){
        Users users = findById(userId);
        users.setNickname(newNickname);
        users.setBio(bio);
        save(users);
        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    //닉네임 중복확인
    public ResponseEntity<MessageResponse> isNicknameDuplicate(String nickname){
        findByNickname(nickname);
        return ResponseEntity.ok(MessageResponse.of(200, "사용 가능한 닉네임입니다."));
    }

    //사진만 업로드
    public ResponseEntity<UsersProfileResponse> uploadProfileImage(Long userId, MultipartFile file){
        Users user = findById(userId);
        // 파일 저장
        String savedFileName = FileStorageUtil.storeProfileImage(file);
        // DB에 접근 URL 경로만 저장
        user.setProfile("/profile/" + savedFileName);
        Users users = save(user);

        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    public Users findById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() ->UsersNotFoundException.from("해당 사용자를 찾을 수 없습니다."));
    }

    public void findByNickname(String nickname){
        if(usersRepository.findByNickname(nickname).isPresent()){
            throw DuplicateNickNameException.from("중복되는 닉네임이 있습니다.");
        }
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

}
