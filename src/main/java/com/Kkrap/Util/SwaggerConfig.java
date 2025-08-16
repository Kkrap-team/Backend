package com.Kkrap.Util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(
                title = "Kkrap API 문서",
                description = "Swagger를 이용한 Kkrap 백엔드 API 문서입니다.",
                version = "v2"
        )
)
@Configuration
public class SwaggerConfig {


        private static final String SECURITY_SCHEME_NAME = "bearerAuth";

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                        // 전역으로 Bearer 인증을 요구(permitAll 엔드포인트는 실제 시큐리티에서만 열립니다)
                        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                        .components(new Components()
                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
        }
        @Bean
        public TimedAspect timedAspect(MeterRegistry registry) {
            return new TimedAspect(registry);
        }
}

    // Enable @Timed on methods without AOP dependency elsewhere

