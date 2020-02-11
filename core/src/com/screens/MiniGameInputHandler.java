package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MiniGameInputHandler implements InputProcessor {

    private final MinigameScreen minigameScreen;

    public MiniGameInputHandler(MinigameScreen minigameScreen) {
        this.minigameScreen = minigameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
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
        Vector3 touch = minigameScreen.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Vector2 newTouch = new Vector2((int)touch.x, (int) touch.y);
        minigameScreen.setTouch((int)newTouch.x, (int)newTouch.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        minigameScreen.setTouch(-1,-1);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        minigameScreen.setTouch(-1,-1);
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
