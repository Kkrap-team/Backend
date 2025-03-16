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
@Setter
@Getter
@Table(name = "links")
@AllArgsConstructor
@NoArgsConstructor
public class Links {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @Column(nullable = false, length = 2084)
    private String linkUrl;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(nullable = false, length = 500)
    private String linkName;

    @OneToMany(mappedBy = "links", cascade = CascadeType.ALL)
    private List<FoldersLinks> foldersLinks;

    public Links(String linkUrl, String linkName){
        this.linkUrl = linkUrl;
        this.linkName = linkName;
    }


}
