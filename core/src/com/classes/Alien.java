package com.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Alien extends Sprite {

    public final AlienType type;
    public final String name;


    public Alien(Texture texture, AlienType type, int xPos, int yPos, String name) {
        super(texture);
        super.setPosition(xPos, yPos);
        this.type = type;
        this.name = name;

    }

    public AlienType getType() { return this.type; }

    public String getName() {
        return name;
    }


    }


