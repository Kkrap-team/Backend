package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.FoldersNotFoundException;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FoldersService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private FoldersRepository foldersRepository;

    @Autowired
    private FoldersLinksService foldersLinksService;

    @Autowired
    private LinksRepository linksRepository;


    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAll(Long userId){
        //중간에 있는 사용자 인지 검사
        usersService.findById(userId);
        // userId를 통해 전부 가져오기
        List<Folders> folders = findByUserUserId(userId);
        List<FoldersLinksAllResponse> responseList = folders.stream().map(folder -> {
            // 해당 폴더의 FoldersLinks 조회 (folder_id 기준)
            // 특정 폴더(folder_id)에 해당하는 FoldersLinks를 조회
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);

            // 각 FoldersLinks에서 link_id 추출하여 Links 조회
            List<Links> linksList = folderLinksList.stream()
                    .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null))
                    .filter(Objects::nonNull) // 존재하는 Links만 리스트에 추가
                    .sorted(Comparator.comparing(Links::getCreateTime).reversed()) // 최신순 정렬
                    .collect(Collectors.toList());
            // Folder + Links 리스트를 Response DTO로 변환
            return FoldersLinksAllResponse.of(folder, linksList);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAllthumbnailUrl(Long userId){
        //중간에 있는 사용자 인지 검사
        usersService.findById(userId);
        // userId를 통해 전부 가져오기
        List<Folders> folders = findByUserUserId(userId);
        List<FoldersLinksAllResponse> responseList = folders.stream().map(folder -> {
            // 해당 폴더의 FoldersLinks 조회 (folder_id 기준)
            // 특정 폴더(folder_id)에 해당하는 FoldersLinks를 조회
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);

            // 각 FoldersLinks에서 link_id 추출하여 Links 조회
            List<Links> linksList = folderLinksList.stream()
                    .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null))
                    .filter(Objects::nonNull) // 존재하는 Links만 리스트에 추가
                    .sorted(Comparator.comparing(Links::getCreateTime).reversed()) // 최신순 정렬
                    .limit(4) // 상위 4개만 추출
                    .collect(Collectors.toList());
            // Folder + Links 리스트를 Response DTO로 변환
            return FoldersLinksAllResponse.of(folder, linksList);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }


    public ResponseEntity<FoldersLinksAllResponse> getOneFolderLinksAll(Long folderId){
        //폴더가 있는지 검사
        Folders folder = findById(folderId);

        //해당 폴더의 FoldersLinks 조회
        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
        List<Links> linksList = folderLinksList.stream()
                .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null))
                .filter(Objects::nonNull) // 존재하는 Links만 리스트에 추가
                .sorted(Comparator.comparing(Links::getCreateTime).reversed()) // 최신순 정렬
                .collect(Collectors.toList());
        // Folder + Links 리스트를 Response DTO로 변환
        return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
    }

    //Create
    //폴더를 만드는 것
    public ResponseEntity<FoldersResponse> createFolder(Long userId, FoldersCreateRequest foldersCreateRequest)
    {
        //사용자 조회
        Users users = usersService.findById(userId);
        Folders folders = save(foldersCreateRequest, users);

        return ResponseEntity.ok(FoldersResponse.from(folders, users.getUserId()));
    }

    //Delete
    //폴더 삭제
    public ResponseEntity<FoldersResponse> deleteFolder(Long userId, FoldersDeleteRequest foldersDeleteRequest)
    {
        //사용자 체크
        usersService.findById(userId);

        //FolderLinks에 해당 folderId에 속한 FolderLinks 리스트 조회
        Long folderId = foldersDeleteRequest.getFolderId();
        Folders folders = findById(folderId);
        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folders);

        // FoldersLinks 테이블에서 해당 folderId를 가진 데이터 삭제 ---------------> 이거 해야됨
        foldersLinksService.deleteAll(folderLinksList);
        // folders 삭제
        deleteById(folderId);
        //응답 데이터를 삭제된 링크들까지 포함 시켜서 해주어야함
        return ResponseEntity.ok(FoldersResponse.from(folders, userId));
    }

    //save
    public Folders save(FoldersCreateRequest foldersCreateRequest, Users users){
        Folders folders = Folders.of(foldersCreateRequest, users);
        foldersRepository.save(folders);
        return folders;
    }

    //사용자가 가진 모든 폴더 조회
    public List<Folders> findByUserUserId(Long userId){
        List<Folders> folders = foldersRepository.findByUserUserId(userId);
        if (folders.isEmpty()){
            throw FoldersNotFoundException.of("폴더가 존재하지 않습니다.");
        }
        return folders;
    }



    //하나 폴더 조회
    public Folders findById(Long folderId){
        return foldersRepository.findById(folderId).orElseThrow(() -> FoldersNotFoundException.of("폴더가 존재하지 않습니다."));
    }


    public void deleteById(Long folderId){
        foldersRepository.deleteById(folderId);
    }




}
