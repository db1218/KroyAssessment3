package com.PathFinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;

public class Road implements Connection<Junction> {

    Junction fromJunction;
    Junction toJunction;
    float cost;

    public Road(Junction fromJunction, Junction toJunction){
        this.fromJunction = fromJunction;
        this.toJunction = toJunction;
        cost = Vector2.dst(fromJunction.x, fromJunction.y, toJunction.x, toJunction.y);
    }

    @Override
    public float getCost() {
        return this.cost;
    }

    @Override
    public Junction getFromNode() {
        return this.fromJunction;
    }

    @Override
    public Junction getToNode() {
        return this.toJunction;
    }
}
