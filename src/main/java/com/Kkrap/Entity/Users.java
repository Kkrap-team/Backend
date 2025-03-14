package com.Kkrap.Entity;

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

//    // Links와의 관계 추가
//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
////    @JsonIgnore // 순환 참조 방지
//    private List<Links> links;



    @Builder
    public Users(String email, String nickname, String profile, Long kakaoId)
    {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.kakaoId = kakaoId;
    }

    public Users() {
        // 기본 생성자
    }
}
