package com.pathFinding;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;

/** This class creates a road between two Junctions and
 *  calculates a cost for travelling on this road
 */

public class Road implements Connection<Junction> {

    final Junction fromJunction;
    final Junction toJunction;

    // The cost of going from fromJunction to toJunction -
    // the cost is determined by the distance between fromJunction
    // and toJunction
    final float cost;

    /** Constructs a road
     *
     * @param fromJunction  One end of the road - the end that a patrol will start at
     * @param toJunction    The other end of the road - the junction the patrol will end at
     */
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
