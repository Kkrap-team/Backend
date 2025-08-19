package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.LinksAPISpec;
import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.RequestDTO.LinksTitleUpdateRequest;
import com.Kkrap.RequestDTO.MoveLinkToAnotherFolders;
import com.Kkrap.ResponseDTO.LinksCreateResponse;
import com.Kkrap.ResponseDTO.LinksResponse;
import com.Kkrap.Service.FolderLink.LinksManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Timed(value = "http.controller", extraTags = {"controller","Links"})
@RequestMapping("/links")
public class LinksController implements LinksAPISpec {

    private final LinksManagerService linksManagerService;

    public LinksController(LinksManagerService linksManagerService){
        this.linksManagerService = linksManagerService;
    }

    @Override
    public ResponseEntity<LinksCreateResponse> createLinkAndAssignToFolders(Authentication authentication, LinksCreateRequest linksCreateRequest) {
        Long userId = Long.parseLong(authentication.getName());
        return linksManagerService.createLinkAndAssignToFolders(userId, linksCreateRequest);
    }

    @Override
    public ResponseEntity<LinksDeleteRequest> deleteLinksWithFolderMapping(Authentication authentication, LinksDeleteRequest linksDeleteRequest) {
        Long userId = Long.parseLong(authentication.getName());
        return linksManagerService.deleteLinksWithFolderMapping(userId, linksDeleteRequest);
    }

    @Override
    public ResponseEntity<LinksResponse> updateLinkTitle(Authentication authentication, LinksTitleUpdateRequest linksTitleUpdateRequest) {
        Long userId = Long.parseLong(authentication.getName());
        return linksManagerService.updateLinkTitle(userId, linksTitleUpdateRequest);
    }

    @Override
    public ResponseEntity<LinksResponse> moveLinkToAnotherFolders(Authentication authentication, MoveLinkToAnotherFolders moveLinkToAnotherFolders) {
        Long userId = Long.parseLong(authentication.getName());
        return linksManagerService.moveLinkToAnotherFolders(userId, moveLinkToAnotherFolders);
    }
}
