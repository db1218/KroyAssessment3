package com.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector;


public enum AlienType {

    red(3,  "red", 435, 435, "alienSquare.png"),
    green(1, "green", 4, 2, "redalien.png"),
    blue(5, "blue", 200, 200,"bluealien.png");

    public int height;
    public int width;
    private int score;
    private String imageName;

    private String name;


    AlienType(int score, String name, int height, int width, String image){
        this.score = score;

        this.name = name;
        this.height = height;
        this.width = width;
        this.imageName = image;

    }

    public int getScore(){return this.score;}

    public String getImageName(){return imageName;}

    public String getName(){return name;}

}
