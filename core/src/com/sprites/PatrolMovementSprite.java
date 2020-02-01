package com.sprites;

import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.PathFinding.Road;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.classes.Patrols;

import java.util.ArrayList;

public class PatrolMovementSprite extends SimpleSprite {

    MapGraph mapGraph;

    Road setRoad;

    Circle circle;

    float x;
    float y;

    float deltaX;
    float deltaY;
    private boolean collision;

    float speed;
    Junction previousJunction;
    Queue<Junction> pathQueue;
    ArrayList<Texture> textureSlices;
    private boolean yo;
    private Patrols whoSet;

    public PatrolMovementSprite(Texture spriteTexture, Junction start, MapGraph mapGraph){
        super(spriteTexture);
        this.mapGraph = mapGraph;
        Gdx.app.log("patrolSprite", String.valueOf(this));
        this.x = start.getX();
        this.y = start.getY();
        this.speed = 2.0f;
        this.pathQueue = new Queue<>();
        this.previousJunction = start;

        Junction goal = mapGraph.getJunctions().random();

        setGoal(goal);
        this.setRoad = mapGraph.hi(this.previousJunction, this.pathQueue.first());
        mapGraph.lockedRoads(this.setRoad, this);

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
   //     Gdx.app.log("previous Junction", String.valueOf(this.previousJunction.getName()) );
   //     Gdx.app.log("next Junction", String.valueOf(nextJunction.getName()));
   //     Gdx.app.log("this x", String.valueOf(this.x));
   //     Gdx.app.log("this y", String.valueOf(this.y));
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
        this.yo = false;
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
       // mapGraph.unsetTravelled(previousJunction, pathQueue.first());
       // mapGraph.unsetTravelled(this.setRoad);
        mapGraph.unlockRoad(this.setRoad,this);
        Junction nextJunction = pathQueue.first();
        this.x = nextJunction.getX();
        this.y = nextJunction.getY();

        this.previousJunction = nextJunction;
        pathQueue.removeFirst();

        if (pathQueue.size == 0) {
            reachDestination();
        } else if (mapGraph.isTravelled(nextJunction, pathQueue.first())){
            pathQueue.clear();
            reachDestination();
        } else {
           // mapGraph.setTravelled(nextJunction, pathQueue.first());
           // mapGraph.lockedRoads();
            this.setRoad = mapGraph.hi(nextJunction, pathQueue.first());
            mapGraph.lockedRoads(this.setRoad,  this);
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

        if (collision){
            Gdx.app.log("new goal", String.valueOf(newGoal.getName()));
        }

        setGoal(newGoal);
    }

    public float getX(){ return this.x; }

    public float getY(){ return this.y; }

    public Circle getCircle() {return this.circle; }


}
