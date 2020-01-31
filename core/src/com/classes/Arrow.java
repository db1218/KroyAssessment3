package com.classes;


import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Arrow extends Polygon {

    public Arrow(float baseWidth, float headWidth, float baseHeight, float headHeight) {
        if (baseWidth > headWidth) throw new IllegalArgumentException("The head must be wider than the base");
        float[] vertices = {0,0,baseWidth/2,0,baseWidth/2,baseHeight,headWidth/2,baseHeight,0,baseHeight+headHeight,-headWidth/2,baseHeight,-baseWidth/2,baseHeight,-baseWidth/2,0,0,0};
        setVertices(vertices);
    }

    public void aimAtTarget(Vector2 target) {
        Vector2 arrowPosition = new Vector2(this.getX(), this.getY());
        float theta = (float) (180f / Math.PI * Math.atan2(arrowPosition.x - target.x, target.y - arrowPosition.y));
        setRotation(theta);
    }

}
