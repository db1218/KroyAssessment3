package com.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.config.Constants.*;

public class Alien extends Sprite {

    public final AlienType type;
    private final long spawnTime;

    public Alien(AlienType type, Vector2 position) {
        super(type.getTexture());
        super.setPosition(position.x, position.y);
        this.type = type;
        this.spawnTime = TimeUtils.millis();
    }

    public int getScore() { return this.type.getScore(); }

    public long getSpawnTime() {
        return this.spawnTime;
    }
}


