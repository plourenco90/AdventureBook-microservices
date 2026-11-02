package com.pictet.bookservice.domain.validation;

import com.pictet.bookservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookValidatorTest {

    private BookValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BookValidator();
    }

    @Test
    void shouldValidateOriginalPrisonerBook() {
        Book book = originalPrisonerBook();
        BookValidationResult result = validator.validate(book);
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void shouldDetectAllErrorsInDraft1() {
        Book book = draft1Book();
        BookValidationResult result = validator.validate(book);
        assertTrue(hasError(result, "MULTIPLE_BEGINNINGS"));
        assertTrue(hasError(result, "MISSING_ENDING"));
        assertTrue(hasError(result, "INVALID_NEXT_SECTION"));
        assertTrue(hasError(result, "MISSING_OPTIONS"));
    }

    @Test
    void shouldDetectInvalidReferenceInDraft2() {
        Book book = draft2Book();
        BookValidationResult result = validator.validate(book);
        assertEquals(1, result.errors().size());
        assertTrue(hasError(result, "INVALID_NEXT_SECTION"));
    }

    @Test
    void shouldRejectBookWithoutBeginning() {
        Book book = new Book(4L, "No Beginning", "Author", List.of(section(10, SectionType.END)));
        BookValidationResult result = validator.validate(book);
        assertTrue(hasError(result, "MISSING_BEGINNING"));
    }

    @Test
    void shouldRejectBookWithoutEnding() {
        Book book = new Book(5L,
                "No Ending",
                "Author",
                List.of(section(1, SectionType.BEGIN, option("Continue", 2)),
                        section(2, SectionType.NODE, option("Back", 1))));

        BookValidationResult result = validator.validate(book);
        assertTrue(hasError(result, "MISSING_ENDING"));
    }

    @Test
    void shouldRejectNonEndingSectionWithoutOptions() {
        Book book = new Book(6L,
                "Missing Options",
                "Author",
                List.of(section(1, SectionType.BEGIN), section(2, SectionType.END)));
        BookValidationResult result = validator.validate(book);
        assertTrue(hasError(result, "MISSING_OPTIONS"));
    }

    @Test
    void shouldAllowEndingSectionWithoutOptions() {
        Book book = new Book(7L,
                "Valid Ending",
                "Author",
                List.of(section(1, SectionType.BEGIN, option("Finish", 2)), section(2, SectionType.END)));

        BookValidationResult result = validator.validate(book);
    }

    @Test
    void shouldDetectDuplicateSectionIds() {
        Book book = new Book(8L,
                "Duplicate IDs",
                "Author",
                List.of(section(1, SectionType.BEGIN, option("Continue", 2)),
                        section(1, SectionType.NODE, option("Finish", 2)),
                        section(2, SectionType.END)));

        BookValidationResult result = validator.validate(book);
        assertTrue(hasError(result, "DUPLICATE_SECTION_ID"));
    }

    private boolean hasError(BookValidationResult result, String code) {
        return result.errors().stream().anyMatch(error -> error.code().equals(code));
    }

    private Section section(int id, SectionType type, Option... options) {
        return new Section(id, "Section " + id, type, List.of(options));
    }

    private Option option(String description, int targetId) {
        return new Option(description, 0, targetId, null);
    }

    private Option optionWithConsequence(String description, int targetId, int healthLoss, String consequenceText) {
        return new Option(description,
                0,
                targetId,
                new Consequence(ConsequenceType.LOSE_HEALTH, healthLoss, consequenceText));
    }

    private Book originalPrisonerBook() {
        return new Book(1L,
                "The Prisoner",
                "Daniel El Fuego",
                List.of(section(1, SectionType.BEGIN, option("Open door", 500), option("Look under bed", 20)),
                        section(20,
                                SectionType.NODE,
                                optionWithConsequence("Scan the area", 30, 6, "You cut yourself.")),
                        section(30, SectionType.NODE, option("Open door with key", 1000)),
                        section(500,
                                SectionType.NODE,
                                optionWithConsequence("Gather your thoughts", 1, 3, "You're getting crazier.")),
                        section(1000, SectionType.END)));
    }

    private Book draft1Book() {
        return new Book(2L,
                "The Prisoner (draft 1)",
                "Daniel El Fuego",
                List.of(section(1, SectionType.BEGIN, option("Open door", 50011), option("Look under bed", 20)),
                        section(20, SectionType.BEGIN, option("Scan the area", 30)),
                        section(30, SectionType.NODE, option("Open door with key", 1000)),
                        section(500, SectionType.NODE, option("Gather thoughts", 1)),
                        section(1000, SectionType.NODE)));
    }

    private Book draft2Book() {
        return new Book(3L,
                "The Prisoner (draft 2)",
                "Daniel El Fuego",
                List.of(section(1, SectionType.BEGIN, option("Open door", 50011), option("Look under bed", 20)),
                        section(20, SectionType.NODE, option("Scan the area", 30)),
                        section(30, SectionType.NODE, option("Open door with key", 1000)),
                        section(500, SectionType.NODE, option("Gather thoughts", 1)),
                        section(1000, SectionType.END)));
    }
}
