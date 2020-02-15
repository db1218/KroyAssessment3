package com.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.misc.Constants;
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

    private ArrayList<Texture> textures;

    private Firetruck firetruckUnderTest;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        setUpTextures();
        firetruckUnderTest = new Firetruck(textures, textures,TruckType.BLUE,t1,t2,firestation,false);
    }

    private void setUpTextures() {
        Texture texture = Mockito.mock(Texture.class);
        textures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            textures.add(texture);
        }
    }

    /**
     * Tests if firetrucks have different attributes (Health, Reserve, Speed).
     * Tests them using waterfall, if 2/4 trucks have the same attribute,
     * the test fails.
     */
    @Test
    public void differentHealth() {
        assertNotEquals(TruckType.BLUE.getProperties()[0], TruckType.RED.getProperties()[0]);
        assertNotEquals(TruckType.RED.getProperties()[0], TruckType.YELLOW.getProperties()[0]);
        assertNotEquals(TruckType.YELLOW.getProperties()[0], TruckType.GREEN.getProperties()[0]);
    }
    @Test
    public void differentAcceleration() {
        assertNotEquals(TruckType.BLUE.getProperties()[1], TruckType.RED.getProperties()[1]);
        assertNotEquals(TruckType.RED.getProperties()[1], TruckType.YELLOW.getProperties()[1]);
        assertNotEquals(TruckType.YELLOW.getProperties()[1], TruckType.GREEN.getProperties()[1]);
    }
    @Test
    public void differentMaxSpeed() {
        assertNotEquals(TruckType.BLUE.getProperties()[2], TruckType.RED.getProperties()[2]);
        assertNotEquals(TruckType.RED.getProperties()[2], TruckType.YELLOW.getProperties()[2]);
        assertNotEquals(TruckType.YELLOW.getProperties()[2], TruckType.GREEN.getProperties()[2]);
    }
    @Test
    public void differentRestitution() {
        assertNotEquals(TruckType.BLUE.getProperties()[3], TruckType.RED.getProperties()[3]);
        assertNotEquals(TruckType.RED.getProperties()[3], TruckType.YELLOW.getProperties()[3]);
        assertNotEquals(TruckType.YELLOW.getProperties()[3], TruckType.GREEN.getProperties()[3]);
    }
    @Test
    public void differentRange() {
        assertNotEquals(TruckType.BLUE.getProperties()[4], TruckType.RED.getProperties()[4]);
        assertNotEquals(TruckType.RED.getProperties()[4], TruckType.YELLOW.getProperties()[4]);
        assertNotEquals(TruckType.YELLOW.getProperties()[4], TruckType.GREEN.getProperties()[4]);
    }
    @Test
    public void differentWaterReserve() {
        assertNotEquals(TruckType.BLUE.getProperties()[5], TruckType.RED.getProperties()[5]);
        assertNotEquals(TruckType.RED.getProperties()[5], TruckType.YELLOW.getProperties()[5]);
        assertNotEquals(TruckType.YELLOW.getProperties()[5], TruckType.GREEN.getProperties()[5]);
    }

    @Test
    public void testTruckIsInitiallyNotBought() {
        assertFalse(firetruckUnderTest.isBought());
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
    public void testToggleHose() {
        firetruckUnderTest.setToggleDelay(0);
        firetruckUnderTest.toggleHose();
        assertTrue(firetruckUnderTest.isSpraying());
    }

    @Test
    public void waterDecreases() {
        float initialWater = firetruckUnderTest.getWaterBar().getCurrentAmount();
        firetruckUnderTest.getWaterBar().subtractResourceAmount(10);
        assertEquals(initialWater-10, firetruckUnderTest.getWaterBar().getCurrentAmount(), 0.0001f);
    }

    @Test
    public void healthDecreases() {
        float initialHealth = firetruckUnderTest.getHealthBar().getCurrentAmount();
        firetruckUnderTest.getHealthBar().subtractResourceAmount(10);
        assertEquals(initialHealth-10, firetruckUnderTest.getHealthBar().getCurrentAmount(), 0.0001f);
    }

}