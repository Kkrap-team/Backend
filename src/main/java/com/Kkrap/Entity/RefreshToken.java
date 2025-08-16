package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String refreshToken;


    @Column(nullable = false)
    private LocalDateTime expiration;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // 생성자, builder 등 생략
    private RefreshToken() {};

    public RefreshToken(String token, Users user) {
        this.refreshToken = token;
        this.user = user;
        this.expiration = LocalDateTime.now().plusDays(7);
    }

    public static RefreshToken of(String token, Users user){
        return new RefreshToken(token, user);
    }
}
