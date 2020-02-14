package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.misc.Constants;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class ETFortressTest {

    @Mock
    private Texture mockTexture;
    @Mock
    private Texture mockDestroyedTexture;
    @Mock
    private GameScreen mockGameScreen;

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

}
