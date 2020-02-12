package com.classes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Alien extends Sprite {

    public final AlienType type;

    public Alien(AlienType type, Vector2 position) {
        super(type.getTexture());
        super.setPosition(position.x, position.y);
        this.type = type;
    }

    public int getScore() { return this.type.getScore(); }

    }


