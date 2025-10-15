package com.kkrap.Security;

import com.kkrap.Service.SocialLoginRefreshToken.CustomAuthenticationEntryPoint;
import com.kkrap.Service.SocialLoginRefreshToken.JwtAuthenticationFilter;
import com.kkrap.Service.SocialLoginRefreshToken.JwtUtil;
import com.kkrap.Service.Users.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final UsersService usersService;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtUtil jwtUtil,
                          UsersService usersService,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .csrf(csrf -> csrf.disable()) //
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/auth/**", "/profile/**","/api/auth/**", "/folders-search/all", "/folders-search/migrate",
                                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/favicon.ico", "/error", "/folders/users/noauth/scroll", "/folders/users/noauth/**",
                                        "/folders-search/noauth/**"
                                        ).permitAll() // 정적 리소스 허용
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 등록
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, usersService),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://43.203.234.0:3000",
                "http://localhost:5173",         // 개발용
                "http://192.168.1.193:5173",
                "http://192.168.1.193:5174",
                "http://192.168.1.27:5174",
                "http://192.168.1.27:5173",
                "http://192.168.1.27:5174",
                "http://localhost:5174",
                "https://kkrap.cloud",
                "https://www.kkrap.cloud"
        ));

        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()));
        configuration.setAllowCredentials(true); // 쿠키 및 인증 정보 허용
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "refreshToken", "Set-Cookie", "set-cookie"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}