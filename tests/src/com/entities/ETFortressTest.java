package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.misc.Constants;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class ETFortressTest {

    @Mock
    private Texture mockTexture;
    @Mock
    private Texture mockDestroyedTexture;
    @Mock
    private GameScreen mockGameScreen;
    @Mock
    private Batch mockBatch;

    private ETFortress etFortressUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        etFortressUnderTest = new ETFortress(mockTexture, mockDestroyedTexture, 1.0f, 1.0f, 0.0f, 0.0f, Constants.FortressType.CLIFFORD, mockGameScreen);
    }

    @Test
    public void testCanShootProjectile() {
        final boolean result = etFortressUnderTest.canShootProjectile();
        assertFalse(result);
    }

    @Test
    public void testIsInRadius() {
        final boolean result = etFortressUnderTest.isInRadius(new Vector2(0,0));
        assertTrue(result);
    }

    @Test
    public void testIsOnRadius() {
        final boolean result = etFortressUnderTest.isInRadius(new Vector2(etFortressUnderTest.getType().getRange(),0));
        assertTrue(result);
    }

    @Test
    public void testIsNotInRadius() {
        final boolean result = etFortressUnderTest.isInRadius(new Vector2(etFortressUnderTest.getType().getRange() + 1,0));
        assertFalse(result);
    }

    @Test
    public void testNotEnoughToFlood() {
        when(mockGameScreen.getETFortressesDestroyed()).thenReturn(new int[]{1, 6});
        doNothing().when(mockGameScreen).showPopupText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        etFortressUnderTest.getHealthBar().subtractResourceAmount(((int) etFortressUnderTest.getHealthBar().getCurrentAmount()) - 1);
        etFortressUnderTest.update(mockBatch);
        assertFalse(etFortressUnderTest.isFlooded());
    }

    @Test
    public void testJustEnoughToFlood() {
        when(mockGameScreen.getETFortressesDestroyed()).thenReturn(new int[]{1, 6});
        doNothing().when(mockGameScreen).showPopupText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        etFortressUnderTest.getHealthBar().subtractResourceAmount(((int) etFortressUnderTest.getHealthBar().getCurrentAmount()));
        etFortressUnderTest.update(mockBatch);
        assertTrue(etFortressUnderTest.isFlooded());
    }

    @Test
    public void testDefinatelyEnoughToFlood() {
        when(mockGameScreen.getETFortressesDestroyed()).thenReturn(new int[]{1, 6});
        doNothing().when(mockGameScreen).showPopupText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        etFortressUnderTest.getHealthBar().subtractResourceAmount(10000);
        etFortressUnderTest.update(mockBatch);
        assertTrue(etFortressUnderTest.isFlooded());
    }

    @Test
    public void testHealOverTime() {
        when(mockGameScreen.getETFortressesDestroyed()).thenReturn(new int[]{1, 6});
        doNothing().when(mockGameScreen).showPopupText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        etFortressUnderTest.getHealthBar().subtractResourceAmount(1);

        float healthBefore = etFortressUnderTest.getHealthBar().getCurrentAmount();
        etFortressUnderTest.setInternalTime(0);
        etFortressUnderTest.update(mockBatch);
        float healthAfter = etFortressUnderTest.getHealthBar().getCurrentAmount();
        assertTrue(healthAfter > healthBefore);
    }

    @Test
    public void testHealOverTimeTooSoon() {
        when(mockGameScreen.getETFortressesDestroyed()).thenReturn(new int[]{1, 6});
        doNothing().when(mockGameScreen).showPopupText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        etFortressUnderTest.getHealthBar().subtractResourceAmount(1);

        float healthBefore = etFortressUnderTest.getHealthBar().getCurrentAmount();
        etFortressUnderTest.setInternalTime(20);
        etFortressUnderTest.update(mockBatch);
        float healthAfter = etFortressUnderTest.getHealthBar().getCurrentAmount();
        assertEquals(healthAfter, healthBefore, 0.0);
    }
}
