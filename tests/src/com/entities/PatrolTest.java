package com.entities;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.pathFinding.Junction;
import com.pathFinding.MapGraph;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PatrolTest {

    private Patrol patrolUnderTest;
    @Mock
    private ArrayList<Texture> texturesMock;
    @Mock
    private Texture textureMock;
    @Mock
    private MapGraph mapGraphMock;
    @Mock
    private GraphPath<Junction> graphPathJunctionMock;

    @Before
    public void setUp() {
        initMocks(this);
        when(textureMock.getHeight()).thenReturn(10);
        when(textureMock.getWidth()).thenReturn(10);
        when(texturesMock.get(texturesMock.size() - 1)).thenReturn(textureMock);

        Array<Junction> junctions = new Array<>();
        junctions.add(new Junction(0,0, "junction 1"));
        junctions.add(new Junction(10,10, "junction 2"));
        when(mapGraphMock.getJunctions()).thenReturn(junctions);
        when(mapGraphMock.findPath(any(Junction.class), any(Junction.class))).thenReturn(graphPathJunctionMock);

        patrolUnderTest = new Patrol(texturesMock, mapGraphMock);
    }

    @Test
    public void testCanShootProjectile() {

    }
}
