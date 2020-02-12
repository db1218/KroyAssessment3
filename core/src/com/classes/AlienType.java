package com.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum AlienType {

    red("red",30, 0.3, new Texture(Gdx.files.internal("Minigame/redalien.png"))),
    green("green",10, 0.5, new Texture(Gdx.files.internal("Minigame/aliensquare.png"))),
    blue("blue", 50,0.2, new Texture(Gdx.files.internal("Minigame/bluealien.png")));

    private String name;
    private int score;
    private double chance;

    private Texture texture;

    AlienType(String name, int score, double chance, Texture texture){
        this.name = name;
        this.score = score;
        this.chance = chance;

        this.texture = texture;
    }

    public String getName(){return this.name;}

    public int getScore(){return this.score;}

    public double getChance() {return this.chance; }

    public Texture getTexture(){ return this.texture; }

}
