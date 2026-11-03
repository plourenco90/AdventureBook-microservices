package com.pictet.bookservice.persistence;

import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.model.Option;
import com.pictet.bookservice.domain.model.Section;
import com.pictet.bookservice.domain.model.SectionType;
import com.pictet.bookservice.infrastructure.persistence.entity.BookEntity;
import com.pictet.bookservice.infrastructure.persistence.mapper.BookPersistenceMapper;
import com.pictet.bookservice.infrastructure.persistence.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public class BookPersistenceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookPersistenceMapper mapper;

    @Test
    void shouldPersistAndRetrieveBook() {
        Book book = new Book(999L,
                "Test Book",
                "Test Author",
                List.of(new Section(1, "Beginning", SectionType.BEGIN, List.of(new Option("Continue", 0, 2, null))),
                        new Section(2, "Ending", SectionType.END, List.of())));
        repository.save(mapper.toEntity(book));

        BookEntity persisted = repository.findOneById(999L)
                .orElseThrow();

        Book result = mapper.toDomain(persisted);

        assertEquals(book.id(), result.id());
        assertEquals(book.title(), result.title());
        assertEquals(book.author(), result.author());
        assertEquals(2, result.sections().size());
        assertEquals(1, result.sections().get(0).options().size());
        assertEquals(2, result.sections().get(0).options()
                .get(0).targetSectionId());
    }
}
