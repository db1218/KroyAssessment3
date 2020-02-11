package com.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Aliens extends Sprite {

    public final AlienType type;
    public final int xPos;
    public final int yPos;
    public final String name;


    public Aliens(Texture texture, AlienType type, int xPos, int yPos, String name) {
        super(texture);
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;

    }

    public AlienType getType() { return this.type; }

    public int getxPos() { return xPos; }
    public int getyPos() { return yPos; }

    public String getName(){return name;}


    }


