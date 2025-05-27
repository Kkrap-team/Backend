package com.Kkrap.Entity;

import com.Kkrap.RequestDTO.FoldersCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "folders")
@Getter
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;


    @OneToMany(mappedBy = "folders", cascade = CascadeType.ALL)
    private List<FoldersLinks> foldersLinks;

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
}
