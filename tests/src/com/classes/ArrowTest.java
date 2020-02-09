package com.classes;

import com.badlogic.gdx.math.Vector2;
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
        // Setup
        final Vector2 target = new Vector2(0.0f, 0.0f);
        arrowUnderTest.aimAtTarget(target);
        assertEquals(target, new Vector2(0.0f, 0.0f));
    }
}
