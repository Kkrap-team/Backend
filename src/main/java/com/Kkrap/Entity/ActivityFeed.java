package com.Kkrap.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_feed")
@Getter
@Setter
@AllArgsConstructor
public class ActivityFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long actorUserId;

    @Column(nullable = false)
    private Long folderId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private ActivityFeed() {}

    public ActivityFeed(Long actorUserId, Long folderId, LocalDateTime localDateTime) {
        this.actorUserId = actorUserId;
        this.folderId = folderId;
        this.createdAt = localDateTime;
    }

    public static ActivityFeed of(Long actorUserId, Long folderId, LocalDateTime localDateTime) {

        return new ActivityFeed(actorUserId,  folderId, localDateTime);
    }
}
