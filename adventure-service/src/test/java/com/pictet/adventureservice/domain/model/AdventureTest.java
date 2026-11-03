
package com.pictet.adventureservice.domain.model;

import com.pictet.adventureservice.domain.exception
        .AdventureAlreadyFinishedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdventureTest {

    private Adventure newAdventure() {
        return new Adventure(
                null, 1L, new Player("John"), 1);
    }

    @Test
    void newAdventureIsActive() {
        Adventure adventure = newAdventure();

        assertEquals(AdventureStatus.ACTIVE,
                adventure.getStatus());
        assertEquals(1, adventure.getCurrentSectionId());
    }

    @Test
    void canMoveToAnotherSection() {
        Adventure adventure = newAdventure();

        adventure.moveTo(2);

        assertEquals(2, adventure.getCurrentSectionId());
    }

    @Test
    void canCompleteAdventure() {
        Adventure adventure = newAdventure();

        adventure.complete();

        assertEquals(AdventureStatus.COMPLETED,
                adventure.getStatus());
    }

    @Test
    void canDie() {
        Adventure adventure = newAdventure();

        adventure.die();

        assertEquals(AdventureStatus.DEAD,
                adventure.getStatus());
    }

    @Test
    void completedAdventureCannotContinue() {
        Adventure adventure = newAdventure();
        adventure.complete();

        assertThrows(
                AdventureAlreadyFinishedException.class,
                () -> adventure.moveTo(3)
        );
    }

    @Test
    void deadAdventureCannotContinue() {
        Adventure adventure = newAdventure();
        adventure.die();

        assertThrows(
                AdventureAlreadyFinishedException.class,
                () -> adventure.moveTo(3)
        );
    }

    @Test
    void canRestoreCompletedAdventure() {
        Adventure adventure = Adventure.restore(
                10L,
                1L,
                Player.restore("John", 5),
                3,
                AdventureStatus.COMPLETED
        );

        assertEquals(AdventureStatus.COMPLETED,
                adventure.getStatus());
        assertEquals(3, adventure.getCurrentSectionId());
        assertEquals(5, adventure.getPlayer().getHealth());
    }
}
