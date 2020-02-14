package com.screens;

/*
 *  =======================================================================
 *                    New class added for Assessment 3
 *  =======================================================================
 */

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Input handler for Minigame, when the player
 * clicks down on the screen, a water image
 * will appear and destroy an alien if there is
 * one there. The water will then disappear when
 * the user clicks up. The user cannot drag the
 * hose to avoid cheating!
 */
public class MiniGameInputHandler implements InputProcessor {

    // communication with screen it is detecting inputs for
    private final MinigameScreen minigameScreen;

    /**
     * Constructor for minigame handler, which takes minigameScreen
     * so that is can communicate events that happen in here
     *
     * @param minigameScreen MinigameScreen
     */
    public MiniGameInputHandler(MinigameScreen minigameScreen) {
        this.minigameScreen = minigameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            minigameScreen.toGameScreen();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 touch = minigameScreen.getCamera().unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        minigameScreen.setTouch((int)touch.x, (int)touch.y);
        minigameScreen.setCanSpray(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        minigameScreen.setTouch(0, 0);
        minigameScreen.setCanSpray(false);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
