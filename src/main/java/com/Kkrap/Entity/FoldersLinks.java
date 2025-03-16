package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "folders_links")
@AllArgsConstructor
@NoArgsConstructor
public class FoldersLinks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foldersLinksId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "folder_id")
    private Folders folders;

    @ManyToOne
    @JoinColumn(nullable = false, name = "link_id")
    private Links links;

    @Column(nullable = false)
    private Long userId;

    public FoldersLinks(Folders folders, Links links, Long userId){
        this.folders = folders;
        this.links = links;
        this.userId = userId;
    }

}
