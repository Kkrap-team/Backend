package com.Kkrap.Entity;

import com.Kkrap.RequestDTO.UsersCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, length = 2048)
    private String profile;

    @Column(nullable = false)
    private Long kakaoId;

    @Column(nullable = true, length = 150)
    private String bio;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folders> folders;


    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follows> followings;


    private Users() { } // 외부에서 new 사용 못하게 보호

    //정적 팩토리 메서드
    public static Users from(UsersCreateRequest usersCreateRequest) {
        return new Users(usersCreateRequest.getEmail(), usersCreateRequest.getNickname(), usersCreateRequest.getProfileImage(), usersCreateRequest.getKakaoId(), usersCreateRequest.getBio());
    }

    @Builder
    private Users(String email, String nickname, String profile, Long kakaoId, String bio)
    {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.kakaoId = kakaoId;
        this.bio = bio;
    }

}
