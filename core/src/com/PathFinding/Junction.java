package com.PathFinding;

import com.badlogic.gdx.math.Vector2;

public class Junction {
    float x;
    float y;
    String name;
    Vector2 location;

    int index;

    public Junction (float x, float y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
        this.location = new Vector2(x,y);
    }

    public void setIndex(int index){
        this.index = index;
    }

}
