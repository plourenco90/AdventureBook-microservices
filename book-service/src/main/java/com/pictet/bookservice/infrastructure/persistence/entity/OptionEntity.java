package com.pictet.bookservice.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "section_options", uniqueConstraints = @UniqueConstraint(name = "uk_section_options_index", columnNames = {"section_id", "option_index"}))
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private SectionEntity section;

    @Column(name = "option_index", nullable = false)
    private Integer optionIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "target_section_id", nullable = false)
    private Integer targetSectionId;

    @OneToOne(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private OptionConsequenceEntity consequence;

    public void addConsequence(OptionConsequenceEntity consequence) {
        this.consequence = consequence;
        this.consequence.setOption(this);
    }
}
