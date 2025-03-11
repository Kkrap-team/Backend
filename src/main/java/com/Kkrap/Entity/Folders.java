package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "folders")
public class Folders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private String FolderUrl;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(nullable = false)
    private String folderName;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FolderList> folderList;

    @Builder
    public Folders(Long userId){
        this.userId = userId;
    }

    public Folders(Long userId, String folderName){
        this.userId = userId;
        this.folderName = folderName;
    }

    public Folders()
    {}


}
