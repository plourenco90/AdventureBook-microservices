package com.pictet.adventureservice.domain.model;

import lombok.Getter;

@Getter
public class Player {

    private static final int INITIAL_HEALTH = 10;

    private final String name;
    private int health;

    public Player(String name) {
        this.name = name;
        this.health = INITIAL_HEALTH;
    }

    public static Player restore(String name, int health) {
        Player player = new Player(name);
        player.health = health;
        return player;
    }

    public void loseHealth(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Health loss cannot be negative");
        }
        health = Math.max(0, health - amount);
    }

    public boolean isDead() {
        return health <= 0;
    }
}