package com.sprites;

/* =================================================================
   New class added for assessment 3
   ===============================================================*/

import com.pathFinding.Junction;
import com.pathFinding.MapGraph;
import com.pathFinding.Road;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

/** This class is the super class for Patrol and is used to
 * move the patrol around the map. It extends SimpleSprite
 * which gives it a damageHitbox, healthbar and allows you
 * to rotate the patrol
 */

public class PatrolMovementSprite extends SimpleSprite {

    // The mapGraph that contains all the junctions in the map
    final MapGraph mapGraph;
    // The road the patrol is currently travelling on
    Road setRoad;

    // The patrol's x position
    float x;
    // The patrol's y position
    float y;

    // How much the patrol should move each frame in the x direction
    float deltaX;
    // How much the patrol should move each frame in the y direction
    float deltaY;

    // The speed that the patrols should move
    final float speed;

    // The junction that the patrol has just moved away from
    Junction previousJunction;
    // Queue of junctions that the patrol will travel through to get
    // from its start position to it's goal position
    final Queue<Junction> pathQueue;

    /** Constructor for PatrolMovementSprite
     *
     * @param spriteTexture  The texture for the PatrolMovementSprite
     * @param mapGraph       mapGraph that contains all the junctions in the map
     */
    public PatrolMovementSprite(Texture spriteTexture, MapGraph mapGraph){
        super(spriteTexture);

        this.mapGraph = mapGraph;
        this.pathQueue = new Queue<>();

        // Generates a random start and end position each time you start the game
        Junction start = mapGraph.getJunctions().random();
        Junction goal = mapGraph.getJunctions().random();

        this.x = start.getX();
        this.y = start.getY();

        this.deltaX = 0f;
        this.deltaY = 0f;
        this.speed = 2.0f;

        this.previousJunction = start;

        setGoal(goal);

        /* The road it is travelling on is the road between the junction it will start
        moving from (here it's it start junction) and the next junction it is moving to
        (here it's the first junction in pathQueue) */
        this.setRoad = mapGraph.getRoad(this.previousJunction, this.pathQueue.first());

        /* Calls lockRoad in mapGraph which adds the road it is travelling on to the hashmap
        lockedRoads in mapGraph so another patrol doesn't travel on this road at the same
        time */
        mapGraph.lockRoad(this.setRoad, this);
    }

    /** Called each frame, it updates the patrols position
     *
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        this.step();
        super.update(batch);
    }

    /**
     * This generates the queue of junctions the patrol will have to travel through
     * to go from it's start position to it's goal position, once it's created a path
     * it calls setSpeedToNextCity
     * @param goal node to end up at
     */
    public void setGoal(Junction goal){
        GraphPath<Junction> junctionPath = mapGraph.findPath(previousJunction, goal);
        for (int i = 0; i < junctionPath.getCount(); i++){
            pathQueue.addLast(junctionPath.get(i));
        }
        setSpeedToNextJunction();
    }

    /**
     * This calculates the speed at which the patrol should move each frame to go from
     * the junction it is at (previousJunction) to the next junction it should go to.
     */
    private void setSpeedToNextJunction(){
        Junction nextJunction = pathQueue.first();
        float angle = MathUtils.atan2(nextJunction.getY() - previousJunction.getY(), nextJunction.getX() - previousJunction.getX());
        deltaX = MathUtils.cos(angle) * this.speed;
        deltaY = MathUtils.sin(angle) * this.speed;
    }

    /**
     * This updates the rotation of the patrol so it looks like it is facing the
     * direction it is moving in. First calculates this in radians (angle) then
     * converts it into degrees (angleDeg).
     */
    private void updateRotation(){
        Junction nextJunction = pathQueue.first();
        float angle = MathUtils.atan2(nextJunction.getY() - previousJunction.getY(),
                                      nextJunction.getX() - previousJunction.getX());
        double angleDEG = angle * (180/Math.PI);
        this.setRotation((float) angleDEG);
    }

    /**
     * This is called each frame and is what makes the patrol look like it
     * is moving by updating it's x and y position by an amount set by
     * setSpeedToNextJunction.
     */
    public void step() {
        this.x += deltaX;
        this.y += deltaY;
        atNextJunction();
    }

    /**
     * Checks if the patrol has reached the junction at the end of the road it
     * is travelling on - if it has then it calls reachNextJunction().
     */
    private void atNextJunction() {
        if (pathQueue.size > 0) {
            Junction targetJunction = pathQueue.first();
            if (Vector2.dst(x, y, targetJunction.getX(), targetJunction.getY()) < 5) {
                reachNextJunction();
            }
        }
    }

    /**
     * This controls the patrol's pathQueue and determines whether
     * the patrol has reached it's destination and should be set a
     * new goal or if it can travel onto the next junction in it's
     * pathQueue
     */
    private void reachNextJunction() {
        // this removes the road the patrol was travelling on
        // from lockedRoads in mapGraph to allow another patrol
        // to be able to travel on it
        mapGraph.unlockRoad(this.setRoad,this);

        Junction currentJunction = pathQueue.first();
        this.x = currentJunction.getX();
        this.y = currentJunction.getY();

        this.previousJunction = currentJunction;
        pathQueue.removeFirst();

        if (pathQueue.size == 0) { // if it has reached it's goal state
            reachDestination();
        } else if (mapGraph.isRoadLocked(currentJunction, pathQueue.first())){
            // If the road the patrol wants to travel on is locked (another patrol
            // is on it) then terminate it's path and give it a new goal
            pathQueue.clear();
            reachDestination();
        } else { // the patrol can travel on the next road it wants to travel on
            this.setRoad = mapGraph.getRoad(currentJunction, pathQueue.first());
            mapGraph.lockRoad(this.setRoad,  this);
            updateRotation();
            setSpeedToNextJunction();
        }
    }

    /** Gives the patrol a new random goal then calls SetGoal to
     * start generating a path from the patrols current position
     * to it's new randomly generated goal
     */
    private void reachDestination(){
        deltaX = 0;
        deltaY = 0;
        Junction newGoal;

        do {
            newGoal = mapGraph.getJunctions().random();
        } while (newGoal == previousJunction);
        setGoal(newGoal);
    }

    public float getX() {
        return this.x - (this.getWidth() / 2);
    }

    public float getY() {
        return this.y - (this.getHeight() / 2);
    }

    public Junction getGoal() {return this.pathQueue.last();}

    public PatrolMovementSprite getThis(){ return this; }
    
}
