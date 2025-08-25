package com.kkrap.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "folders_links")
@AllArgsConstructor
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

    private FoldersLinks(Folders folders, Links links, Long userId){
        this.folders = folders;
        this.links = links;
        this.userId = userId;
    }

    private FoldersLinks() {}

    public static FoldersLinks of(Folders folders, Links links, Long userId){
        return new FoldersLinks(folders, links, userId);
    }

}
