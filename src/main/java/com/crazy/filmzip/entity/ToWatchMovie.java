package com.crazy.filmzip.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "to_watch_movies")
@EntityListeners(AuditingEntityListener.class)
public class ToWatchMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "movie_id", nullable = false)
    private Integer movieApiId;

    @Column
    private String title;

    @Column(name = "backdrop_path")
    private String backdropPath;

    @LastModifiedDate
    private LocalDateTime addedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    ToWatchMovie(User user, Integer movieApiId, String title, String backdropPath) {
        this.user = user;
        this.movieApiId = movieApiId;
        this.title = title;
        this.backdropPath = backdropPath;
    }

    @Override
    public String toString() {
        return "ToWatchMovie{" +
                "id=" + id +
                ", user=" + user +
                ", movieApiId=" + movieApiId +
                ", title='" + title + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", addedAt=" + addedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
