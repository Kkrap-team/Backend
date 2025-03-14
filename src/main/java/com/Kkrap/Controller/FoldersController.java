package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FolderCreateRequest;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.ResponseDto.MessageResponseDTO;
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
    //사용자가 가지고 있는 모든 폴더 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<FoldersResponse>> getFoldersAll(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(foldersService.getFoldersAll(userId));
    }

    //folders 상세
//    @PostMapping("/folderurl")
//    public ResponseEntity<List<FolderUrlResponseDTO>> SelectedFolderUrl(@RequestBody FolderUrlRequestDTO folderUrlRequestDTO)
//    {
//        List<FolderUrlResponseDTO> responseDto =  foldersService.SelectedFolderUrl(folderUrlRequestDTO.getFolderId(), folderUrlRequestDTO.getUserId());
//        return ResponseEntity.ok(responseDto);
//    }

//    //Create
//    //1. 폴더를 만드는 api
    @PostMapping("/{userId}/create")
    public ResponseEntity<MessageResponseDTO> CreateFolder(@RequestBody FolderCreateRequest foldersCreateRequest)
    {
        return  foldersService.CreateFolder(foldersCreateRequest);
    }
//
//    //Delete
//    @PostMapping("/delete")
//    public ResponseEntity<MessageResponseDTO> DeleteFolder(@RequestBody FolderDeleteDTO folderDeleteDTO)
//    {
//        return foldersService.DeleteFolder(folderDeleteDTO);
//    }


    //Update
    //폴더의 이름을 변경







    //폴더 전체 조회 -> 이거는 최악으 상황일 때 사용,,
    //요청은 사용자 id
//    @GetMapping("/all")
//    public List<> SelectedMemberFolders()
//    {}



}
