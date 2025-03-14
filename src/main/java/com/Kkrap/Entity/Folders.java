package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "folders")
@AllArgsConstructor
@NoArgsConstructor
public class Folders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

//    @Column(nullable = false)
//    private Long userId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(nullable = false, length = 500)
    private String folderName;

    @Column(nullable = false, length = 500)
    private String folderDescription;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;


    @OneToMany(mappedBy = "folders", cascade = CascadeType.ALL)
    private List<FoldersLinks> foldersLinks;


    public Folders(Users user, String folderName, String folderDescription, boolean isPublic) {
        this.user = user;
        this.folderName = folderName;
        this.folderDescription = folderDescription;
        this.isPublic = isPublic;
    }
}
