package com.Kkrap.Entity;

import com.Kkrap.RequestDTO.UsersCreateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false, length = 2048)
    private String profile;

    @Setter
    @Column(nullable = false)
    private Long kakaoId;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folders> folders;


    private Users() { } // 외부에서 new 사용 못하게 보호

    //정적 팩토리 메서드
    public static Users from(UsersCreateRequest usersCreateRequest) {
        return new Users(usersCreateRequest.getEmail(), usersCreateRequest.getNickname(), usersCreateRequest.getProfileImage(), usersCreateRequest.getKakaoId());
    }

    @Builder
    private Users(String email, String nickname, String profile, Long kakaoId)
    {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.kakaoId = kakaoId;
    }

}
