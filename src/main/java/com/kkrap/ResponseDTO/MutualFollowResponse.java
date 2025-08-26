package com.kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MutualFollowResponse {
    boolean mutual;


    private MutualFollowResponse(){}

    public static MutualFollowResponse from(boolean mutual){
        return new MutualFollowResponse(mutual);
    }


}
