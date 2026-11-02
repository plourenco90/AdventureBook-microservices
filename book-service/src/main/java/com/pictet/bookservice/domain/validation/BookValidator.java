package com.pictet.bookservice.domain.validation;

import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.model.Section;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookValidator {

    public BookValidationResult validate(Book book) {
        final List<BookValidationError> errors = new ArrayList<>();

        errors.addAll(validateBeginning(book));
        errors.addAll(validateEndings(book));
        errors.addAll(validateDuplicateSectionIds(book));
        errors.addAll(validateOptions(book));
        errors.addAll(validateTargetSections(book));

        return new BookValidationResult(errors);
    }

    private List<BookValidationError> validateBeginning(Book book) {
        final List<BookValidationError> errors = new ArrayList<>();
        final long beginningCount = book.sections().stream().filter(Section::isBeginning).count();

        if (beginningCount == 0) {
            errors.add(new BookValidationError("MISSING_BEGINNING",
                    "Book must contain exactly one beginning section.",
                    null));
        } else if (beginningCount > 1) {
            errors.add(new BookValidationError("MULTIPLE_BEGINNINGS",
                    "Book cannot contain more than one beginning section.",
                    null));
        }
        return errors;
    }

    private List<BookValidationError> validateEndings(Book book) {
        final List<BookValidationError> errors = new ArrayList<>();
        final boolean hasEnding = book.sections().stream().anyMatch(Section::isEnding);

        if (!hasEnding) {
            errors.add(new BookValidationError("MISSING_ENDING",
                    "Book must contain at least one ending section.",
                    null));
        }
        return errors;
    }

    private List<BookValidationError> validateDuplicateSectionIds(Book book) {
        Set<Integer> sectionIds = new HashSet<>();
        return book.sections()
                .stream()
                .filter(section -> !sectionIds.add(section.id()))
                .map(duplicateSection -> new BookValidationError("DUPLICATE_SECTION_ID",
                        "Section ID " + duplicateSection.id() + " is duplicated.",
                        duplicateSection.id()))
                .toList();
    }

    private List<BookValidationError> validateOptions(Book book) {
        return book.sections()
                .stream()
                .filter(section -> !section.isEnding() && section.options().isEmpty())
                .map(invalidSection -> new BookValidationError("MISSING_OPTIONS",
                        "Non-ending section must contain at least one option.",
                        invalidSection.id()))
                .toList();
    }

    private List<BookValidationError> validateTargetSections(Book book) {
        final Set<Integer> sectionIds = book.sections().stream().map(Section::id).collect(Collectors.toSet());

        return book.sections()
                .stream()
                .flatMap(section -> section.options()
                        .stream()
                        .filter(option -> !sectionIds.contains(option.targetSectionId()))
                        .map(option -> new BookValidationError("INVALID_NEXT_SECTION",
                                "Option references non-existing section " + option.targetSectionId() + ".",
                                section.id())))
                .toList();
    }
}
