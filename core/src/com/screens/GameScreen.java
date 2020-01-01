package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.classes.Firestation;
import com.classes.ETFortress;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.SCORE_Y;
import static com.config.Constants.SCORE_X;
import static com.config.Constants.LERP;
import static com.config.Constants.MIN_ZOOM;
import static com.config.Constants.MAX_ZOOM;
import static com.config.Constants.MAP_SCALE;
import static com.config.Constants.FIRETRUCK_ACCELERATION;
import static com.config.Constants.DEBUG_ENABLED;

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
	private float zoomDelay;

	// Private arrays to group sprites
	private ArrayList<Firetruck> firetrucks;
	private ArrayList<ETFortress> ETFortresses;
	private Firestation firestation;

	/**
	 * The constructor for the main game screen. All main game logic is
	 * contained.
	 * 
	 * @param gam The game object.
	 */
	public GameScreen(final Kroy gam) {
		this.game = gam;

		// ---- 1) Create new instance for all the objects needed for the game ---- //
		
		// Create an orthographic camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Load the map, set the unit scale
		map = new TmxMapLoader().load("MapAssets/York_galletcity.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, MAP_SCALE);
		shapeRenderer = new ShapeRenderer();

		// ---- 2) Initialise and set game properties ----------------------------- //

		// Initialise map renderer as batch to draw textures to
		batch = renderer.getBatch();

		// Set the game batch
		this.game.setBatch(batch);
		
		// Set the Batch to render in the coordinate system specified by the camera.
		this.batch.setProjectionMatrix(camera.combined);

		// ---- 3) Construct all textures to be used in the game here, ONCE ------ //

		// Initialise textures to use for spites
		Texture firestationTexture = new Texture("FiretruckSlices/tile008.png");
		Texture ETFortressTexture = new Texture("FiretruckSlices/tile009.png");
		
		// Create array of textures for firetruck animations
		ArrayList<Texture> firetruckSlices = new ArrayList<Texture>();
		for (int i = 19; i > 0; i--) {
			if (i == 5) { // Texture 5 contains identical slices except the lights are different
				Texture texture = new Texture("FiretruckSlices/tile05A.png");
				firetruckSlices.add(texture);
				texture = new Texture("FiretruckSlices/tile05B.png");
				firetruckSlices.add(texture);
			} else {
				Texture texture = new Texture("FiretruckSlices/tile0" + (i < 10 ? "0" + i:i) + ".png");
				firetruckSlices.add(texture);
			}
		}

		// ---- 4) Create entities that will be around for entire game duration - //

		// Create a new firestation 
		this.firestation = new Firestation(firestationTexture, 1200, 500);

		// Initialise firetrucks array and add firetrucks to it
		this.firetrucks = new ArrayList<Firetruck>();
		for (int i = 1; i <= 2; i++) {
			Firetruck firetruck = new Firetruck(firetruckSlices, 1100 * i, 650, (TiledMapTileLayer) map.getLayers().get("Buildings"), i);
			this.firetrucks.add(firetruck);
		}

		// Initialise ETFortresses array and add ETFortresses to it
		this.ETFortresses = new ArrayList<ETFortress>();
		for (int i = 1; i <= 1; i++) {
			ETFortress ETFortress = new ETFortress(ETFortressTexture, 1750 * i, 500);
			this.ETFortresses.add(ETFortress);
		}
	}

	/**
	 * Actions to perform on first render cycle of the game
	 */
	@Override
	public void show() {
		// Set inital firetruck to focus the camera on
		setFiretruckFocus(1);
		this.zoomDelay = 0;
	}

	/**
	 * Render function to display all elements in the main game.
	 * 
	 * @param delta The delta time of the game, updated every game second rather than frame.
	 */
	@Override
	public void render(float delta) {

		// MUST BE FIRST: Clear the screen each frame to stop textures blurring
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// ---- 1) Update camera and map properties each iteration -------- //
		
		// Set the TiledMapRenderer view based on what the camera sees, and render the map
		renderer.setView(camera);
		renderer.render();

		// Align the debug view with the camera
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Get the firetruck thats being driven so that the camera can follow it
		Firetruck focusedTruck = getFiretruckInFocus();

		// Tell the camera to update to the sprites position with a delay based on lerp and game time
		Vector3 cameraPosition = camera.position;
		float xDifference = focusedTruck.getCentreX() - cameraPosition.x;
		float yDifference = focusedTruck.getCentreY() - cameraPosition.y;
		cameraPosition.x += xDifference * LERP * delta;
		cameraPosition.y += yDifference * LERP * delta;
		
		// Zoom the camera out when firetruck moves
		float maxZoomHoldTime = MAX_ZOOM * 6, zoomSpeed = MIN_ZOOM * 0.01f, timeIncrement = MIN_ZOOM * 0.1f; 
		double speed = Math.max(Math.abs(focusedTruck.getSpeedX()), Math.abs(focusedTruck.getSpeedY()));
		boolean isMoving = speed > FIRETRUCK_ACCELERATION * 10;
		// If moving, increase delay before zooming out up until the limit
		if (isMoving && this.zoomDelay < maxZoomHoldTime) {
			this.zoomDelay += timeIncrement;
		} else if (this.zoomDelay > MIN_ZOOM) {
			this.zoomDelay -= 0.1f;
		}
		// If delay has been reached, zoom out, then hold until stationary
		if (this.zoomDelay > maxZoomHoldTime / 4) {
			camera.zoom = camera.zoom + zoomSpeed > MAX_ZOOM ? MAX_ZOOM : camera.zoom + zoomSpeed;
		} else if (camera.zoom > MIN_ZOOM) {
			camera.zoom -= zoomSpeed * 2;
		}
		camera.update();

		// ---- 2) Perform any checks for user input ---------------------- //

		// Check for user input to see if the focused truck should change
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
            setFiretruckFocus(1);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
            setFiretruckFocus(2);
        }

		// ---- 3) Perform all render functions here ------------------------ //

		// Set font scale
		this.game.getFont().getData().setScale(camera.zoom * 1.5f);

		// Ready the batch's for drawing
		batch.begin();
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		
		// Draw the score and FPS to the screen at given co-ordinates
		game.drawFont("Score: " + score, cameraPosition.x - SCORE_X * camera.zoom, cameraPosition.y + SCORE_Y * camera.zoom);
		if (DEBUG_ENABLED) game.drawFont("FPS: "
			+ Gdx.graphics.getFramesPerSecond(),
			cameraPosition.x + SCORE_X * 0.85f * camera.zoom,
			cameraPosition.y + SCORE_Y * camera.zoom
		);

		// Call the update function of the sprites to draw and update them
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.update(batch);
			if (DEBUG_ENABLED) firetruck.drawDebug(shapeRenderer);
		}
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.update(batch);
			if (DEBUG_ENABLED) ETFortress.drawDebug(shapeRenderer);
		}
		this.firestation.update(batch);
		if (DEBUG_ENABLED) firestation.drawDebug(shapeRenderer);
		
		// Finish rendering 
		if (DEBUG_ENABLED) shapeRenderer.end();
		batch.end();

		// ---- 4) Perform any calulcation needed after sprites are drawn - //

		// Check for any collisions
		checkForCollisions();
	}

	/**
     * Checks to see if any collisions have occurred
     */
	private void checkForCollisions() {
		// Vector to store the minimum movement to seperate two sprites
		Intersector.MinimumTranslationVector seperationVector = new Intersector.MinimumTranslationVector();
		// Check each firetruck to see if it has collided with anything
		for (Firetruck firetruckA : this.firetrucks) {
			for (Firetruck firetruckB : this.firetrucks) {
				// Check if the firetruck overlaps another firetruck, but not itself
				if (!firetruckA.equals(firetruckB) && Intersector.overlapConvexPolygons(firetruckA.getHitBox(), firetruckB.getHitBox(), seperationVector)) {
					firetruckA.collisionOccurred(seperationVector.normal);
					firetruckA.getHealthBar().subtractResourceAmount(5);
				}
			}
			// Check if it overlaps with an ETFortress
			for (ETFortress ETFortress : this.ETFortresses) {
				if (Intersector.overlapConvexPolygons(firetruckA.getHitBox(), ETFortress.getHitBox(), seperationVector)) {
					firetruckA.collisionOccurred(seperationVector.normal);
				}
				if (firetruckA.isInHoseRange(ETFortress.getHitBox())) {
					ETFortress.getHealthBar().subtractResourceAmount(1);
				}
			}
			// Check if it is in the firestation's radius. Only repair the truck if it needs repairing.
			// Allows multiple trucks to be in the radius and be repaired.
			if (firetruckA.isDamaged() && this.firestation.isInRadius(firetruckA.getHitBox())) {
				this.firestation.repair(firetruckA);
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
			if (firetruck.isFocused()) {
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
		this.firestation.dispose();
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.dispose();
		}
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.dispose();
		}
	}

}