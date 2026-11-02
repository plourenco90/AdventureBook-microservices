package com.pictet.bookservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_validation")
public class BookValidationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private BookEntity book;

    @OneToMany(mappedBy = "validation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookValidationErrorEntity> errors = new ArrayList<>();
}
