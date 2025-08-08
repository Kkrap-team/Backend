package com.Kkrap.Security;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Kafka.FolderViewConsumer;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.Service.CustomOAuth2UserService;
import com.Kkrap.Service.SocialLoginRefreshToken.CustomAuthenticationEntryPoint;
import com.Kkrap.Service.SocialLoginRefreshToken.JwtAuthenticationFilter;
import com.Kkrap.Service.SocialLoginRefreshToken.JwtUtil;
import com.Kkrap.Service.Users.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final CustomOAuth2UserService oAuth2UserService;

    private final UsersService usersService;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;



    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService,
                          JwtUtil jwtUtil,
                          UsersService usersService,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.oAuth2UserService = oAuth2UserService;
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
                                .requestMatchers("/auth/**", "/profile/**").permitAll() // 정적 리소스 허용
                                .anyRequest().authenticated()
                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login")
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(oAuth2UserService)
//                        )
//                        .successHandler(authenticationSuccessHandler())  // 로그인 성공 시 핸들러 사용
//                )
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
//        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://43.203.234.0:3000",
                "http://localhost:5173",         // 개발용
                "http://192.168.1.193:5173",
                "http://192.168.1.193:5174",
                "http://192.168.1.27:5174",
                "http://192.168.1.27:5173",
                "http://192.168.1.27:5174",
                "http://localhost:5174"
        ));


        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()));
        configuration.setAllowCredentials(true); // 쿠키 및 인증 정보 허용
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "refreshToken", "Set-Cookie", "set-cookie"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                Object principal = authentication.getPrincipal();
//                if (principal instanceof DefaultOAuth2User) {
//                    DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) principal;
//
//                    // 카카오 사용자 정보 추출
//                    Map<String, Object> attributes = defaultOAuth2User.getAttributes();
//
//                    String kakao_id = attributes.get("id").toString(); // 사용자 ID
//                    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//
//                    String nickname = profile.get("nickname").toString(); // 사용자 닉네임
//                    String profileImage = profile.get("profile_image_url").toString(); // 프로필 이미지 URL
//                    String email = kakaoAccount.get("email").toString(); // 이메일
//
//                    // 디버그용 로그 출력
//                    logger.info("카카오 사용자 ID: " + kakao_id);
//                    logger.info("카카오 사용자 닉네임: " + nickname);
//                    logger.info("카카오 사용자 이메일: " + email);
//                    logger.info("카카오 사용자 프로필 이미지 URL: " + profileImage);
//
//                    // 사용자 정보를 각각 쿠키에 저장
//                    setCookie(response, "kakao_id", kakao_id, 7 * 24 * 60 * 60); // 쿠키 유효기간 7일
//                    setCookie(response, "nickname", nickname, 7 * 24 * 60 * 60);
//                    setCookie(response, "profileImage", profileImage, 7 * 24 * 60 * 60);
//                    setCookie(response, "email", email, 7 * 24 * 60 * 60);
//
//
//                    //DB 로직 추가
//                    String userId;
//                    Optional<Users> CheckUser = usersRepository.findByKaKaoId(Long.valueOf(kakao_id));
//                    logger.info("CheckUser : " + CheckUser);
//                    if (CheckUser.isEmpty()){
//                        UsersCreateRequest usersCreateRequest = UsersCreateRequest.of(email, nickname, profileImage, Long.valueOf(kakao_id), null);
//                        Users newUser = usersService.save(usersCreateRequest);
//
//                        setCookie(response, "userId", String.valueOf(newUser.getUserId()), 7 * 24 * 60 * 60);
//                        userId = String.valueOf(newUser.getUserId());
//
//                        // 처음 로그인 한 사람은 모든 링크 보기 폴더가 없음 만들어주어야함
//                        Folders folder = new Folders(newUser, "모든 링크", "모든 링크가 저장된 폴더입니다.", false, true);
//                        foldersRepository.save(folder);
//                    }
//                    else
//                    {
//                        Users existingUser = CheckUser.get();
//                        setCookie(response, "userId", String.valueOf(existingUser.getUserId()), 7 * 24 * 60 * 60);
//                        userId = String.valueOf(existingUser.getUserId());
//                    }
//
//                    // 사용자 정보를 URL 인코딩
//                    String redirectUrl = String.format(
//                            "http://localhost:3000/login/success?kakao_id=%s&nickname=%s&profileImage=%s&email=%s&userId=%s",
//                            URLEncoder.encode(kakao_id, StandardCharsets.UTF_8.toString()),
//                            URLEncoder.encode(nickname, StandardCharsets.UTF_8.toString()),
//                            URLEncoder.encode(profileImage, StandardCharsets.UTF_8.toString()),
//                            URLEncoder.encode(email, StandardCharsets.UTF_8.toString()),
//                            URLEncoder.encode(userId, StandardCharsets.UTF_8.toString())
//                    );
//
//                    // 리디렉션
//                    response.sendRedirect(redirectUrl);
//
//                } else {
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user principal type");
//                }
//            }
//        };
//    }
    private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);

//        cookie.setPath("/");
//        cookie.setMaxAge(maxAge);
//        response.addCookie(cookie);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(false); // JavaScript에서 접근 가능하도록 설정
        cookie.setSecure(false); // HTTPS가 아닌 경우에도 전송되도록 설정
//        cookie.setDomain("172.20.10.12"); // 도메인을 프론트엔드 주소로 설정
        cookie.setDomain("43.203.234.0"); // 도메인을 프론트엔드 주소로 설정
//        cookie.setDomain("192.168.1.193"); // 도메인을 프론트엔드 주소로 설정
        response.addCookie(cookie);
    }

}