package com.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.config.Constants;
import com.config.Constants.TruckType;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class FiretruckTest {

    @Mock
    GameScreen gameScreenMock;
    @Mock
    Firestation firestation;
    @Mock
    TiledMapTileLayer t1, t2;
    @Mock
    ArrayList<Texture> a1, a2;

    private Firetruck firetruckUnderTest;


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
    public void testBuy() {
        firetruckUnderTest = new Firetruck(a1, a2,TruckType.BLUE,t1,t2,firestation,true);
        firetruckUnderTest.buy();
        assertTrue(firetruckUnderTest.isBought());
    }

    @Test
    public void testCheckCarparkCollision() {
        firetruckUnderTest = new Firetruck(a1, a2,TruckType.BLUE,t1,t2,firestation,true);
        firetruckUnderTest.checkCarparkCollision();
    }

    @Test
    public void testDestroy() {
        firetruckUnderTest = new Firetruck(a1, a2,TruckType.BLUE,t1,t2,firestation,true);
        firetruckUnderTest.destroy();
        assertNull(firetruckUnderTest);
    }

    @Test
    public void testGetDamage() {
        firetruckUnderTest = new Firetruck(a1, a2,TruckType.BLUE,t1,t2,firestation,true);
        final float result = firetruckUnderTest.getDamage();
        assertEquals(0.0f, result, 0.0001);
    }

    @Test
    public void testToggleHose() {
        firetruckUnderTest = new Firetruck(a1, a2,TruckType.BLUE,t1,t2,firestation,true);
        firetruckUnderTest.toggleHose();
        assertTrue(firetruckUnderTest.isSpraying());
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