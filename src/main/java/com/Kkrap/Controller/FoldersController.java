package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.Service.FoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/v2/folders")
public class FoldersController {
    @Autowired
    private FoldersService foldersService;

    //Selected
    //사용자가 가지고 있는 모든 폴더와 내안 있는 링크들 같이 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAll(@PathVariable("userId") Long userId){
        return foldersService.getFoldersAll(userId);
    }

//    //Create
//    //1. 폴더를 만드는 api
    @PostMapping("/{userId}/create")
    public ResponseEntity<Object> CreateFolder(@PathVariable("userId") Long userId, @RequestBody FoldersCreateRequest foldersCreateRequest)
    {
        return  foldersService.CreateFolder(userId, foldersCreateRequest);
    }
//
//    //Delete
//    @PostMapping("/delete")
//    public ResponseEntity<MessageResponseDTO> DeleteFolder(@RequestBody FolderDeleteDTO folderDeleteDTO)
//    {
//        return foldersService.DeleteFolder(folderDeleteDTO);
//    }


    //Update
    //1. 폴더에 링크 추가
//    @PostMapping
//    public ResponseEntity<Object> InsertLink(@PathVariable("userId") Long userId, @RequestBody )


    //폴더의 이름을 변경







    //폴더 전체 조회 -> 이거는 최악으 상황일 때 사용,,
    //요청은 사용자 id
//    @GetMapping("/all")
//    public List<> SelectedMemberFolders()
//    {}



}
