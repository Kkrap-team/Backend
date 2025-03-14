package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FolderInsertUrlRequestDTO;
import com.Kkrap.Service.FoldersLinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/v1/folderlist")
public class FoldersLinksController {

    @Autowired
    private FoldersLinksService foldersLinksService;

//    @PostMapping("/insert/url")
//    public ResponseEntity<FolderInsertUrlRequestDTO> InsertUrlFolder(@RequestBody FolderInsertUrlRequestDTO folderInsertUrlRequestDTO)
//    {
//        return foldersLinksService.InsertUrlFolder(folderInsertUrlRequestDTO);
//    }


}
