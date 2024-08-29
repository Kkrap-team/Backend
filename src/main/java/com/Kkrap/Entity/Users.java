package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String profile;

    @Setter
    @Column(nullable = false)
    private Long kakao_id;

    @Builder
    public Users(String email, String nickname, String profile, Long kakao_id)
    {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.kakao_id = kakao_id;
    }


}
