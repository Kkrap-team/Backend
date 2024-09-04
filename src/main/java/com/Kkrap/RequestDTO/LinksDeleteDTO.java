package com.Kkrap.RequestDTO;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LinksDeleteDTO {
    private Long userId;
    private List<Long> linkId;
}
