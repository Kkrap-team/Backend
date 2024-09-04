package com.Kkrap.Service;

import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.LinksDeleteDTO;
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

    public void deleteLink(LinksDeleteDTO linksDeleteDTO){
        List<Links> userLinks = linksRepository.findByUsers_UserId(linksDeleteDTO.getUserId());
        if (userLinks == null || userLinks.isEmpty())
        {
            throw new IllegalArgumentException("해당 사용자에 대한 링크가 없습니다.");
        }


        List<Links> linksToDelete = userLinks.stream() //userLinks는 Link 객체들의 리스트이다. 여기서 .stream() 메서드는 리스트를 스트림(Stream)으로 변환한다. 스트림을 사용하면 컬렉션에 대해 여러 가지 처리를 더 간결하고 효율적으로 할 수 있다
                .filter(links -> linksDeleteDTO.getLinkId().contains(links.getLinkId()))//
                .collect(Collectors.toList());

        if (linksToDelete.isEmpty()){
            throw new IllegalArgumentException("삭제할 링크 ID 값이 존재하지 않습니다.");
        }

        linksRepository.deleteAll(linksToDelete);
    }


}
