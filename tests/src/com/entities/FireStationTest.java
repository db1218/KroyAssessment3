package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.misc.Constants;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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

    @Before
    public void setUp() {
        initMocks(this);
        when(textureMock.getHeight()).thenReturn(10);
        when(textureMock.getWidth()).thenReturn(10);
        when(texturesMock.get(texturesMock.size() - 1)).thenReturn(textureMock);
        firestation = new Firestation(textureMock, textureMock, 0, 0, gameScreen);
    }

    @Test
    public void refillEmptyTest() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firetruck.getWaterBar().subtractResourceAmount(((int) firetruck.getWaterBar().getCurrentAmount()));
        float waterBefore = firetruck.getWaterBar().getCurrentAmount();
        firestation.repairRefill(firetruck);
        float waterAfter = firetruck.getWaterBar().getCurrentAmount();
        assertTrue(waterAfter > waterBefore);
    }

    @Test
    public void refillNearlyFullTest() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firetruck.getWaterBar().subtractResourceAmount(1);
        float waterBefore = firetruck.getWaterBar().getCurrentAmount();
        firestation.repairRefill(firetruck);
        float waterAfter = firetruck.getWaterBar().getCurrentAmount();
        assertTrue(waterAfter > waterBefore);
    }

    @Test
    public void refillFullTest() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firetruck.getWaterBar().subtractResourceAmount(0);
        float waterBefore = firetruck.getWaterBar().getCurrentAmount();
        firestation.repairRefill(firetruck);
        float waterAfter = firetruck.getWaterBar().getCurrentAmount();
        assertEquals(waterAfter, waterBefore, 0.0);
    }

    @Test
    public void changeFiretruckTest() {
        Firetruck firetruck1 = Mockito.mock(Firetruck.class);
        Firetruck firetruck2 = Mockito.mock(Firetruck.class);
        Firetruck firetruck3 = Mockito.mock(Firetruck.class);
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
