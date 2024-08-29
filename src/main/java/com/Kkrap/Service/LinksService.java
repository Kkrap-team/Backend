package com.Kkrap.Service;

import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.ResponseDto.LinksResponseDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinksService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private LinksRepository linksRepository;


    //유저별 링크 조회
    public List<LinksResponseDTO> SelectedMemberLinks(Long user_id){
        List<Links> links = linksRepository.findByUsers_UserId(user_id);
        return links.stream()
                .map(link -> new LinksResponseDTO(link.getLinkId(), link.getLinkUrl(), link.getUsers().getUserId()))
                .collect(Collectors.toList());
    }

    //유저별 링크 만들기
    public void createLink(Long user_id, String linkUrl){
        System.out.println(user_id);
        Users user = usersRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + user_id));
        // Links 엔티티 생성
        Links link = Links.builder()
                .users(user)
                .link_url(linkUrl)
                .build();
        // 링크 저장
        linksRepository.save(link);
    }


}
