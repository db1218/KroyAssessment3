package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.misc.Constants;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
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
    @Mock
    private Batch mockBatch;

    @Before
    public void setUp() {
        initMocks(this);
        when(textureMock.getHeight()).thenReturn(10);
        when(textureMock.getWidth()).thenReturn(10);
        when(texturesMock.get(texturesMock.size() - 1)).thenReturn(textureMock);
        firestation = new Firestation(textureMock, textureMock, 0, 0, gameScreen);
    }

    @Test
    public void firestationVulnerableTest() {
        firestation.checkRepairRefill(0, false);
        assertTrue(firestation.isVulnerable());
    }

    @Test
    public void firestationDestroyTest() {
        firestation.getHealthBar().subtractResourceAmount(((int) firestation.getHealthBar().getCurrentAmount()));
        firestation.update(mockBatch);
        assertTrue(firestation.isDestroyed());
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
    public void repairNotAtFirestationTest() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firestation.setActiveFireTruck(firetruck);
        firestation.getActiveFireTruck().getHealthBar().subtractResourceAmount(10);
        float healthBefore = firestation.getActiveFireTruck().getHealthBar().getCurrentAmount();
        firestation.getActiveFireTruck().setRespawnLocation(3); // sets car park to upper 1 (not fire station)
        firestation.checkRepairRefill(180, true);
        float healthAfter = firestation.getActiveFireTruck().getHealthBar().getCurrentAmount();
        assertEquals(healthAfter, healthBefore, 0.0);
    }

    @Test
    public void repairAtFirestationTest() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firestation.setActiveFireTruck(firetruck);
        firestation.getActiveFireTruck().getHealthBar().subtractResourceAmount(10);
        float healthBefore = firestation.getActiveFireTruck().getHealthBar().getCurrentAmount();
        firestation.getActiveFireTruck().setRespawnLocation(0); // sets car park to fire station
        firestation.checkRepairRefill(180, true);
        float healthAfter = firestation.getActiveFireTruck().getHealthBar().getCurrentAmount();
        assertTrue(healthAfter > healthBefore);
    }

    @Test
    public void repairAtFirestationAfterItHasBeenDestroyedTest() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firestation.setActiveFireTruck(firetruck);
        firestation.getActiveFireTruck().getHealthBar().subtractResourceAmount(10);
        float healthBefore = firestation.getActiveFireTruck().getHealthBar().getCurrentAmount();
        firestation.getActiveFireTruck().setRespawnLocation(0); // sets car park to fire station
        firestation.getHealthBar().subtractResourceAmount(((int) firestation.getHealthBar().getCurrentAmount()));
        firestation.update(mockBatch);
        firestation.checkRepairRefill(0, true);
        float healthAfter = firestation.getActiveFireTruck().getHealthBar().getCurrentAmount();
        assertEquals(healthAfter, healthBefore, 0.0);
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

    @Test
    public void testOpenCarparkMenu() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firestation.setActiveFireTruck(firetruck);
        firestation.toggleMenu(true);
        assertTrue(firestation.isMenuOpen() && !firestation.getActiveFireTruck().isSpraying());
    }

    @Test
    public void testCloseCarparkMenuToRespawnFiretruck() {
        Firetruck firetruck = new Firetruck(texturesMock, texturesMock, Constants.TruckType.BLUE, tileLayerMock, tileLayerMock, firestation, true);
        firestation.setActiveFireTruck(firetruck);
        firestation.toggleMenu(false);

        Vector2 truckActualRespawnLocation = new Vector2(firestation.getActiveFireTruck().getX(), firestation.getActiveFireTruck().getY());
        Vector2 truckExpectedLocation = firestation.getActiveFireTruck().getCarpark().getLocation();

        assertTrue(!firestation.isMenuOpen() && truckExpectedLocation.equals(truckActualRespawnLocation));
    }
}
