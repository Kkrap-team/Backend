package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "follows")
public class Follows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follows_id")
    private Long followsId;

    // 팔로우를 거는 사람 (나 자신)
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Users follower;

    // 내가 팔로우하는 대상의 user_id
    @Column(nullable = false)
    private Long followingId;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, length = 2000)
    private String profile;


    public Long getFollwerId(){
        return this.follower != null ? this.follower.getUserId() : null;
    }

    private  Follows() { }

    public Follows(Long userId, String nickname, String profile, Users follower) {
        this.follower = follower;
        this.followingId = userId;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static Follows of(Users follower, Users following){
        return new Follows(following.getUserId(), following.getNickname(), following.getProfile(), follower);
    }
}