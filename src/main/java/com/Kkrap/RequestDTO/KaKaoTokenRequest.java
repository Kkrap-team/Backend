package com.Kkrap.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoTokenRequest {
    @Schema(description = "카카오에서 받은 액세스 토큰", example = "kakao_access_token_123456", required = true)
    private String accesstoken;
}
