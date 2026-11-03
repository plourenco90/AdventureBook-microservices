package com.pictet.adventureservice.infrastructure.persistence.mapper;

import com.pictet.adventureservice.domain.model.Adventure;
import com.pictet.adventureservice.domain.model.Player;
import com.pictet.adventureservice.infrastructure.persistence.entity.AdventureEntity;
import com.pictet.adventureservice.infrastructure.persistence.entity.PlayerEmbeddable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdventurePersistenceMapper {


    @Mapping(target = "id", ignore = true)
    AdventureEntity toEntity(Adventure adventure);

    PlayerEmbeddable toEmbeddable(Player player);

    default Adventure toDomain(AdventureEntity entity) {
        Player player = toDomain(entity.getPlayer());
        return Adventure.restore(entity.getId(),
                entity.getBookId(),
                player,
                entity.getCurrentSectionId(),
                entity.getStatus());
    }

    default Player toDomain(PlayerEmbeddable entity) {
        return Player.restore(entity.getName(), entity.getHealth());
    }

    void update(Adventure adventure, @MappingTarget AdventureEntity entity);

    @Mapping(target = "player", expression = "java(new PlayerEmbeddable(adventure.getPlayer().getName(), adventure.getPlayer().getHealth()))")
    void updateFrom(Adventure adventure, @MappingTarget AdventureEntity entity);

}
