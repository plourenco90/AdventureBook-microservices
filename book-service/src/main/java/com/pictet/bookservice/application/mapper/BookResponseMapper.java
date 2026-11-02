package com.pictet.bookservice.application.mapper;

import com.pictet.bookservice.application.dto.BookResponse;
import com.pictet.bookservice.application.dto.BookValidationResponse;
import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.model.Option;
import com.pictet.bookservice.infrastructure.persistence.entity.BookValidationErrorEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    @IterableMapping(elementTargetType = BookValidationResponse.BookValidationErrorResponse.class)
    List<BookValidationResponse.BookValidationErrorResponse> toResponse(List<BookValidationErrorEntity> bookValidationError);

    BookValidationResponse.BookValidationErrorResponse toResponse(BookValidationErrorEntity bookValidationError);

    BookResponse toResponse(Book book);
    
    @Mapping(target = "goToId", source = "targetSectionId")
    BookResponse.OptionResponse toResponse(Option option);
}
