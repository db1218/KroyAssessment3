package com.PathFinding;

import com.badlogic.gdx.math.Vector2;

public class Junction {
    float x;
    float y;
    String name;

    int index;

    public Junction (float x, float y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public Vector2 getVector(){
        return new Vector2(x,y);
    }

    public String getName(){
        return this.name;
    }

}
