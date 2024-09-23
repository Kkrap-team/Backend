package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "folderlist")
public class FolderList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderListId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "folder_id")
    private Folders folder;

    @ManyToOne
    @JoinColumn(nullable = false, name = "link_id")
    private Links link;

}
