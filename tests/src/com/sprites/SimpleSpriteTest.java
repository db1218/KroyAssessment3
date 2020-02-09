package com.sprites;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SimpleSpriteTest {

    @Mock
    private Texture mockSpriteTexture;

    private SimpleSprite simpleSpriteUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        simpleSpriteUnderTest = new SimpleSprite(mockSpriteTexture);
    }

    @Test
    public void testRemoveSprite() {
        final Texture destroyedTexture = new Texture(0, 0, Pixmap.Format.Alpha);
        simpleSpriteUnderTest.removeSprite(destroyedTexture);
        assertEquals(simpleSpriteUnderTest.getTexture(), mockSpriteTexture);
    }

    @Test
    public void testSetSize() {
        simpleSpriteUnderTest.setSize(0.0f, 0.0f);
        assertEquals(simpleSpriteUnderTest.getHeight(), 0.0f, 0.0001f);
        assertEquals(simpleSpriteUnderTest.getWidth(), 0.0f, 0.0001f);
    }

    @Test
    public void testSetMovementHitBox() {
        simpleSpriteUnderTest.setMovementHitBox(0.0f);
        assertEquals(simpleSpriteUnderTest.getMovementHitBox().getRotation(), 0.0f, 0.0001f);
    }

    @Test
    public void testRotate() {
        simpleSpriteUnderTest.rotate(1.0f);
        assertEquals(simpleSpriteUnderTest.getMovementHitBox().getRotation(), -89.0f, 0.0001f);
    }

    @Test
    public void testSetRotation() {
        simpleSpriteUnderTest.setRotation(5.0f);
        assertEquals(simpleSpriteUnderTest.getMovementHitBox().getRotation(), 5.0f, 0.0001f);
    }

    @Test
    public void testDispose() {
        simpleSpriteUnderTest.dispose();
        verify(mockSpriteTexture).dispose();
    }
}
