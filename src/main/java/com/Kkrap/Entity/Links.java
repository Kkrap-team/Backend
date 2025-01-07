package com.Kkrap.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "links")
@EntityListeners(AuditingEntityListener.class) // Enables JPA Auditing
public class Links {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
//    @JsonBackReference // 순환 참조 방지
    private Users users;

    @Column(nullable = false)
    private String linkUrl;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL)
    private List<FolderList> folderList;


    @Builder
    public Links(Users users, String link_url)
    {
        this.users = users;
        this.linkUrl = link_url;
    }

    public Links(){

    }


}
