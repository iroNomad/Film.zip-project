package com.crazy.filmzip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "genre")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Genre {

    @Id
    @Column(name = "id")
    private Long id; // user.id와 동일하게 사용

    @Column(nullable = false)
    private String genres;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId // user_id를 id로 매핑
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;
}
