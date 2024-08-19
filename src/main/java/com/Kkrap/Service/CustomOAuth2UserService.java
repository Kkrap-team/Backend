package com.Kkrap.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;


//스프링 시큐리티의 DefaultOAuth2UserService를 확장하여 사용자 정보를 가져오고 처리하는 역할
//OAuth2 클라이언트의 사용자 정보를 가져오기 위한 기본 서비스 클래스 DefaultOAuth2UserService
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String userNameAttributeName = userRequest.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            Map<String, Object> attributes = oAuth2User.getAttributes();

            // 로그 출력
            logger.info("카카오로부터 불러온 사용자 정보: {}", attributes);
            System.out.println("registrationId :" + registrationId);
            System.out.println("userNameAttributeName :" + userNameAttributeName);
            System.out.println("attributes :" + attributes);


            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    attributes,
                    userNameAttributeName);
            // 사용자 정보 처리 로직
        } catch (OAuth2AuthenticationException ex) {
            logger.error("OAuth2 authentication error", ex);
            throw ex;
        }


    }
}