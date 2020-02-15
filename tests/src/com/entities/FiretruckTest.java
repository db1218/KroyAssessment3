package com.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.misc.Constants.TruckType;
import com.misc.ResourceBar;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class FiretruckTest {

    @Mock
    Firestation firestation;
    @Mock
    TiledMapTileLayer t1, t2;
    @Mock
    private ArrayList<Texture> texturesMock;
    @Mock
    private Texture textureMock;

    private Firetruck firetruckUnderTest;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        initMocks(this);
        when(textureMock.getHeight()).thenReturn(10);
        when(textureMock.getWidth()).thenReturn(10);
        when(texturesMock.get(texturesMock.size() - 1)).thenReturn(textureMock);
        firetruckUnderTest = new Firetruck(texturesMock, texturesMock,TruckType.BLUE,t1,t2,firestation,false);
    }

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
        firetruckUnderTest.buy();
        assertTrue(firetruckUnderTest.isBought());
    }

    @Test
    public void testDestroy() {
        firetruckUnderTest.destroy();
        assertFalse(firetruckUnderTest.isAlive());
    }

    @Test
    public void testGetDamage() {
        final float result = firetruckUnderTest.getDamage();
        assertEquals(2.1f, result, 0.0001);
    }

    @Test
    public void testToggleHose() {
        firetruckUnderTest.setToggleDelay(0);
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