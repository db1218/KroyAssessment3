package com.sprites;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.pathFinding.Junction;
import com.pathFinding.MapGraph;
import com.pathFinding.Road;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PatrolMovementSpriteTest {

    private MapGraph mockMapGraph;
    private Texture mockSpriteTexture;

    private PatrolMovementSprite patrolMovementSpriteUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        Array<Junction> junctions = new Array<>();
        junctions.add(new Junction(0,0, "junction 1"));
        junctions.add(new Junction(10,10, "junction 2"));
        when(mockMapGraph.getJunctions()).thenReturn(junctions);
        GraphPath graphPathMock = Mockito.mock(GraphPath.class);
        when(graphPathMock.getCount()).thenReturn(2);
        when(mockMapGraph.findPath(junctions.get(0), junctions.get(1))).thenReturn(graphPathMock);
        patrolMovementSpriteUnderTest = new PatrolMovementSprite(mockSpriteTexture, mockMapGraph);
    }

    @Test
    public void testSetGoal() {
        final Junction goal = new Junction(0.0f, 0.0f, "name");
        when(mockMapGraph.findPath(any(Junction.class), any(Junction.class))).thenReturn(null);

        patrolMovementSpriteUnderTest.setGoal(goal);

        assertEquals(patrolMovementSpriteUnderTest.getGoal(), goal);
    }

    @Test
    public void testStep() {
        final Array<Junction> junctions = new Array<>(false, new Junction[]{new Junction(0.0f, 0.0f, "name")}, 0, 0);
        when(mockMapGraph.getJunctions()).thenReturn(junctions);

        when(mockMapGraph.findPath(any(Junction.class), any(Junction.class))).thenReturn(null);
        when(mockMapGraph.isRoadLocked(any(Junction.class), any(Junction.class))).thenReturn(false);

        final Road road = new Road(new Junction(0.0f, 0.0f, "name"), new Junction(0.0f, 0.0f, "name"));
        when(mockMapGraph.getRoad(any(Junction.class), any(Junction.class))).thenReturn(road);

        patrolMovementSpriteUnderTest.step();

        verify(mockMapGraph).unlockRoad(any(Road.class), any(PatrolMovementSprite.class));
        verify(mockMapGraph).lockRoad(any(Road.class), any(PatrolMovementSprite.class));
    }
}
