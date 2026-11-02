package com.pictet.bookservice.infrastructure.persistence.entity;

import com.pictet.bookservice.domain.model.ConsequenceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "option_consequences")
public class OptionConsequenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "option_id")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private OptionEntity option;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsequenceType type;

    @Column(nullable = false)
    private Integer value;

    @Column(columnDefinition = "TEXT")
    private String text;
}
