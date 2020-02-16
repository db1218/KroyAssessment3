package com.sprites;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Queue;
import com.pathFinding.Junction;
import com.pathFinding.MapGraph;
import com.pathFinding.Road;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PatrolMovementSpriteTest {

    @Mock
    private Texture mockSpriteTexture;

    private MapGraph mapGraph;
    private Junction zero;
    private Junction one;
    private Junction two;

    private PatrolMovementSprite patrolMovementSpriteUnderTest;
    
    @Before
    public void setUp() {
        initMocks(this);
        mapGraph = new MapGraph();
        populateTestGraph();
        patrolMovementSpriteUnderTest = new PatrolMovementSprite(mockSpriteTexture, mapGraph);
    }

    private void populateTestGraph() {
        zero = new Junction(0, 0, "zero");
        one = new Junction(0, 10, "one");
        two = new Junction(10, 10, "two");

        mapGraph.addJunction(zero);
        mapGraph.addJunction(one);
        mapGraph.addJunction(two);

        mapGraph.connectJunctions(zero, one);
        mapGraph.connectJunctions(one, zero);
        mapGraph.connectJunctions(one, two);
        mapGraph.connectJunctions(two, one);
        mapGraph.connectJunctions(zero, two);
        mapGraph.connectJunctions(two, zero);

        /* Simple Testing Graph

                     (two)
                      /|
                    /  |
                  /    |
                /      |
              /________|
           (zero)      (one)

         */
    }

    @Test
    public void testSetGoal() {
        patrolMovementSpriteUnderTest.previousJunction = mapGraph.getJunctions().get(0);
        Junction goal = mapGraph.getJunctions().get(1);
        patrolMovementSpriteUnderTest.setGoal(goal);
        assertEquals(patrolMovementSpriteUnderTest.getGoal(), goal);
    }

    @Test
    public void testGoalEqualsStartRoute() {
        patrolMovementSpriteUnderTest.pathQueue.clear();
        patrolMovementSpriteUnderTest.previousJunction = zero;

        Junction goal = zero;
        patrolMovementSpriteUnderTest.setGoal(goal);

        Queue<Junction> expectedPath = new Queue<Junction>();
        expectedPath.addLast(zero);

        assertEquals(expectedPath, patrolMovementSpriteUnderTest.pathQueue);
    }

    @Test
    public void testOneEdgeRoute() {
        patrolMovementSpriteUnderTest.pathQueue.clear();
        patrolMovementSpriteUnderTest.previousJunction = zero;

        Junction goal = one;
        patrolMovementSpriteUnderTest.setGoal(goal);

        Queue<Junction> expectedPath = new Queue<Junction>();
        expectedPath.addLast(zero);
        expectedPath.addLast(one);

        assertEquals(expectedPath, patrolMovementSpriteUnderTest.pathQueue);
    }

    @Test
    public void testTwoEdgeShortestPathRoute() {
        patrolMovementSpriteUnderTest.pathQueue.clear();
        patrolMovementSpriteUnderTest.previousJunction = zero;

        Junction goal = two;
        patrolMovementSpriteUnderTest.setGoal(goal);

        Queue<Junction> expectedPath = new Queue<Junction>();
        expectedPath.addLast(zero);
        expectedPath.addLast(two);

        assertEquals(expectedPath, patrolMovementSpriteUnderTest.pathQueue);
    }

}
