package com.pictet.adventureservice.domain.model;


import com.pictet.adventureservice.domain.exception.AdventureAlreadyFinishedException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Adventure {

    private final Long id;
    private final Long bookId;
    private final Player player;

    private Integer currentSectionId;
    private AdventureStatus status;

    public Adventure(Long id, Long bookId, Player player, Integer currentSectionId) {
        this.id = id;
        this.bookId = Objects.requireNonNull(bookId);
        this.player = Objects.requireNonNull(player);
        this.currentSectionId = Objects.requireNonNull(currentSectionId);
        this.status = AdventureStatus.ACTIVE;
    }

    public static Adventure restore(Long id, Long bookId, Player player, Integer currentSectionId, AdventureStatus status) {
        Adventure adventure = new Adventure(id, bookId, player, currentSectionId);
        adventure.status = Objects.requireNonNull(status);
        return adventure;
    }

    public void moveTo(Integer sectionId) {
        ensureActive();
        this.currentSectionId = Objects.requireNonNull(sectionId);
    }

    public void complete() {
        ensureActive();
        this.status = AdventureStatus.COMPLETED;
    }

    public void die() {
        ensureActive();
        this.status = AdventureStatus.DEAD;
    }

    public void ensureActive() {
        if (status != AdventureStatus.ACTIVE) {
            throw new AdventureAlreadyFinishedException(id);
        }
    }
}
