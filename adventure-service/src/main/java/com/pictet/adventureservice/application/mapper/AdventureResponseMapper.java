package com.pictet.adventureservice.application.mapper;

import com.pictet.adventureservice.application.dto.AdventureStateResponse;
import com.pictet.adventureservice.domain.model.Adventure;
import com.pictet.adventureservice.infrastructure.client.book.dto.PlayableOption;
import com.pictet.adventureservice.infrastructure.client.book.dto.PlayableSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdventureResponseMapper {

    @Mapping(target = "id", source = "adventure.id")
    @Mapping(target = "playerName", source = "adventure.player.name")
    @Mapping(target = "health", source = "adventure.player.health")
    @Mapping(target = "currentSection", source = "playableSection")
    AdventureStateResponse toResponse(Adventure adventure, PlayableSection playableSection, String consequenceMessage);

    @Mapping(target = "choices", source = "options")
    AdventureStateResponse.CurrentSectionResponse toResponse(PlayableSection playableSection);

    @Mapping(target = "index", source = "optionIndex")
    AdventureStateResponse.ChoiceResponse toResponse(PlayableOption playableOption);

}
