package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

// Tiled map imports fro LibGDX
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Class imports
import com.kroy.Kroy;
import com.classes.Firetruck;
import com.classes.ETFortress;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.SCORE_Y;
import static com.config.Constants.SCORE_X;
import static com.config.Constants.LERP;

/**
 * Display the main game.
 * 
 * @author Archie
 * @since 23/11/2019
 */
public class GameScreen implements Screen {
	  
	// A constant variable to store the game
	final Kroy game;

	// Private values for game screen logic
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Texture texture;
	private Batch batch;

	// Private values for the game
	private int score;

	// Private values for each firetruck
	private Firetruck fireTruckOne;
	private Firetruck fireTruckTwo;
	
	// Private values for each ETFortress
	private ETFortress ETFotressOne;

	/**
	 * The constructor for the main game screen. All main game logic is
	 * contained.
	 * 
	 * @param gam The game object.
	 */
	public GameScreen(final Kroy gam) {
		this.game = gam;

		// Create an orthographic camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		game.init(camera);

		// Load the map, set the unit scale
		map = new TmxMapLoader().load("MapAssets/KroyMap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1f / 1f);

		// Initalise textures and batch and then create a sprite
		batch = renderer.getBatch();
		texture = new Texture("badlogic.jpg");

		// Initialise fire trucks and set inital focus
		fireTruckOne = new Firetruck(batch, texture, 1000, 500, (TiledMapTileLayer) map.getLayers().get("River"), 1);
		fireTruckTwo = new Firetruck(batch, texture, 2000, 500, (TiledMapTileLayer) map.getLayers().get("River"), 2);
		setFiretruckFocus(1);

		// Initialise ETFortresses
		ETFotressOne = new ETFortress(batch, texture, 1500, 500);
	}

	/**
	 * Render function to display all elements in the main game.
	 * 
	 * @param delta The delta time of the game, updated every game second rather than frame.
	 */
	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1] of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set the TiledMapRenderer view based on what the camera sees, and render the map
		renderer.setView(camera);
		renderer.render();

		// Draw score to the screen at given co-ordinates
		game.drawFont("Score: " + score, SCORE_X, SCORE_Y);

		// Draw FPS to the screen at given co-ordinates
		game.drawFont("FPS: " + Gdx.graphics.getFramesPerSecond(), SCREEN_WIDTH - SCORE_X * 2, SCORE_Y);

		// Check for user input to see if the focused truck should change
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
            setFiretruckFocus(1);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
            setFiretruckFocus(2);
        }

		// Get the firetruck thats being driven
		Firetruck focusedTruck = getFiretruckInFocus();

		// Tell the camera to update to the sprites position with a delay based on lerp and game time
		Vector3 position = camera.position;
		position.x += (focusedTruck.getCentreX() - position.x) * LERP * delta;
		position.y += (focusedTruck.getCentreY() - position.y) * LERP * delta;
		camera.update();

		// Call the update function of the sprites to draw and update it
		fireTruckOne.update();
		fireTruckTwo.update();
		ETFotressOne.update();
	}

	/**
	 * Get the firetruck the user is currently controlling.
	 * 
	 * @return The firetruck with user's focus.
	 */
	private Firetruck getFiretruckInFocus() { 
		return fireTruckOne.getFocus() ? fireTruckOne : fireTruckTwo;
	}

	/**
	 * Set which firetruck the user is currently controlling.
	 * 
	 * @param focusID The ID of the firetruck to focus on.
	 */
	private void setFiretruckFocus(int focusID) {
		fireTruckOne.setFocus(focusID);
		fireTruckTwo.setFocus(focusID);
	}

	/**
	 * Resize the screen.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	@Override
	public void resize(int width, int height) {
	}

	/**
	 * Actions to perform when the main game is shown.
	 */
	@Override
	public void show() {
	}

	/**
	 * Actions to perform when the main game is hidden.
	 */
	@Override
	public void hide() {
	}

	/**
	 * Actions to perform when the main game is paused.
	 */
	@Override
	public void pause() {
	}

	/**
	 * Actions to perform when the main game is resumed.
	 */
	@Override
	public void resume() {
	}

	/**
	 * Dispose of main game assets upon completion.
	 */
	@Override
	public void dispose() {
		texture.dispose();
		fireTruckOne.dispose();
		fireTruckTwo.dispose();
	}

}