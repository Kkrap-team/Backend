package com.kkrap.Controller.Spec;

import com.kkrap.RequestDTO.LinksCreateRequest;
import com.kkrap.RequestDTO.LinksDeleteRequest;
import com.kkrap.RequestDTO.LinksTitleUpdateRequest;
import com.kkrap.RequestDTO.MoveLinkToAnotherFolders;
import com.kkrap.ResponseDTO.LinksCreateResponse;
import com.kkrap.ResponseDTO.LinksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Links", description = "링크 저장/삭제 API Endpoint")
public interface LinksAPISpec {
    //Create
    //링크 저장
    @PostMapping("/users/links")
    @Operation(
            summary = "link 저장",
            description = "링크를 저장하게 되는데 사용자가 모든 저장 링크 폴더에만 저장을 하면 모든 저장 링크 폴더에만 링크가 저장됩니다. " +
                    "만약 다른 폴더에서 저장하면 다른 폴더에서 링크가 생기고 모든 링크가 저장된 폴더에서도 링크가 생기게 됩니다."
    )
    ResponseEntity<LinksCreateResponse> createLinkAndAssignToFolders(
            Authentication authentication,
            @RequestBody LinksCreateRequest linksCreateRequest);

    //Delete
    //링크 삭제
    @DeleteMapping("/users/links")
    @Operation(
            summary = "link 삭제",
            description = "만약 링크가 다른 폴더에 있을 때 모든 링크 폴더에서 삭제하면 다른 폴더에서도 삭제가 됩니다."
    )
    ResponseEntity<LinksDeleteRequest> deleteLinksWithFolderMapping(
            Authentication authentication,
            @RequestBody LinksDeleteRequest linksDeleteRequest);


    //links 제목 변경
    @PatchMapping("/users/links/title")
    @Operation(
            summary = "link 제목 수정",
            description = "사용자가 link 설정에서 제목 변경하는 api입니다."
    )
    ResponseEntity<LinksResponse> updateLinkTitle(
            Authentication authentication,
            @RequestBody LinksTitleUpdateRequest linksTitleUpdateRequest
    );

    @PatchMapping("/users/links/move")
    @Operation(
            summary = "링크 폴더 이동",
            description = "링크를 현재 폴더에서 다른 폴더로 이동합니다. " +
                    "이동 시 모든 저장 링크 폴더와의 관계도 함께 반영됩니다."
    )
    ResponseEntity<LinksResponse> moveLinkToAnotherFolders(
            Authentication authentication,
            @RequestBody MoveLinkToAnotherFolders moveLinkToAnotherFolders
            );


}
