package com.Kkrap.Util;

import com.Kkrap.Exception.ErrorCode;
import com.Kkrap.Exception.SaveFileErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileStorageUtil {
    private static final String UPLOAD_DIR = "/profile/";

    //사진 저장 -> 파일 저장 로직 분리
    public static String storeProfileImage(MultipartFile file) {
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
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 최종 저장 파일 경로 생성
            File dest = new File(UPLOAD_DIR + savedFileName);
            // 실제로 파일을 해당 경로에 저장하는 명령
            file.transferTo(dest);
            return savedFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw SaveFileErrorException.of("파일 저장 실패", ErrorCode.SAVE_FILE_ERROR);
        }
    }
}
