package com.pictet.adventureservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void newPlayerStartsWithTenHealth() {
        Player player = new Player("John");

        assertEquals(10, player.getHealth());
        assertFalse(player.isDead());
    }

    @Test
    void damageReducesHealth() {
        Player player = new Player("John");

        player.loseHealth(4);

        assertEquals(6, player.getHealth());
    }

    @Test
    void lethalDamageKillsPlayer() {
        Player player = new Player("John");

        player.loseHealth(10);

        assertEquals(0, player.getHealth());
        assertTrue(player.isDead());
    }

    @Test
    void healthNeverBecomesNegative() {
        Player player = new Player("John");

        player.loseHealth(100);

        assertEquals(0, player.getHealth());
    }

    @Test
    void negativeDamageIsRejected() {
        Player player = new Player("John");

        assertThrows(
                IllegalArgumentException.class,
                () -> player.loseHealth(-1)
        );
    }

    @Test
    void persistedPlayerCanBeRestored() {
        Player player = Player.restore("John", 3);

        assertEquals("John", player.getName());
        assertEquals(3, player.getHealth());
    }
}
