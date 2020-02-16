package com.misc;

import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class ResourceBarTest {

    private ResourceBar resource;

    @Before
    public void setUp() {
        resource = new ResourceBar(10f, 2f);
    }

    @Test
    public void resourceBarDecreases() {
        resource.subtractResourceAmount(10);
        assertEquals((int)resource.getCurrentAmount(), 90);
    }

    @Test
    public void resourceBarIncreases() {
        resource.resetResourceAmount();
        resource.subtractResourceAmount(20);
        resource.addResourceAmount(15);
        assertEquals((int)resource.getCurrentAmount(), 95);
    }
}
