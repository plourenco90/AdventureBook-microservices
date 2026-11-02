package com.pictet.bookservice.domain.unreachable;

import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.model.Option;
import com.pictet.bookservice.domain.model.Section;
import com.pictet.bookservice.domain.model.SectionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnreachableSectionsAnalyzerTest {

    private UnreachableSectionsAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new UnreachableSectionsAnalyzer();
    }

    @Test
    void shouldReturnEmptyListWhenAllSectionsAreReachable() {
        Book book = createBook(
                section(1, SectionType.BEGIN, option(20), option(30)),
                section(20, SectionType.NODE, option(100)),
                section(30, SectionType.NODE, option(100)),
                section(100, SectionType.END)
        );
        List<Integer> result = analyzer.findUnreachableSections(book);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldIdentifyUnreachableSections() {
        Book book = createBook(
                section(1, SectionType.BEGIN, option(20)),
                section(20, SectionType.NODE, option(100)),
                section(100, SectionType.END),
                section(200, SectionType.NODE, option(300)),
                section(300, SectionType.END)
        );

        List<Integer> result = analyzer.findUnreachableSections(book);
        assertEquals(List.of(200, 300), result);
    }

    @Test
    void shouldHandleCyclesWithoutInfiniteLoop() {
        Book book = createBook(
                section(1, SectionType.BEGIN, option(20)),
                section(20, SectionType.NODE, option(30)),
                section(30, SectionType.NODE, option(20), option(100)),
                section(100, SectionType.END)
        );
        List<Integer> result = analyzer.findUnreachableSections(book);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleMultipleEndingSections() {
        Book book = createBook(
                section(1, SectionType.BEGIN, option(20), option(30)),
                section(20, SectionType.END),
                section(30, SectionType.END),
                section(40, SectionType.END)
        );
        List<Integer> result = analyzer.findUnreachableSections(book);
        assertEquals(List.of(40), result);
    }

    private Book createBook(Section... sections) {
        return new Book(
                1L,
                "Test Book",
                "Test Author",
                List.of(sections)
        );
    }

    private Section section(
            int id,
            SectionType type,
            Option... options
    ) {
        return new Section(
                id,
                "Section " + id,
                type,
                List.of(options)
        );
    }

    private Option option(int targetSectionId) {
        return new Option(
                "Go to " + targetSectionId,
                1,
                targetSectionId,
                null
        );
    }
}