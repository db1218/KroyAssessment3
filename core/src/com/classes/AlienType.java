package com.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum AlienType {

    green("green",10, 0.5, new Texture(Gdx.files.internal("Minigame/aliensquare.png")), 3000),
    red("red",30, 0.3, new Texture(Gdx.files.internal("Minigame/redalien.png")), 2000),
    blue("blue", 50,0.2, new Texture(Gdx.files.internal("Minigame/bluealien.png")), 1000);

    private String name;
    private int score;
    private double chance;
    private long aliveTime;

    private Texture texture;

    AlienType(String name, int score, double chance, Texture texture, long aliveTime){
        this.name = name;
        this.score = score;
        this.chance = chance;
        this.aliveTime = aliveTime;
        this.texture = texture;
    }

    public String getName(){return this.name;}

    public int getScore(){return this.score;}

    public double getChance() {return this.chance; }

    public Texture getTexture(){ return this.texture; }

    public long getAliveTime() {
        return aliveTime;
    }

}
