package com.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.misc.Constants;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class FireStationTest {

    private Firestation firestation;

    @Mock
    private Texture textureMock;

    @Mock
    private ArrayList<Texture> texturesMock;

    @Mock
    private GameScreen gameScreen;

    @Mock
    private TiledMapTileLayer tileLayerMock;

    private ArrayList<Texture> textures;

    @Before
    public void setUp() {
        setUpTextures();
        firestation = new Firestation(textureMock, textureMock, 0, 0, gameScreen);
    }

    private void setUpTextures() {
        Texture texture = new Texture(Gdx.files.internal("garage.jpg"));
        textures = new ArrayList<>();
        textures.add(texture);
        textures.add(texture);
        textures.add(texture);
        textures.add(texture);
        textures.add(texture);
    }

    @Test
    public void refillEmptyTest() {
        Firetruck firetruck = new Firetruck(textures, textures, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firetruck.getWaterBar().subtractResourceAmount(((int) firetruck.getWaterBar().getCurrentAmount()));
        float waterBefore = firetruck.getWaterBar().getCurrentAmount();
        firestation.repairRefill(firetruck);
        float waterAfter = firetruck.getWaterBar().getCurrentAmount();
        assertTrue(waterAfter > waterBefore);
    }

    @Test
    public void refillNearlyFullTest() {
        Firetruck firetruck = new Firetruck(textures, textures, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firetruck.getWaterBar().subtractResourceAmount(1);
        float waterBefore = firetruck.getWaterBar().getCurrentAmount();
        firestation.repairRefill(firetruck);
        float waterAfter = firetruck.getWaterBar().getCurrentAmount();
        assertTrue(waterAfter > waterBefore);
    }

    @Test
    public void refillFullTest() {
        Firetruck firetruck = new Firetruck(textures, textures, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firetruck.getWaterBar().subtractResourceAmount(0);
        float waterBefore = firetruck.getWaterBar().getCurrentAmount();
        firestation.repairRefill(firetruck);
        float waterAfter = firetruck.getWaterBar().getCurrentAmount();
        assertEquals(waterAfter, waterBefore, 0.0);
    }

    @Test
    public void changeFiretruckTest() {
        Firetruck firetruck1 = new Firetruck(textures, textures, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        Firetruck firetruck2 = new Firetruck(textures, textures, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        Firetruck firetruck3 = new Firetruck(textures, textures, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firestation.setActiveFireTruck(firetruck1);
        firestation.parkFireTruck(firetruck2);
        firestation.parkFireTruck(firetruck3);

        firestation.changeFiretruck(0);

        boolean activeCorrect = firetruck2.equals(firestation.getActiveFireTruck());

        ArrayList<Firetruck> expectedParkedFiretrucks = new ArrayList<>();
        expectedParkedFiretrucks.add(firetruck1);
        expectedParkedFiretrucks.add(firetruck3);

        boolean parkedCorrect = expectedParkedFiretrucks.equals(firestation.getParkedFireTrucks());

        assertTrue(activeCorrect && parkedCorrect);
    }
}
