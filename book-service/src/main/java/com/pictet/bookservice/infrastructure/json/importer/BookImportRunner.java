package com.pictet.bookservice.infrastructure.json.importer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookImportRunner implements ApplicationRunner {

    private final BookJsonImporter importer;

    @Override
    public void run(ApplicationArguments args) {
        importer.importBooks();
    }
}