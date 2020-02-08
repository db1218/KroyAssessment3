package com.pathFinding;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

/** This class creates the Heuristic that should be used when
 *  finding a path
 */

public class MapHeuristic implements Heuristic<Junction> {
    @Override
    public float estimate(Junction currentNode, Junction nextNode) {
        return Vector2.dst(currentNode.x, currentNode.y, nextNode.x, nextNode.y);
    }
}
