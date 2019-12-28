package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;

// Tiled map imports fro LibGDX
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Java util imports
import java.util.ArrayList;

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
import static com.config.Constants.MAP_SCALE;

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
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Batch batch;

	// Private values for the game
	private int score;

	// Private arrays to group sprites
	private ArrayList<Firetruck> firetrucks;
	private ArrayList<ETFortress> ETFortresses;

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
		map = new TmxMapLoader().load("MapAssets/York_galletcity.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, MAP_SCALE);
		shapeRenderer = new ShapeRenderer();

		// Initialise batch to draw texture to
		batch = renderer.getBatch();

		// Initialise texture to use for firetruck
		Texture firetruckTexture = new Texture("FiretruckSlices/tile000.png");

		// Initialise firetrucks array and add firetrucks to it
		this.firetrucks = new ArrayList<Firetruck>();
		for (int i = 1; i <= 2; i++) {
			Firetruck firetruck = new Firetruck(batch, firetruckTexture, 1000 * i, 650, (TiledMapTileLayer) map.getLayers().get("Buildings"), i);
			this.firetrucks.add(firetruck);
		}

		// Set inital firetruck to focus
		setFiretruckFocus(1);

		// Initialise texture to use for ETFortress
		Texture ETFortressTexture = new Texture("FiretruckSlices/tile008.png");

		// Initialise ETFortresses array and add ETFortresses to it
		this.ETFortresses = new ArrayList<ETFortress>();
		for (int i = 1; i <= 1; i++) {
			ETFortress ETFortress = new ETFortress(batch, ETFortressTexture, 1500 * i, 500);
			this.ETFortresses.add(ETFortress);
		}
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

		shapeRenderer.setProjectionMatrix(camera.combined);

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
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.update();
			firetruck.drawDebug(shapeRenderer);
		}
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.update();
			ETFortress.drawDebug(shapeRenderer);
		}

		// Check for any collisions
		checkForCollisions();
	}

	/**
     * Checks to see if any collisions have occurred
     */
	private void checkForCollisions() {
		Intersector.MinimumTranslationVector seperationVector = new Intersector.MinimumTranslationVector();
		// Check each firetruck to see if it has collided with anything
		for (Firetruck firetruckA : this.firetrucks) {
			for (Firetruck firetruckB : this.firetrucks) {
				// Check if the firetruck overlaps another firetruck, but not itself
				if (!firetruckA.equals(firetruckB) && Intersector.overlapConvexPolygons(firetruckA.hitBox, firetruckB.hitBox, seperationVector)) {
					firetruckA.collisionOccurred(seperationVector.normal);
				}
			}
			// Check if it overlaps with an ETFortress
			for (ETFortress ETFortress : this.ETFortresses) {
				if (Intersector.overlapConvexPolygons(firetruckA.hitBox, ETFortress.hitBox, seperationVector)) {
					firetruckA.collisionOccurred(seperationVector.normal);
				}
			}
		}
	}

	/**
	 * Get the firetruck the user is currently controlling.
	 * 
	 * @return The firetruck with user's focus.
	 */
	private Firetruck getFiretruckInFocus() {
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getFocus()) {
				return firetruck;
			}
		}
		return this.firetrucks.get(0);
	}

	/**
	 * Set which firetruck the user is currently controlling.
	 * 
	 * @param focusID The ID of the firetruck to focus on.
	 */
	private void setFiretruckFocus(int focusID) {
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.setFocus(focusID);
		}
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
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.dispose();
		}
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.dispose();
		}
	}

}