package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.misc.Arrow;
import com.misc.Constants;
import com.misc.Constants.TruckType;
import com.misc.ResourceBar;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
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

    private Arrow arrowUnderTest;

    private Firetruck firetruckUnderTest;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        initMocks(this);
        when(textureMock.getHeight()).thenReturn(10);
        when(textureMock.getWidth()).thenReturn(10);
        when(texturesMock.get(texturesMock.size() - 1)).thenReturn(textureMock);
        firetruckUnderTest = new Firetruck(texturesMock, texturesMock, TruckType.BLUE, t1, t2, firestation, false);
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
    public void testShouldNotBeDestroyed() {
        firetruckUnderTest.getHealthBar().subtractResourceAmount(((int) firetruckUnderTest.getHealthBar().getCurrentAmount()) - 1);
        firetruckUnderTest.checkDestroyed();
        assertTrue(firetruckUnderTest.isAlive());
    }

    @Test
    public void testShouldJustBeDestroyed() {
        firetruckUnderTest.getHealthBar().subtractResourceAmount(((int) firetruckUnderTest.getHealthBar().getCurrentAmount()));
        firetruckUnderTest.checkDestroyed();
        assertFalse(firetruckUnderTest.isAlive());
    }

    @Test
    public void testShouldDefinitelyBeDestroyed() {
        firetruckUnderTest.getHealthBar().subtractResourceAmount(10000);
        firetruckUnderTest.checkDestroyed();
        assertFalse(firetruckUnderTest.isAlive());
    }

    @Test
    public void testShouldDefinitelyNotToggleHose() {
        firetruckUnderTest.setToggleDelay(0);
        firetruckUnderTest.getWaterBar().subtractResourceAmount(100000);
        firetruckUnderTest.toggleHose();
        assertFalse(firetruckUnderTest.isSpraying());
    }

    @Test
    public void testShouldJustNotToggleHose() {
        firetruckUnderTest.setToggleDelay(0);
        firetruckUnderTest.getWaterBar().subtractResourceAmount(((int) firetruckUnderTest.getWaterBar().getCurrentAmount()));
        firetruckUnderTest.toggleHose();
        assertFalse(firetruckUnderTest.isSpraying());
    }

    @Test
    public void testShouldToggleHose() {
        firetruckUnderTest.setToggleDelay(0);
        firetruckUnderTest.getWaterBar().subtractResourceAmount(((int) firetruckUnderTest.getWaterBar().getCurrentAmount()) - 1);
        firetruckUnderTest.toggleHose();
        assertTrue(firetruckUnderTest.isSpraying());
    }

    @Test
    public void waterDecreases() {
        float initialWater = firetruckUnderTest.getWaterBar().getCurrentAmount();
        firetruckUnderTest.getWaterBar().subtractResourceAmount(10);
        assertEquals(initialWater - 10, firetruckUnderTest.getWaterBar().getCurrentAmount(), 0.0001f);
    }

    @Test
    public void healthDecreases() {
        float initialHealth = firetruckUnderTest.getHealthBar().getCurrentAmount();
        firetruckUnderTest.getHealthBar().subtractResourceAmount(10);
        assertEquals(initialHealth-10, firetruckUnderTest.getHealthBar().getCurrentAmount(), 0.0001f);
    }

    @Test
    public void waterIncreases() {
        float initialWater = firetruckUnderTest.getWaterBar().getCurrentAmount();
        firetruckUnderTest.getWaterBar().subtractResourceAmount(10);
        firetruckUnderTest.getWaterBar().addResourceAmount(5);
        assertEquals(initialWater - 5, firetruckUnderTest.getWaterBar().getCurrentAmount(), 0.0001f);
    }

    @Test
    public void healthIncreases() {
        float initialHealth = firetruckUnderTest.getHealthBar().getCurrentAmount();
        firetruckUnderTest.getWaterBar().subtractResourceAmount(10);
        firetruckUnderTest.getHealthBar().addResourceAmount(10);
        assertEquals(initialHealth, firetruckUnderTest.getHealthBar().getCurrentAmount(), 0.0001f);
    }

    @Test
    public void testUpdateNearestFortress1() {
        ArrayList<ETFortress> fortresses = new ArrayList<ETFortress>();
        ETFortress fortress1 = Mockito.mock(ETFortress.class);
        ETFortress fortress2 = Mockito.mock(ETFortress.class);
        when(fortress1.getCentre()).thenReturn(new Vector2(0,0));
        when(fortress2.getCentre()).thenReturn(new Vector2(10,10));
        fortresses.add(fortress1);
        fortresses.add(fortress2);
        firetruckUnderTest.setPosition(4,4);
        firetruckUnderTest.setNearestFortress(fortresses);
        assertEquals(firetruckUnderTest.getNearestFortress(), fortress1);
    }

    @Test
    public void testUpdateNearestFortressOnBoundary() {
        ArrayList<ETFortress> fortresses = new ArrayList<ETFortress>();
        ETFortress fortress1 = Mockito.mock(ETFortress.class);
        ETFortress fortress2 = Mockito.mock(ETFortress.class);
        when(fortress1.getCentre()).thenReturn(new Vector2(0,0));
        when(fortress2.getCentre()).thenReturn(new Vector2(10,10));
        fortresses.add(fortress1);
        fortresses.add(fortress2);
        firetruckUnderTest.setPosition(5,5);
        firetruckUnderTest.setNearestFortress(fortresses);
        assertEquals(firetruckUnderTest.getNearestFortress(), fortress1);
    }

    @Test
    public void testUpdateNearestFortress2() {
        ArrayList<ETFortress> fortresses = new ArrayList<ETFortress>();
        ETFortress fortress1 = Mockito.mock(ETFortress.class);
        ETFortress fortress2 = Mockito.mock(ETFortress.class);
        when(fortress1.getCentre()).thenReturn(new Vector2(0,0));
        when(fortress2.getCentre()).thenReturn(new Vector2(10,10));
        fortresses.add(fortress1);
        fortresses.add(fortress2);
        firetruckUnderTest.setPosition(6,6);
        firetruckUnderTest.setNearestFortress(fortresses);
        assertEquals(firetruckUnderTest.getNearestFortress(), fortress1);
    }

    @Test
    public void testUpdateArrowA() {
        ArrayList<ETFortress> fortresses = new ArrayList<ETFortress>();
        ETFortress fortress1 = Mockito.mock(ETFortress.class);
        ETFortress fortress2 = Mockito.mock(ETFortress.class);
        when(fortress1.getCentre()).thenReturn(new Vector2(10,10));
        when(fortress2.getCentre()).thenReturn(new Vector2(20,20));
        fortresses.add(fortress1);
        fortresses.add(fortress2);
        firetruckUnderTest.setPosition(2,2);
        firetruckUnderTest.updateArrow(Mockito.mock(ShapeRenderer.class), fortresses);

        arrowUnderTest = new Arrow(0, 0, 0, 0);
        final Vector2 target = firetruckUnderTest.getNearestFortress().getCentre();
        arrowUnderTest.aimAtTarget(target);
        float theta = (float) (180f / Math.PI * Math.atan2(arrowUnderTest.getX() - target.x, target.y - arrowUnderTest.getY()));
        System.out.println(theta);
        System.out.println(arrowUnderTest.getRotation());
        assertEquals(theta, arrowUnderTest.getRotation(), 0.0001f);
    }

    @Test
    public void testUpdateArrowB() {
        ArrayList<ETFortress> fortresses = new ArrayList<ETFortress>();
        ETFortress fortress1 = Mockito.mock(ETFortress.class);
        ETFortress fortress2 = Mockito.mock(ETFortress.class);
        when(fortress1.getCentre()).thenReturn(new Vector2(0,0));
        when(fortress2.getCentre()).thenReturn(new Vector2(20,20));
        fortresses.add(fortress1);
        fortresses.add(fortress2);
        firetruckUnderTest.setPosition(0,0);
        firetruckUnderTest.updateArrow(Mockito.mock(ShapeRenderer.class), fortresses);

        arrowUnderTest = new Arrow(0, 0, 0, 0);
        final Vector2 target = firetruckUnderTest.getNearestFortress().getCentre();
        arrowUnderTest.aimAtTarget(target);
        float theta = (float) (180f / Math.PI * Math.atan2(arrowUnderTest.getX() - target.x, target.y - arrowUnderTest.getY()));
        System.out.println(theta);
        System.out.println(arrowUnderTest.getRotation());
        assertEquals(theta, arrowUnderTest.getRotation(), 0.0001f);
    }

    @Test
    public void testUpdateArrowC() {
        ArrayList<ETFortress> fortresses = new ArrayList<ETFortress>();
        ETFortress fortress1 = Mockito.mock(ETFortress.class);
        ETFortress fortress2 = Mockito.mock(ETFortress.class);
        when(fortress1.getCentre()).thenReturn(new Vector2(999999999,999999999));
        when(fortress2.getCentre()).thenReturn(new Vector2(999999999,999999999));
        fortresses.add(fortress1);
        fortresses.add(fortress2);
        firetruckUnderTest.setPosition(999999999,999999999);
        firetruckUnderTest.updateArrow(Mockito.mock(ShapeRenderer.class), fortresses);

        arrowUnderTest = new Arrow(999999999, 999999999, 999999999, 999999999);
        final Vector2 target = firetruckUnderTest.getNearestFortress().getCentre();
        arrowUnderTest.aimAtTarget(target);
        float theta = (float) (180f / Math.PI * Math.atan2(arrowUnderTest.getX() - target.x, target.y - arrowUnderTest.getY()));
        System.out.println(theta);
        System.out.println(arrowUnderTest.getRotation());
        assertEquals(theta, arrowUnderTest.getRotation(), 0.0001f);
    }
}