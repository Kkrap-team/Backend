package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.ResponseDto.LinksCreateResponse;
import com.Kkrap.Service.FolderLink.LinksManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/links")
public class LinksController {

    private final LinksManagerService linksManagerService;

    public LinksController(LinksManagerService linksManagerService){
        this.linksManagerService = linksManagerService;
    }


    //Create
    //링크 저장
    @PostMapping("/users/{userId}/links")
    @Operation(summary = "link 저장", description = "링크를 저장하게 되는데 사용자가 모든 저장 링크 폴더에만 저장을 하면 모든 저장 링크 폴더에만 링크가 저장됩니다. 만약 다른 폴더에서 저장하면 다른 폴더에서 링크가 생기고 모든 링크가 저장된 폴더에서도 링크가 생기게 됩니다.")
    @ResponseBody
    public ResponseEntity<LinksCreateResponse> createLinkAndAssignToFolders(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody LinksCreateRequest linksCreateRequest){
        return linksManagerService.createLinkAndAssignToFolders(userId, linksCreateRequest);
    }

    //Delete
    //링크 삭제
    @DeleteMapping("/users/{userId}/links")
    @Operation(summary = "link 삭제", description = "만약 링크가 다른 폴더에 있을 때 모든 링크 폴더에서 삭제하면 다른 폴더에서도 삭제가 됩니다.")
    public ResponseEntity<LinksDeleteRequest> deleteLinksWithFolderMapping(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody LinksDeleteRequest linksDeleteRequest){
        return linksManagerService.deleteLinksWithFolderMapping(userId, linksDeleteRequest);
    }

}
