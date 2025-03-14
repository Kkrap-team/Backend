package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "folders_links")
public class FolderList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foldersLinksId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "folder_id")
    private Folders folders;

    @ManyToOne
    @JoinColumn(nullable = false, name = "link_id")
    private Links links;

}
