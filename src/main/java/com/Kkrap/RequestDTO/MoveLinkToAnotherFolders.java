package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MoveLinkToAnotherFolders {
    private Long linkId;
    private Long sourceFolderId;
    private Long targetFolderId;
}
