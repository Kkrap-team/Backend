package com.Kkrap.Util;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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

    // Enable @Timed on methods without AOP dependency elsewhere
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
