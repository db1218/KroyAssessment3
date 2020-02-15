package com.entities;

import com.badlogic.gdx.math.Vector2;
import com.misc.Arrow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ArrowTest {

    private Arrow arrowUnderTest;

    @Before
    public void setUp() {
        arrowUnderTest = new Arrow(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Test
    public void testAimAtTarget() {
        final Vector2 target = new Vector2(10.0f, 15.0f);
        arrowUnderTest.aimAtTarget(target);
        float theta = (float) (180f / Math.PI * Math.atan2(arrowUnderTest.getX() - target.x, target.y - arrowUnderTest.getY()));
        assertEquals(theta, arrowUnderTest.getRotation(), 0.0001f);
    }
}
