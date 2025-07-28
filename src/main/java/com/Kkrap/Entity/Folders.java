package com.Kkrap.Entity;

import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.FoldersScrapRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "folders")
@Getter
@Setter
@AllArgsConstructor
public class Folders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(nullable = false, length = 500)
    private String folderName;

    @Column(nullable = true, length = 500)
    private String folderDescription;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private boolean defaultFolder;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long viewCount = 0L;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long scrapCount = 0L;


    @Column(nullable = false)
    private boolean shared;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToMany(mappedBy = "folders", cascade = CascadeType.ALL)
    private List<FoldersLinks> foldersLinks;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FoldersPermissions> folderPermissions;

    public Folders(Users user, String folderName, String folderDescription, boolean visible, boolean defaultFolder) {
        this.user = user;
        this.folderName = folderName;
        this.folderDescription = folderDescription;
        this.visible = visible;
        this.defaultFolder = defaultFolder;
    }

    private Folders() {} //외부에서 new 사용 못하게 보호

    public static Folders of(FoldersCreateRequest foldersCreateRequest, Users user){
        return new Folders(user, foldersCreateRequest.getFolderName(), foldersCreateRequest.getFolderDescription(), foldersCreateRequest.isVisible(), foldersCreateRequest.isDefaultFolder());
    }

    public static Folders of(FoldersScrapRequest foldersScrapRequest, Users user){
        return new Folders(user, foldersScrapRequest.getFolderName(), foldersScrapRequest.getFolderDescription(), foldersScrapRequest.isVisible(), false);
    }

    public boolean isOwnedBy(Long userId) {
        return this.user != null && this.user.getUserId().equals(userId);
    }
}
