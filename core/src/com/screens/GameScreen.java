package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

// Class imports
import com.kroy.Kroy;
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.SCORE_Y;
import static com.config.Constants.SCORE_X;

public class GameScreen implements Screen {
  	final Kroy game;

	OrthographicCamera camera;
	int score;
	SimpleSprite testSprite;
	Texture texture;
	SpriteBatch batch;

	public GameScreen(final Kroy gam) {
		this.game = gam;

		// create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		// create sprite
		batch = new SpriteBatch();
		texture = new Texture("badlogic.jpg");
		testSprite = new SimpleSprite(batch, texture);
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		game.init(camera);

		// Draw score
		game.drawFont("Score: " + score, SCORE_X, SCORE_Y);

		// Draw sprite
        testSprite.drawSprite(texture, 100, 100);
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
		texture.dispose();
	}

}