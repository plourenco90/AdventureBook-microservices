package com.pictet.adventureservice.infrastructure.client.book.mapper;

import com.pictet.adventureservice.infrastructure.client.book.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayableBookMapper {


    PlayableBook toDomain(BookDetailsResponse book);

    PlayableSection toDomain(BookDetailsResponse.SectionResponse section);

    @Mapping(target = "targetSectionId", source = "goToId")
    PlayableOption toDomain(BookDetailsResponse.OptionResponse option);

    PlayableConsequence toDomain(BookDetailsResponse.ConsequenceResponse consequence);
}
