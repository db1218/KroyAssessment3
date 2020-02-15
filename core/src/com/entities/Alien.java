package com.entities;

/*
 *  =======================================================================
 *                    New class added for Assessment 3
 *  =======================================================================
 */

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.misc.Constants.*;

/**
 * Alien is a really simple sprite which also
 * keeps track of how long it has been alive,
 * and gives it certain properties depending
 * on {@link AlienType}
 */
public class Alien extends Sprite {

    // type gives certain properties
    public final AlienType type;

    // time alien was spawned
    private final long spawnTime;

    /**
     * Constructor for Alien, which is called
     * to spawn at a random position, remains
     * there for a certain amount of time, and
     * gives a certain amount of score depending
     * on the type
     *
     * @param type      gives properties of the alien
     * @param position  where it spawns on the screen
     */
    public Alien(AlienType type, Vector2 position) {
        super(type.getTexture());
        super.setSize(100, 100);
        super.setPosition(position.x, position.y);
        this.type = type;
        this.spawnTime = TimeUtils.millis();
    }

    public int getScore() { return this.type.getScore(); }

    public long getSpawnTime() {
        return this.spawnTime;
    }
}


