package com.sprites;

import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.PathFinding.Road;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;


public class PatrolMovementSprite extends SimpleSprite {

    MapGraph mapGraph;
    Road setRoad;

    float x;
    float y;

    float deltaX;
    float deltaY;

    String name;

    float speed;
    Junction previousJunction;
    Queue<Junction> pathQueue;

    public PatrolMovementSprite(Texture spriteTexture, Junction start, MapGraph mapGraph, String name){
        super(spriteTexture);
        this.mapGraph = mapGraph;
        this.x = start.getX();
        this.y = start.getY();
        this.speed = 2.0f;
        this.pathQueue = new Queue<>();
        this.previousJunction = start;
        this.name = name;

        Junction goal = mapGraph.getJunctions().random();

        setGoal(goal);
        this.setRoad = mapGraph.hi(this.previousJunction, this.pathQueue.first());
        mapGraph.lockRoads(this.setRoad, this);

        this.deltaX = 0;
        this.deltaY = 0f;
    }

    public void update(Batch batch) {
        this.step();
        super.update(batch);
    }

    public void setGoal(Junction goal){
        GraphPath<Junction> junctionPath = mapGraph.findPath(previousJunction, goal);
        for (int i = 0; i < junctionPath.getCount(); i++){
            pathQueue.addLast(junctionPath.get(i));
        }
        setSpeedToNextCity();
    }

    private void setSpeedToNextCity(){
        Junction nextJunction = pathQueue.first();
        float angle = MathUtils.atan2(nextJunction.getY() - previousJunction.getY(), nextJunction.getX() - previousJunction.getX());
        deltaX = MathUtils.cos(angle) * this.speed;
        deltaY = MathUtils.sin(angle) * this.speed;
    }

    private void updateRotation(){
        Junction nextJunction = pathQueue.first();
        float angle = MathUtils.atan2(nextJunction.getY() - previousJunction.getY(), nextJunction.getX() - previousJunction.getX());
        double angleDEG = angle * (180/Math.PI);
        this.setRotation((float) angleDEG);
    }

    public void step() {
        this.x += deltaX;
        this.y += deltaY;
        checkCollision();
    }

    private void checkCollision() {
        if (pathQueue.size > 0) {
            Junction targetJunction = pathQueue.first();
            if (Vector2.dst(x, y, targetJunction.getX(), targetJunction.getY()) < 5) {
                reachNextJunction();
            }
        }
    }

    private void reachNextJunction() {
        mapGraph.unlockRoad(this.setRoad,this);
        Junction currentJunction = pathQueue.first();
        this.x = currentJunction.getX();
        this.y = currentJunction.getY();

        this.previousJunction = currentJunction;
        pathQueue.removeFirst();

        if (pathQueue.size == 0) {
            reachDestination();
        } else if (mapGraph.isRoadLocked(currentJunction, pathQueue.first())){
            Gdx.app.log("new goal for:", String.valueOf(this.name));
            Gdx.app.log("junction where goal created:", String.valueOf(currentJunction.getName()));
            pathQueue.clear();
            reachDestination();
        } else {
            this.setRoad = mapGraph.hi(currentJunction, pathQueue.first());
            mapGraph.lockRoads(this.setRoad,  this);
            updateRotation();
            setSpeedToNextCity();
        }
    }

    private void reachDestination(){
        deltaX = 0;
        deltaY = 0;
        Junction newGoal;

        do {
            newGoal = mapGraph.getJunctions().random();
        } while (newGoal == previousJunction);

        setGoal(newGoal);
    }

    public float getX(){ return this.x; }

    public float getY(){ return this.y; }

    public PatrolMovementSprite getThis(){ return this; }

    public String getName(){
        return this.name;
    }
    
}
