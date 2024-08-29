package com.Kkrap.Security;

import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.Service.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UsersRepository usersRepository;

    private final CustomOAuth2UserService oAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .csrf(csrf -> csrf.disable()) //
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()
//                        .requestMatchers("/login", "/oauth2/**").permitAll()
//                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(authenticationSuccessHandler())  // 로그인 성공 시 핸들러 사용
                );

        return http.build();
    }
    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://172.20.10.12:3000")); // 프론트엔드 주소 허용
        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setAllowCredentials(true); // 쿠키 및 인증 정보 허용
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                Object principal = authentication.getPrincipal();
                if (principal instanceof DefaultOAuth2User) {
                    DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) principal;

                    // 카카오 사용자 정보 추출
                    Map<String, Object> attributes = defaultOAuth2User.getAttributes();

                    String id = attributes.get("id").toString(); // 사용자 ID
                    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                    String nickname = profile.get("nickname").toString(); // 사용자 닉네임
                    String profileImage = profile.get("profile_image_url").toString(); // 프로필 이미지 URL
                    String email = kakaoAccount.get("email").toString(); // 이메일

                    // 디버그용 로그 출력
                    System.out.println("카카오 사용자 ID: " + id);
                    System.out.println("카카오 사용자 닉네임: " + nickname);
                    System.out.println("카카오 사용자 이메일: " + email);
                    System.out.println("카카오 사용자 프로필 이미지 URL: " + profileImage);

                    // 사용자 정보를 각각 쿠키에 저장
                    setCookie(response, "id", id, 7 * 24 * 60 * 60); // 쿠키 유효기간 7일
                    setCookie(response, "nickname", nickname, 7 * 24 * 60 * 60);
                    setCookie(response, "profileImage", profileImage, 7 * 24 * 60 * 60);
                    setCookie(response, "email", email, 7 * 24 * 60 * 60);


                    //DB 로직 추가
                    Optional<Users> CheckUser = usersRepository.findByKaKaoId(Long.valueOf(id));
                    if (CheckUser.isEmpty()){
                        Users newUser = new Users(email, nickname, profileImage, Long.valueOf(id));
                        usersRepository.save(newUser);
                    }

                    //-----------
                    // DB에서 유저 존재 여부 확인
//                    Users user = usersRepository.findById(Long.valueOf(id)).orElse(null);
//
//                    if (user != null) {
//                        // 사용자가 존재하는 경우
//                        System.out.println("기존 사용자가 존재합니다: " + user.getUsername());
//                    } else {
//                        // 사용자가 존재하지 않는 경우, 새로운 사용자를 생성할 수 있습니다.
//                        Users newUser = new Users();
//                        newUser.setId(Long.valueOf(id));
//                        newUser.setUsername(nickname);
//                        newUser.setEmail(email);
//                        newUser.setProfileImage(profileImage);
//                        usersRepository.save(newUser);
//                        System.out.println("새로운 사용자가 생성되었습니다: " + newUser.getUsername());
//                    }
                    //------------------

                    // 사용자 정보를 추출
//                    String id = defaultOAuth2User.getAttribute("id").toString();
//                    Map<String, Object> profileInfo = defaultOAuth2User.getAttribute("kakao_account");
//                    String nickname = (String) ((Map<String, Object>) profileInfo.get("profile")).get("nickname");
//                    String email = (String) profileInfo.get("email");

//                    String jwtToken = generateToken(id, email, nickname);

                    // JWT 토큰을 응답에 추가
//                    response.setHeader("Authorization", "Bearer " + jwtToken);

                    // 로그인 성공 후 프론트엔드 경로로 리디렉션
//                    response.sendRedirect("http://172.20.10.14:3000/login/success");
                    // 로그인 성공 후 프론트엔드 경로로 리디렉션
//                    response.sendRedirect("http://localhost:3000/login/success?token=" + jwtToken);

                    // 로그인 성공 후 프론트엔드 경로로 리디렉션
//                    String redirectUrl = String.format("http://localhost:3000/login/success?token=%s&email=%s", jwtToken, email);
                    response.sendRedirect("http://172.20.10.12:3000/login/success");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user principal type");
                }
            }
        };
    }
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
        cookie.setDomain("172.20.10.12"); // 도메인을 프론트엔드 주소로 설정
        response.addCookie(cookie);
    }

}