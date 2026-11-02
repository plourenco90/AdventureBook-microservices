package com.pictet.bookservice.infrastructure.json.mapper;

import com.pictet.bookservice.domain.model.*;
import com.pictet.bookservice.infrastructure.json.model.BookJson;
import com.pictet.bookservice.infrastructure.json.model.ConsequenceJson;
import com.pictet.bookservice.infrastructure.json.model.OptionJson;
import com.pictet.bookservice.infrastructure.json.model.SectionJson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public interface BookJsonMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "sections", source = "sections")
    Book toDomain(BookJson source);

    @Mapping(target = "id", source = "id", qualifiedByName = "toInteger")
    @Mapping(target = "type", source = "type", qualifiedByName = "toSectionType")
    Section toDomain(SectionJson source);

    @Mapping(target = "optionIndex", source = "index")
    @Mapping(target = "targetSectionId", source = "source.gotoId", qualifiedByName = "toInteger")
    Option toDomain(OptionJson source, int index);

    default List<Option> toDomainList(List<OptionJson> sourceList) {
        if (sourceList == null) {
            return null;
        }
        return IntStream.range(0, sourceList.size())
                .mapToObj(i -> toDomain(sourceList.get(i), i))
                .toList();
    }

    @Mapping(target = "type", source = "type", qualifiedByName = "toConsequenceType")
    @Mapping(target = "value", source = "value", qualifiedByName = "toInteger")
    Consequence toDomain(ConsequenceJson source);

    @Named("toInteger")
    default Integer toInteger(JsonNode value) {
        if (value == null || value.isNull()) {
            return null;
        }
        return Integer.valueOf(value.asString());
    }

    @Named("toSectionType")
    default SectionType toSectionType(String value) {
        return value == null ? null : SectionType.valueOf(value);
    }

    @Named("toConsequenceType")
    default ConsequenceType toConsequenceType(String value) {
        return value == null ? null : ConsequenceType.valueOf(value);
    }

    @Named("toInteger")
    default Integer toInteger(String value) {
        return !StringUtils.hasLength(value) ? null : Integer.valueOf(value);
    }

}
