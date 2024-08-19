package com.Kkrap.Security;

import com.Kkrap.Service.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

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

                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user principal type");
                }
            }
        };
    }
    private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

}