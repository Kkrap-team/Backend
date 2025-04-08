package com.Kkrap.Util;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Kkrap API 문서",
                description = "Swagger를 이용한 Kkrap 백엔드 API 문서입니다.",
                version = "v2"
        )
)
@Configuration
public class Swagger {
}
