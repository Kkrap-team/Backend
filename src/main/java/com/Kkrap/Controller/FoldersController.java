package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FoldersCreateRequest;

import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.ResponseDto.MessageResponse;
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
    public ResponseEntity<Object> CreateFolder(@PathVariable("userId") Long userId, @RequestBody FoldersCreateRequest foldersCreateRequest) {
        return foldersService.CreateFolder(userId, foldersCreateRequest);
    }

    //Delete
    @PostMapping("/{userId}/delete")
    public ResponseEntity<Object> DeleteFolder(@PathVariable("userId") Long userId, @RequestBody FoldersDeleteRequest foldersDeleteRequest)
    {
        return foldersService.DeleteFolder(userId, foldersDeleteRequest);
    }


    //Update
    //폴더 제목, 설명 바꾸기
//    @PostMapping
//    public ResponseEntity<Object> InsertLink(@PathVariable("userId") Long userId, @RequestBody )


    //폴더의 이름을 변경







    //폴더 전체 조회 -> 이거는 최악으 상황일 때 사용,,
    //요청은 사용자 id
//    @GetMapping("/all")
//    public List<> SelectedMemberFolders()
//    {}



}
