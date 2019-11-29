package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

// Tiled map imports
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Class imports
import com.kroy.Kroy;
import com.sprites.MovementSprite;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.SCORE_Y;
import static com.config.Constants.SCORE_X;

public class GameScreen implements Screen {
  	final Kroy game;

	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;
	int score;
	MovementSprite testSprite;
	Texture texture;
	Batch batch;

	public GameScreen(final Kroy gam) {
		this.game = gam;

		// create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		game.init(camera);

		// load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
		// Update map asset location later
		map = new TmxMapLoader().load("MapAssets/KroyMap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1f / 1f);

		// create sprite
		batch = renderer.getBatch();
		texture = new Texture("badlogic.jpg");
		testSprite = new MovementSprite(batch, texture, 2000, 500, (TiledMapTileLayer) map.getLayers().get("River"));
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set the TiledMapRenderer view based on what the
		// camera sees, and render the map
		renderer.setView(camera);
		renderer.render();

		// Draw score
		game.drawFont("Score: " + testSprite.getX(), SCORE_X, SCORE_Y);

		// Draw FPS
		game.drawFont("FPS: " + Gdx.graphics.getFramesPerSecond(), SCREEN_WIDTH - SCORE_X * 2, SCORE_Y);

		// tell the camera to update its matrices.
		float lerp = 1f;
		Vector3 position = camera.position;
		position.x += (testSprite.getCentreX() - position.x) * lerp * Gdx.graphics.getDeltaTime();
		position.y += (testSprite.getCentreY() - position.y) * lerp * Gdx.graphics.getDeltaTime();
		camera.update();

		// Draw and move sprite
		testSprite.update();
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
		testSprite.dispose();
	}

}