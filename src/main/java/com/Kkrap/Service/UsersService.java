package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.DuplicateNickNameException;
import com.Kkrap.Exception.ErrorCode;
import com.Kkrap.Exception.SaveFileErrorException;
import com.Kkrap.Exception.UsersNotFoundException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;


    private static final String UPLOAD_DIR = "/profile/";

    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId){
        Users users = findById(userId);
        return ResponseEntity.ok(UsersProfileResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId(), users.getBio()));
    }

    public ResponseEntity<UsersProfileResponse> updateProfile(Long userId, String newNickname, String bio){
        findByNickname(newNickname);
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
        String savedFileName = storeProfileImage(file, UPLOAD_DIR);

        // DB에 접근 URL 경로만 저장
        user.setProfile("/profile/" + savedFileName);
        Users users = save(user);

        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    //사진 저장 -> 파일 저장 로직 분리
    private String storeProfileImage(MultipartFile file, String baseDir) {
        try {
            //MultipartFile 객체에서 원래 업로드한 파일 이름을 가져옴 null이면 예외처리
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw SaveFileErrorException.of("파일 이름 없음", ErrorCode.SAVE_FILE_ERROR);
            }

            //파일 확장자만 추출
            //확장자가 없는 파일은 예외 발생 가능
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex != -1 && dotIndex != originalFilename.length() - 1) {
                // 점(.)이 존재하고, 파일 이름 끝이 아닌 경우 → 확장자 추출
                extension = originalFilename.substring(dotIndex);
            } else {
                // 확장자가 없는 경우 예외 발생
                throw SaveFileErrorException.of("확장자가 없는 파일입니다.", ErrorCode.SAVE_FILE_ERROR);
            }

            //이름이 중복되지 않도록 UUID를 사용 또한 실제 파일명을 저장하면 URL 추측 가능
            String savedFileName = UUID.randomUUID() + extension;

            //저장할 폴더가 실제로 존재하는지 확인하고 없다면 생성
            File directory = new File(baseDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 최종 저장 파일 경로 생성
            File dest = new File(baseDir + savedFileName);
            // 실제로 파일을 해당 경로에 저장하는 명령
            file.transferTo(dest);
            return savedFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw SaveFileErrorException.of("파일 저장 실패", ErrorCode.SAVE_FILE_ERROR);
        }
    }

    public Users findById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() ->UsersNotFoundException.of("해당 사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));
    }

    public void findByNickname(String nickname){
        if(usersRepository.findByNickname(nickname).isPresent()){
            throw DuplicateNickNameException.of("중복되는 닉네임이 있습니다.", ErrorCode.DUPLICATE_NICKNAME);
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

}
