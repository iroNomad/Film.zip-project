package com.crazy.filmzip.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "community_posts")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private int views;

    private int hasForbiddenWords;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReaction> reactions = new ArrayList<>();

    @Builder
    public CommunityPost(Long id, String title, String content, int views, int hasForbiddenWords,
                         LocalDateTime createdAt, LocalDateTime updatedAt, User user,
                         List<Comment> comments, List<PostReaction> reactions) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.hasForbiddenWords = hasForbiddenWords;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.comments = comments != null ? comments : new ArrayList<>();
        this.reactions = reactions != null ? reactions : new ArrayList<>();
    }

}
