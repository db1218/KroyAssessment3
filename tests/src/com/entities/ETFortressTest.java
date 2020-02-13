package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.config.Constants;
import com.screens.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

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
        final Polygon polygon = new Polygon(new float[]{0.0f});
        final boolean result = etFortressUnderTest.isInRadius(polygon);
        assertFalse(result);
    }

}
