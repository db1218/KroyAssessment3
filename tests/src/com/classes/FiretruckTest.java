package com.classes;

import static com.config.Constants.FiretruckOneProperties;
import static com.config.Constants.FiretruckTwoProperties;
import static com.screens.GameScreen.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.config.Constants;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.screens.GameScreen;

import com.testrunner.GdxTestRunner;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.xml.soap.Text;
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
        assertNotEquals(FiretruckOneProperties[0], FiretruckTwoProperties[0]);
    }
    @Test
    public void differentAcceleration() {
        assertNotEquals(FiretruckOneProperties[1], FiretruckTwoProperties[1]);
    }
    @Test
    public void differentMaxSpeed() {
        assertNotEquals(FiretruckOneProperties[2], FiretruckTwoProperties[2]);
    }
    @Test
    public void differentRestitution() {
        assertNotEquals(FiretruckOneProperties[3], FiretruckTwoProperties[3]);
    }
    @Test
    public void differentRange() {
        assertNotEquals(FiretruckOneProperties[4], FiretruckTwoProperties[4]);
    }
    @Test
    public void differentWaterReserve() {
        assertNotEquals(FiretruckOneProperties[5], FiretruckTwoProperties[5]);
    }

    @Test
    public void spawnFireTruck() {
        gameScreenMock.constructFireTruck(Constants.TruckColours.BLUE, true, FiretruckOneProperties);
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