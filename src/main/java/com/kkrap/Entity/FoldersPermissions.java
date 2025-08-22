package com.kkrap.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "folders_permissions")
public class FoldersPermissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissions_id")
    private Long permissionsId;

    // 폴더 소유자
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users owner;

    // 공유되는 폴더
    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folders folder;

    // 공유 받는 대상 사용자
    @Column(nullable = false)
    private Long invitedUserId;

    @Column(length = 255, nullable = false)
    private String nickname;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    private FoldersPermissions(){};

    public FoldersPermissions(Users owner, Folders folders, Long invitedUserId, String nickname) {
        this.owner = owner;
        this.folder = folders;
        this.invitedUserId = invitedUserId;
        this.nickname = nickname;
    }

    public static FoldersPermissions of(Users owner, Folders folders, Users invitedUser){
        return new FoldersPermissions(owner, folders, invitedUser.getUserId(), invitedUser.getNickname());

    }



}
