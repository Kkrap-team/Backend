package com.Kkrap.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinksDeleteRequest {
    private Long defaultFoldersId;

    private Long foldersId;

    private List<Long> linkId;

}
