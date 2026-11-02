package com.pictet.bookservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_validation_error")
public class BookValidationErrorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "validation_id", nullable = false)
    private BookValidationEntity validation;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String message;

    private Integer sectionId;
}
