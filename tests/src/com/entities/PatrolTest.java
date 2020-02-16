package com.entities;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.misc.Constants;
import com.pathFinding.Junction;
import com.pathFinding.MapGraph;
import com.pathFinding.Road;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PatrolTest {

    @Mock
    private ArrayList<Texture> texturesMock;
    @Mock
    private Texture textureMock;
    @Mock
    private MapGraph mockMapGraph;
    @Mock
    private Array<Junction> junctionsMock;
    @Mock
    private GraphPath<Junction> graphPathMock;
    @Mock
    private Road roadMock;
    @Mock
    private Junction junctionMock;
    @Mock
    private Batch mockBatch;

    private Patrol patrolUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        mockitoWhenSetup();
        patrolUnderTest = new Patrol(texturesMock, mockMapGraph);
    }

    private void mockitoWhenSetup() {
        when(texturesMock.get(texturesMock.size() - 1)).thenReturn(textureMock);
        when(graphPathMock.getCount()).thenReturn(2);
        when(graphPathMock.get(isA(int.class))).thenReturn(junctionMock);
        when(mockMapGraph.findPath(isA(Junction.class), isA(Junction.class))).thenReturn(graphPathMock);
        when(mockMapGraph.getJunctions()).thenReturn(junctionsMock);
        when(mockMapGraph.getRoad(any(Junction.class), any(Junction.class))).thenReturn(roadMock);
        when(mockMapGraph.isRoadLocked(any(Junction.class), any(Junction.class))).thenReturn(false);
        when(mockMapGraph.getJunctions().random()).thenReturn(junctionMock);
    }

    @Test
    public void testTimeCannotShootProjectile() {
        patrolUnderTest.setInternalTime(20);
        assertFalse(patrolUnderTest.canShootProjectile());
    }

    @Test
    public void testTimeCanShootProjectile() {
        patrolUnderTest.setInternalTime(0);
        assertTrue(patrolUnderTest.canShootProjectile());
    }

    @Test
    public void testIfPatrolCanAttackIfWithinRadius() {
        Vector2 targetPosition = new Vector2((Constants.TILE_DIMS*5)-1,0);
        assertTrue(patrolUnderTest.isInRadius(targetPosition));
    }

    @Test
    public void testIfPatrolCanAttackIfOnRadius() {
        Vector2 targetPosition = new Vector2(Constants.TILE_DIMS*5,0);
        assertTrue(patrolUnderTest.isInRadius(targetPosition));
    }

    @Test
    public void testIfPatrolCanAttackIfBeyondRadius() {
        Vector2 targetPosition = new Vector2((Constants.TILE_DIMS*5)+1,0);
        assertFalse(patrolUnderTest.isInRadius(targetPosition));
    }

    @Test
    public void testPatrolIsDead() {
        patrolUnderTest.getHealthBar().subtractResourceAmount(((int) patrolUnderTest.getHealthBar().getCurrentAmount()));
        patrolUnderTest.update(mockBatch);
        assertTrue(patrolUnderTest.isDead());
    }

    @Test
    public void testPatrolIsNotDead() {
        patrolUnderTest.getHealthBar().subtractResourceAmount(((int) patrolUnderTest.getHealthBar().getCurrentAmount())-1);
        patrolUnderTest.update(mockBatch);
        assertFalse(patrolUnderTest.isDead());
    }
}
