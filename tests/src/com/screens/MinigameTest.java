package com.screens;

import com.Kroy;
import com.badlogic.gdx.math.Vector2;
import com.entities.Alien;
import com.misc.Constants;
import com.screens.GameScreen;
import com.screens.MinigameScreen;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class MinigameTest {

    @Mock
    private Kroy kroyMock;
    @Mock
    private GameScreen gameScreenMock;

    private MinigameScreen minigameScreen;

    @Before
    public void setUp() {
        initMocks(this);
        minigameScreen = new MinigameScreen(kroyMock, gameScreenMock);
    }

    @Test
    public void testGenerateType() {
        ArrayList<Constants.AlienType> alienTypes = new ArrayList<Constants.AlienType>();
        alienTypes.add(Constants.AlienType.blue);
        alienTypes.add(Constants.AlienType.green);
        alienTypes.add(Constants.AlienType.red);

        Constants.AlienType alienType = minigameScreen.generateType();

        assertTrue(alienTypes.contains(alienType));
    }

    @Test
    public void testSpawnAlien() {
        minigameScreen.setScreenDimentions(1000, 1000);
        int ETsBefore = minigameScreen.getOnScreenETs().size();
        minigameScreen.spawnAlien();
        int ETsAfter = minigameScreen.getOnScreenETs().size();
        assertTrue(ETsAfter > ETsBefore);
    }

    @Test
    public void testGreenAlienPoints() {
        minigameScreen.getOnScreenETs().add(new Alien(Constants.AlienType.green, new Vector2(0, 0)));

        int scoreBefore = minigameScreen.getScore();
        minigameScreen.setTouch(0,0);
        minigameScreen.checkAlienDespawn();
        int scoreAfter = minigameScreen.getScore();

        assertEquals(scoreAfter, scoreBefore + 10);
    }

    @Test
    public void testRedAlienPoints() {
        minigameScreen.getOnScreenETs().add(new Alien(Constants.AlienType.red, new Vector2(0, 0)));

        int scoreBefore = minigameScreen.getScore();
        minigameScreen.setTouch(0,0);
        minigameScreen.checkAlienDespawn();
        int scoreAfter = minigameScreen.getScore();

        assertEquals(scoreAfter, scoreBefore + 20);
    }

    @Test
    public void testBlueAlienPoints() {
        minigameScreen.getOnScreenETs().add(new Alien(Constants.AlienType.blue, new Vector2(0, 0)));

        int scoreBefore = minigameScreen.getScore();
        minigameScreen.setTouch(0,0);
        minigameScreen.checkAlienDespawn();
        int scoreAfter = minigameScreen.getScore();

        assertEquals(scoreAfter, scoreBefore + 50);
    }

}
