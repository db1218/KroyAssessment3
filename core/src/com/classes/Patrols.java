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

    public Patrols(Junction starts, MapGraph mapGraph){
        this.mapGraph = mapGraph;
       // Junction start = mapGraph.getJunctions().get(5);
        Junction start = mapGraph.getJunctions().get(2);
        Gdx.app.log("start", String.valueOf(start.getName()));
        this.x = start.getX();
        this.y = start.getY();
     //   Gdx.app.log("start x ", String.valueOf(start.getX()));
     //   Gdx.app.log("start y ", String.valueOf(start.getY()));
        this.speed = 2.0f;
        this.pathQueue = new Queue<>();
        this.previousJunction = start;

        this.circle = new Circle();
        this.circle.x = this.x;
        this.circle.y = this.y;
        this.circle.radius = 30;
       // Junction goal = mapGraph.getJunctions().random();
        Junction goal = mapGraph.getJunctions().get(0);
       // setGoal(mapGraph.getJunctions().get(12));
        Gdx.app.log("goal", String.valueOf(goal.getName()));

        setGoal(goal);
       // reachDestination();

        this.deltaX = 0;
        this.deltaY = 0f;
    }

    public Circle getCircle(){
        return this.circle;
    }


    public void setGoal(Junction goal){
       // Junction goal = mapGraph.getJunctions().random();
        Gdx.app.log("previous Junction", String.valueOf(previousJunction.getName()));

        GraphPath<Junction> junctionPath = mapGraph.findPath(previousJunction, goal);

        for (int i = 0; i < junctionPath.getCount(); i++){
            pathQueue.addLast(junctionPath.get(i)); //- here just getting like the first junction then the second etc.
        }
        for (Junction junction : pathQueue){
           // Gdx.app.log("junction", String.valueOf(junction.getX()));
            Gdx.app.log("junction", String.valueOf(junction.getName()));
        }
        setSpeedToNextCity();
    }

    private void setSpeedToNextCity(){
        //pathQueue.removeFirst();
        Junction nextJunction = pathQueue.first();
        Gdx.app.log("previous city", String.valueOf(previousJunction.getName()));
        Gdx.app.log("next city", String.valueOf(nextJunction.getName()));
       // Float distance = previousJunction.getVector().dst(nextJunction.getVector());
        float angle = MathUtils.atan2(nextJunction.getY() - previousJunction.getY(), nextJunction.getX() - previousJunction.getX());
        deltaX = MathUtils.cos(angle) * this.speed;
        deltaY = MathUtils.sin(angle) * this.speed;
    }

    public void step() {
       // Gdx.app.log("stepped", "step");
        this.x += deltaX;
        this.y += deltaY;
        this.circle.x = this.x;
        this.circle.y = this.y;
        checkCollision();
    }

    private void checkCollision() {
        Gdx.app.log("checked collision", "yes");
        if (pathQueue.size > 0) {
            Junction targetJunction = pathQueue.first();
            if (Vector2.dst(x, y, targetJunction.getX(), targetJunction.getY()) < 5) {
                reachNextJunction();
            }
        }
    }

    private void reachNextJunction() {
        Gdx.app.log("Reached the next junction", "yes");
        Junction nextJunction = pathQueue.first();

        this.x = nextJunction.getX();
        this.y = nextJunction.getY();

        this.previousJunction = nextJunction;
        pathQueue.removeFirst();
        for(Junction junction : pathQueue){
            Gdx.app.log("rest of queue", String.valueOf(junction.getName()));
        }

        if (pathQueue.size == 0) {
            reachDestination();
        } else {
            Gdx.app.log("set speed to next city", "yes");
            setSpeedToNextCity();
        }
    }

    private void reachDestination(){
        Gdx.app.log("reached desintionation", "yes");
        deltaX = 0;
        deltaY = 0;

        Junction newGoal;

        do {
            newGoal = mapGraph.getJunctions().random();
        } while (newGoal == previousJunction);

        setGoal(newGoal);
        }
}





