package com.pictet.adventureservice.infrastructure.persistence.entity;

import com.pictet.adventureservice.domain.model.AdventureStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "adventures")
@EntityListeners(AuditingEntityListener.class)
public class AdventureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "current_section_id", nullable = false)
    private Integer currentSectionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdventureStatus status;

    @Embedded
    private PlayerEmbeddable player;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @Version
    private Long version;

}
