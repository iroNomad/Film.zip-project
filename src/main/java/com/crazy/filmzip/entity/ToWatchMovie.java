package com.crazy.filmzip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "to_watch_movies")
@EntityListeners(AuditingEntityListener.class)
public class ToWatchMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String movieApiId;

    @LastModifiedDate
    private LocalDateTime addedAt;

    @CreatedDate
    private LocalDateTime createdAt;
}
