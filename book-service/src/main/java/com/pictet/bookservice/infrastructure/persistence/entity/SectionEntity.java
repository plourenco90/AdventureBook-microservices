package com.pictet.bookservice.infrastructure.persistence.entity;


import com.pictet.bookservice.domain.model.SectionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_sections", uniqueConstraints = @UniqueConstraint(name = "uk_book_sections_book_section", columnNames = {"book_id", "section_id"}))
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Column(name = "section_id", nullable = false)
    private Integer sectionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionType type;

    @OrderBy("optionIndex ASC")
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> options = new ArrayList<>();

    public void addOption(OptionEntity option) {
        options.add(option);
        option.setSection(this);
    }
}
