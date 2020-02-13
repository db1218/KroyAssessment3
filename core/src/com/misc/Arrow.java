package com.misc;

/* =================================================================
                       New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Creates a polygon which looks like an arrow
 * which points to a target vector
 */
public class Arrow extends Polygon {

    /**
     * Constructs an arrow with the following parameters
     *
     * @param baseWidth     width of the stem of the arrow
     * @param headWidth     width of the pointy arrow head
     * @param baseHeight    height of the stem of the arrow
     * @param headHeight    height of the pointy arrow head
     */
    public Arrow(float baseWidth, float headWidth, float baseHeight, float headHeight) {
        if (baseWidth > headWidth) throw new IllegalArgumentException("The head must be wider than the base");
        float[] vertices = {0,0,baseWidth/2,0,baseWidth/2,baseHeight,headWidth/2,baseHeight,0,baseHeight+headHeight,-headWidth/2,baseHeight,-baseWidth/2,baseHeight,-baseWidth/2,0,0,0};
        setVertices(vertices);
    }

    /**
     * "Update" function which rotates the arrow head to
     * point to the the target
     * @param target    vector of target
     */
    public void aimAtTarget(Vector2 target) {
        Vector2 arrowPosition = new Vector2(this.getX(), this.getY());
        float theta = (float) (180f / Math.PI * Math.atan2(arrowPosition.x - target.x, target.y - arrowPosition.y));
        setRotation(theta);
    }
}
