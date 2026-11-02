package com.pictet.bookservice.infrastructure.json.importer;

import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.validation.BookValidationResult;
import com.pictet.bookservice.domain.validation.BookValidator;
import com.pictet.bookservice.infrastructure.json.mapper.BookJsonMapper;
import com.pictet.bookservice.infrastructure.json.model.BookJson;
import com.pictet.bookservice.infrastructure.persistence.entity.BookEntity;
import com.pictet.bookservice.infrastructure.persistence.mapper.BookPersistenceMapper;
import com.pictet.bookservice.infrastructure.persistence.repository.BookRepository;
import com.pictet.bookservice.infrastructure.persistence.repository.BookValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookJsonImporter {

    private static final String BOOKS_LOCATION = "classpath:books/*.json";

    private final ObjectMapper objectMapper;
    private final BookJsonMapper jsonMapper;
    private final BookRepository bookRepository;
    private final ResourcePatternResolver resourceResolver;
    private final BookPersistenceMapper bookPersistenceMapper;
    private final BookValidationRepository bookValidationRepository;

    @Transactional
    public void importBooks() {
        try {
            Resource[] resources = resourceResolver.getResources(BOOKS_LOCATION);
            BookValidator validator = new BookValidator();
            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    final BookJson json = objectMapper.readValue(inputStream, BookJson.class);
                    log.debug("Importing book with ID={}", json.id());
                    final Book book = jsonMapper.toDomain(json);
                    final Optional<BookEntity> existingBook = bookRepository.findOneById(book.id());
                    if (existingBook.isPresent()) {
                        final BookEntity entity = existingBook.get();
                        if (bookValidationRepository.findOneByBookId(entity.getId()).isEmpty()) {
                            log.debug("Book with ID {} is already valid, skipping recreate", entity.getId());
                            continue;
                        }
                        bookValidationRepository.deleteAllByBook(entity);
                        bookRepository.delete(entity);
                        bookRepository.flush();
                    }
                    final BookValidationResult validationResult = validator.validate(book);
                    final BookEntity savedBook = bookRepository.save(bookPersistenceMapper.toEntity(book));
                    if (!validationResult.errors().isEmpty()) {
                        bookValidationRepository.save(bookPersistenceMapper.toEntity(savedBook, validationResult));
                    }
                }
            }
        } catch (IOException exception) {
            log.error("Error importing json book", exception);
            throw new BookImportException(
                    "Failed to import books", exception);
        }
    }

}
