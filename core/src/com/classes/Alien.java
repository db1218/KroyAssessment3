package com.classes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Alien extends Sprite {

    public final AlienType type;
    public final int score;

    public Alien(AlienType type, Vector2 position) {
        super(type.getTexture());
        super.setPosition(position.x, position.y);
        this.type = type;
        this.score = type.getScore();
    }

    public AlienType getType() { return this.type; }

    public int getScore() { return this.score; }

    }


