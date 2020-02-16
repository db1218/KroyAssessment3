package com.screens;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.misc.SFX;

/**
 * Input handler processor for {@link GameScreen}
 */
public class GameInputHandler implements InputProcessor {

    // communication with screen it is detecting inputs for
    private final GameScreen gameScreen;

    /**
     * Constructor for game input handler which takes
     * in GameScreen to be able to provide some sort
     * of feedback to when an event occurs
     * @param gameScreen    GameScreen
     */
    public GameInputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            gameScreen.pause();
            SFX.sfx_button_click.play();
        } else if (keycode == Input.Keys.ENTER) {
            gameScreen.finishTutorial();
        } else if (keycode == Input.Keys.SPACE) {
            gameScreen.getActiveTruck().setArrow(true);
        }
        return true;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            gameScreen.getActiveTruck().setArrow(false);
        }
        return true;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            gameScreen.getFirestation().getActiveFireTruck().toggleHose();
        }
        return true;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX   x coord of touch
     * @param screenY   y coord of touch
     * @param pointer   the pointer for the event.
     * @param button    the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            gameScreen.getFirestation().getActiveFireTruck().toggleHose();
        }
        return true;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX x coord of touch
     * @param screenY y coord of touch
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX x coord of touch
     * @param screenY y coord of touch
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        gameScreen.cameraZoom(0.04f * amount);
        return true;
    }
}
