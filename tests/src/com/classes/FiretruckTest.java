package com.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.config.Constants.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.screens.GameScreen;

import com.testrunner.GdxTestRunner;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class FiretruckTest {

    @Mock
    GameScreen gameScreenMock;
    Firestation firestation;
    TiledMapTileLayer t1, t2;
    ArrayList<Texture> a1, a2;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * Tests if firetrucks have different attributes (Health, Reserve, Speed)
     */
    @Test
    public void differentHealth() {
        assertNotEquals(TruckType.BLUE.getProperties()[0], TruckType.RED.getProperties()[0]);
    }
    @Test
    public void differentAcceleration() {
        assertNotEquals(TruckType.BLUE.getProperties()[1], TruckType.RED.getProperties()[1]);
    }
    @Test
    public void differentMaxSpeed() {
        assertNotEquals(TruckType.BLUE.getProperties()[2], TruckType.RED.getProperties()[2]);
    }
    @Test
    public void differentRestitution() {
        assertNotEquals(TruckType.BLUE.getProperties()[3], TruckType.RED.getProperties()[3]);
    }
    @Test
    public void differentRange() {
        assertNotEquals(TruckType.BLUE.getProperties()[4], TruckType.RED.getProperties()[4]);
    }
    @Test
    public void differentWaterReserve() {
        assertNotEquals(TruckType.BLUE.getProperties()[5], TruckType.RED.getProperties()[5]);
    }

    @Test
    public void spawnFireTruck() {
        gameScreenMock.constructFireTruck(true, TruckType.BLUE, true);
    }

    @Test
    public void resourceBarDecreases() {
        ResourceBar resource = new ResourceBar(10f, 2f);
        resource.subtractResourceAmount(10);

        assertEquals((int)resource.getCurrentAmount(), 90);
    }

    @Test
    public void resourceBarIncreases() {
        ResourceBar resource = new ResourceBar(10f, 2f);
        resource.subtractResourceAmount(20);
        resource.addResourceAmount(15);

        assertEquals((int)resource.getCurrentAmount(), 95);
    }

}