package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "links")
@AllArgsConstructor
public class Links {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @Column(nullable = false, length = 2084)
    private String linkUrl;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(nullable = true, length = 500)
    private String linkName;

    @Column(length = 2084)
    private String thumbnailUrl;

    @Column(length = 2084)
    private String faviconUrl;


    @OneToMany(mappedBy = "links", cascade = CascadeType.ALL)
    private List<FoldersLinks> foldersLinks;

    private Links(String linkUrl, String linkName, String thumbnailUrl, String faviconUrl){
        this.linkUrl = linkUrl;
        this.linkName = linkName;
        this.thumbnailUrl = thumbnailUrl;
        this.faviconUrl = faviconUrl;
    }

    private Links() {};

    public static Links of(String linkUrl, String linkName, String thumbnailUrl, String faviconUrl){
        linkUrl = linkUrl;
        linkName = linkName;
        thumbnailUrl = thumbnailUrl;
        faviconUrl = faviconUrl;
        return new Links(linkUrl, linkName,thumbnailUrl, faviconUrl);
    }


}
