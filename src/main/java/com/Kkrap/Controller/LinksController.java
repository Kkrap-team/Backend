package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.LinksAPISpec;
import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.RequestDTO.LinksTitleUpdateRequest;
import com.Kkrap.ResponseDTO.LinksCreateResponse;
import com.Kkrap.ResponseDTO.LinksResponse;
import com.Kkrap.Service.FolderLink.LinksManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LinksCreateResponse> createLinkAndAssignToFolders(Long userId, LinksCreateRequest linksCreateRequest) {
        return linksManagerService.createLinkAndAssignToFolders(userId, linksCreateRequest);
    }

    @Override
    public ResponseEntity<LinksDeleteRequest> deleteLinksWithFolderMapping(Long userId, LinksDeleteRequest linksDeleteRequest) {
        return linksManagerService.deleteLinksWithFolderMapping(userId, linksDeleteRequest);
    }

    @Override
    public ResponseEntity<LinksResponse> updateLinkTitle(Long userId, LinksTitleUpdateRequest linksTitleUpdateRequest) {
        return linksManagerService.updateLinkTitle(userId, linksTitleUpdateRequest);
    }
}
