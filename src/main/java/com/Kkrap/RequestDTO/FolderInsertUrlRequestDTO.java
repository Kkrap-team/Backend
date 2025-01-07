package com.Kkrap.RequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderInsertUrlRequestDTO {
    private Long folderId;

    private List<Long> linkId;

    private Long userId;
}
