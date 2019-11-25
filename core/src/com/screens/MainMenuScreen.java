package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

// Class imports
import com.kroy.Kroy;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.SCREEN_CENTRE_X;
import static com.config.Constants.SCREEN_CENTRE_Y;

public class MainMenuScreen implements Screen {
    
    final Kroy game;
	OrthographicCamera camera;

	public MainMenuScreen(final Kroy gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.init(camera);
        game.drawFont(
			new String[] {"Welcome to Kroy!", "Tap anywhere to begin!"}, 
			new Double[] {SCREEN_CENTRE_X - 90.0, SCREEN_CENTRE_X - 120.0}, 
			new Double[] {SCREEN_CENTRE_Y, SCREEN_CENTRE_Y - 50.0}
		);

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}