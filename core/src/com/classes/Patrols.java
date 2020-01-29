package com.classes;

import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;


public class Patrols {

    MapGraph mapGraph;

    Circle circle;
    float x;
    float y;

    float deltaX;
    float deltaY;

    float speed;
    Junction previousJunction;
    Queue<Junction> pathQueue;

    public Patrols(Junction start, MapGraph mapGraph){
        this.mapGraph = mapGraph;

        this.x = start.getX();
        this.y = start.getY();
        this.speed = 2.0f;
        this.pathQueue = new Queue<>();
        this.previousJunction = start;

        this.circle = new Circle();
        this.circle.x = this.x;
        this.circle.y = this.y;
        this.circle.radius = 30;
        Junction goal = mapGraph.getJunctions().random();

        setGoal(goal);

        this.deltaX = 0;
        this.deltaY = 0f;
    }

    public Circle getCircle(){
        return this.circle;
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

    public void step() {
        this.x += deltaX;
        this.y += deltaY;
        this.circle.x = this.x;
        this.circle.y = this.y;
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
        Junction nextJunction = pathQueue.first();

        this.x = nextJunction.getX();
        this.y = nextJunction.getY();

        this.previousJunction = nextJunction;
        pathQueue.removeFirst();

        if (pathQueue.size == 0) {
            reachDestination();
        } else {
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
}





