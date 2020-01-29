package com.classes;

import com.PathFinding.GenerateGraph;
import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

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

    public Patrols(MapGraph mapGraph){
        this.mapGraph = mapGraph;
        Junction start = mapGraph.getJunctions().get(5);
        Gdx.app.log("start", String.valueOf(start.getName()));
        this.x = start.getX();
        this.y = start.getY();
        Gdx.app.log("start x ", String.valueOf(start.getX()));
        Gdx.app.log("start y ", String.valueOf(start.getY()));
        this.speed = 2.0f;
        this.pathQueue = new Queue<>();
        this.previousJunction = start;

        this.circle = new Circle();
        this.circle.x = this.x;
        this.circle.y = this.y;
        this.circle.radius = 30;

        setGoal(mapGraph.getJunctions().get(12));
       // reachDestination();

        this.deltaX = 0.3f;
        this.deltaY = 0.4f;
    }

    public Circle getCircle(){
        return this.circle;
    }


    public void setGoal(Junction goal){
       // Junction goal = mapGraph.getJunctions().random();
        Gdx.app.log("previous Junction", String.valueOf(previousJunction.getName()));

        GraphPath<Junction> junctionPath = mapGraph.findPath(previousJunction, goal);
        pathQueue.addLast(previousJunction);
        for (int i = 0; i < junctionPath.getCount(); i++){
            pathQueue.addLast(mapGraph.getJunction(i));
        }
        for (Junction junction : pathQueue){
            Gdx.app.log("junction", String.valueOf(junction.getX()));
            Gdx.app.log("junction", String.valueOf(junction.getName()));
        }
        setSpeedToNextCity();
    }

    private void setSpeedToNextCity(){
        Junction nextJunction = pathQueue.first();
       // Float distance = previousJunction.getVector().dst(nextJunction.getVector());
        float angle = MathUtils.atan2(nextJunction.getY() - previousJunction.getY(), nextJunction.getX() - previousJunction.getX());
     //   deltaX = MathUtils.cos(angle) * this.speed;
      //  deltaY = MathUtils.sin(angle) * this.speed;
    }

    public void step() {
        x += deltaX;
        y += deltaY;
        checkCollision();
    }

    private void checkCollision() {
        if (pathQueue.size > 0) {
            Junction targetJunction = pathQueue.first();
            if (Vector2.dst(x, y, targetJunction.getX(), targetJunction.getY()) < 1) {
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





