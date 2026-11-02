package com.pictet.bookservice.infrastructure.persistence.mapper;

import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.model.Consequence;
import com.pictet.bookservice.domain.model.Option;
import com.pictet.bookservice.domain.model.Section;
import com.pictet.bookservice.domain.validation.BookValidationError;
import com.pictet.bookservice.domain.validation.BookValidationResult;
import com.pictet.bookservice.infrastructure.persistence.entity.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookPersistenceMapper {

    @Mapping(target = "sections", ignore = true)
    BookEntity mapBook(Book source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "sectionId", source = "id")
    SectionEntity mapSection(Section source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "section", ignore = true)
    @Mapping(target = "optionIndex", ignore = true)
    @Mapping(target = "consequence", ignore = true)
    OptionEntity mapOption(Option source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "option", ignore = true)
    OptionConsequenceEntity mapConsequence(Consequence source);

    @Mapping(target = "id", source = "sectionId")
    Section mapSection(SectionEntity source);

    @Mapping(target = "targetSectionId", source = "targetSectionId")
    Option mapOption(OptionEntity source);

    Consequence mapConsequence(OptionConsequenceEntity source);

    Book toDomain(BookEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", source = "book")
    BookValidationEntity toEntity(BookEntity book, BookValidationResult bookValidationResult);

    BookValidationErrorEntity toEntity(BookValidationError bookValidationError);

    default BookEntity toEntity(Book source) {
        BookEntity bookEntity = mapBook(source);

        source.sections().forEach(section -> {
            SectionEntity sectionEntity = mapSection(section);
            bookEntity.addSection(sectionEntity);

            for (int i = 0; i < section.options().size(); i++) {
                Option option = section.options().get(i);
                OptionEntity optionEntity = mapOption(option);
                optionEntity.setOptionIndex(i);
                sectionEntity.addOption(optionEntity);

                if (option.consequence() != null) {
                    optionEntity.addConsequence(mapConsequence(option.consequence()));
                }
            }
        });
        return bookEntity;
    }

    @AfterMapping
    default void linkValidationErrors(@MappingTarget BookValidationEntity validation) {
        if (validation.getErrors() == null) {
            return;
        }
        validation.getErrors().forEach(error -> error.setValidation(validation));
    }

}
