package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FolderDeleteDTO;
import com.Kkrap.RequestDTO.FolderInsertUrlRequestDTO;
import com.Kkrap.RequestDTO.FolderUrlRequestDTO;
import com.Kkrap.RequestDTO.FolderCreateRequestDTO;
import com.Kkrap.ResponseDto.FolderUrlResponseDTO;
import com.Kkrap.ResponseDto.MessageResponseDTO;
import com.Kkrap.Service.FoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/v1/folders")
public class FoldersController {
    @Autowired
    private FoldersService foldersService;

    //Selected
    //folders의 전체 Id 보내주기
    @GetMapping("/allid")
    public ResponseEntity<List<Long>> SelectedFoldersId(@RequestParam(name = "userId") Long user_id){
//        FoldersReponseDTO FoldersId = foldersService.SelectedFoldersId(user_id);
        return ResponseEntity.ok(foldersService.SelectedFoldersId(user_id));
    }

    //folders 안에 있는 url 보내주기
    @PostMapping("/folderurl")
    public ResponseEntity<List<FolderUrlResponseDTO>> SelectedFolderUrl(@RequestBody FolderUrlRequestDTO folderUrlRequestDTO)
    {
        List<FolderUrlResponseDTO> responseDto =  foldersService.SelectedFolderUrl(folderUrlRequestDTO.getFolderId(), folderUrlRequestDTO.getUserId());
        return ResponseEntity.ok(responseDto);
    }

    //Create
    //1. 폴더를 만드는 api
    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> CreateFolder(@RequestBody FolderCreateRequestDTO foldersCreateRequestDTO)
    {
        return  foldersService.CreateFolder(foldersCreateRequestDTO);
    }

    //Delete
    @PostMapping("/delete")
    public ResponseEntity<MessageResponseDTO> DeleteFolder(@RequestBody FolderDeleteDTO folderDeleteDTO)
    {
        return foldersService.DeleteFolder(folderDeleteDTO);
    }


    //Update
    //폴더의 이름을 변경







    //폴더 전체 조회 -> 이거는 최악으 상황일 때 사용,,
    //요청은 사용자 id
//    @GetMapping("/all")
//    public List<> SelectedMemberFolders()
//    {}



}
