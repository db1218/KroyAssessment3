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
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

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
import com.classes.Projectile;
import com.classes.Firestation;
import com.classes.ETFortress;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.FONT_Y;
import static com.config.Constants.SCORE_X;
import static com.config.Constants.TIME_X;
import static com.config.Constants.LERP;
import static com.config.Constants.MIN_ZOOM;
import static com.config.Constants.MAX_ZOOM;
import static com.config.Constants.MAP_SCALE;
import static com.config.Constants.TILE_DIMS;
import static com.config.Constants.DEBUG_ENABLED;
import static com.config.Constants.FiretruckOneProperties;
import static com.config.Constants.FiretruckTwoProperties;
import static com.config.Constants.FIRETRUCK_DAMAGE;
import static com.config.Constants.PROJECTILE_DAMAGE;

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
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Batch batch;

	// Private values for tiled map
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private int[] foregroundLayers;
    private int[] backgroundLayers;

	// Private values for the game
	private int score, time, focusedID;
	private float zoomDelay;
	private Texture projectileTexture;

	// Private arrays to group sprites
	private ArrayList<Firetruck> firetrucks;
	private ArrayList<Firetruck> firetrucksToRemove;
	private ArrayList<ETFortress> ETFortresses;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> projectilesToRemove;
	private Firestation firestation;

	/**
	 * The constructor for the main game screen. All main game logic is
	 * contained.
	 * 
	 * @param gam The game object.
	 */
	public GameScreen(final Kroy gam) {
		// Assign the game to a property so it can be used when transitioning screens
		this.game = gam;

		// ---- 1) Create new instance for all the objects needed for the game ---- //
		
		// Create an orthographic camera
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Load the map, set the unit scale
		this.map = new TmxMapLoader().load("MapAssets/York_galletcity.tmx");
		this.renderer = new OrthogonalTiledMapRenderer(map, MAP_SCALE);
		this.shapeRenderer = new ShapeRenderer();

		// Create an array to store all projectiles in motion
		this.projectiles = new ArrayList<Projectile>();

		// Decrease time every second, starting at 3 minutes
		this.time = 3 * 60;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				decreaseTime();
			}
		}, 1, 1 );

		Gdx.input.setInputProcessor(new GameInputHandler(this));

		// ---- 2) Initialise and set game properties ----------------------------- //

		// Initialise map renderer as batch to draw textures to
		this.batch = renderer.getBatch();

		// Set the game batch
		this.game.setBatch(this.batch);
		
		// Set the Batch to render in the coordinate system specified by the camera.
		this.batch.setProjectionMatrix(this.camera.combined);

		// ---- 3) Construct all textures to be used in the game here, ONCE ------ //

		// Select background and foreground map layers, order matters
        MapLayers mapLayers = map.getLayers();
        this.foregroundLayers = new int[] {
			mapLayers.getIndex("Buildings"),
			mapLayers.getIndex("Trees"),
        };
        this.backgroundLayers = new int[] {
			mapLayers.getIndex("River"),
			mapLayers.getIndex("Road")
        };

		// Initialise textures to use for spites
		Texture firestationTexture = new Texture("MapAssets/UniqueBuildings/firestation.png");
		Texture cliffordsTowerTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower.png");
		Texture cliffordsTowerWetTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower_wet.png");
		Texture railstationTexture = new Texture("MapAssets/UniqueBuildings/railstation.png");
		Texture railstationWetTexture = new Texture("MapAssets/UniqueBuildings/railstation_wet.png");
		Texture yorkMinisterTexture = new Texture("MapAssets/UniqueBuildings/Yorkminster.png");
		Texture yorkMinisterWetTexture = new Texture("MapAssets/UniqueBuildings/Yorkminster_wet.png");
		this.projectileTexture = new Texture("alienProjectile.png");
		
		// Create arrays of textures for animations
		ArrayList<Texture> waterFrames = new ArrayList<Texture>();
		ArrayList<Texture> firetruckBlue = new ArrayList<Texture>();
		ArrayList<Texture> firetruckRed = new ArrayList<Texture>();

		for (int i = 1; i <= 3; i++) {
			Texture texture = new Texture("waterSplash" + i + ".png");
			waterFrames.add(texture);
		}
		for (int i = 20; i > 0; i--) {
			if (i == 6) { // Texture 5 contains identical slices except the lights are different
				Texture blue = new Texture("FiretruckBlue/FiretruckBLUE (6) A.png");
				Texture red = new Texture("FiretruckRed/FiretruckRED (6) A.png");
				firetruckBlue.add(blue);
				firetruckRed.add(red);
				blue = new Texture("FiretruckBlue/FiretruckBLUE (6) B.png");
				red = new Texture("FiretruckRed/FiretruckRED (6) B.png");
				firetruckBlue.add(blue);
				firetruckRed.add(red);
			} else {
				Texture blue = new Texture("FiretruckBlue/FiretruckBLUE (" + i + ").png");
				Texture red = new Texture("FiretruckRed/FiretruckRED (" + i + ").png");
				firetruckBlue.add(blue);
				firetruckRed.add(red);
			}
		}

		// ---- 4) Create entities that will be around for entire game duration - //

		// Create a new firestation 
		this.firestation = new Firestation(firestationTexture, 77 * TILE_DIMS, 36 * TILE_DIMS);

		// Initialise firetrucks array and add firetrucks to it
		this.firetrucks = new ArrayList<Firetruck>();
		this.firetrucks.add(new Firetruck(firetruckBlue, waterFrames, FiretruckOneProperties, (TiledMapTileLayer) map.getLayers().get("Collision"), 1, 80 * TILE_DIMS, 30 * TILE_DIMS));
		this.firetrucks.add(new Firetruck(firetruckRed, waterFrames, FiretruckTwoProperties, (TiledMapTileLayer) map.getLayers().get("Collision"), 2, 80 * TILE_DIMS, 32 * TILE_DIMS));

		// Initialise ETFortresses array and add ETFortresses to it
		this.ETFortresses = new ArrayList<ETFortress>();
		this.ETFortresses.add(new ETFortress(cliffordsTowerTexture, cliffordsTowerWetTexture, 1, 1, 69 * TILE_DIMS, 51 * TILE_DIMS));
		this.ETFortresses.add(new ETFortress(yorkMinisterTexture, yorkMinisterWetTexture, 2, 3.25f, 68.25f * TILE_DIMS, 82.25f * TILE_DIMS));
		this.ETFortresses.add(new ETFortress(railstationTexture, railstationWetTexture, 2, 2.5f, 1 * TILE_DIMS, 72.75f * TILE_DIMS));
	}

	/**
	 * Actions to perform on first render cycle of the game
	 */
	@Override
	public void show() {
		// Set inital firetruck to focus the camera on
		this.focusedID = 1;
		setFireStationFocus(this.focusedID);

		// Zoom delay is the time before the camera zooms out
		this.zoomDelay = 0;

		// Start the camera near the firestation
		this.camera.position.x = 80 * TILE_DIMS;
		this.camera.position.y = 30 * TILE_DIMS;

		// Create array to collect entities that are no longer used
		this.projectilesToRemove = new ArrayList<Projectile>();
		this.firetrucksToRemove = new ArrayList<Firetruck>();
	}

	/**
	 * Render function to display all elements in the main game.
	 * 
	 * @param delta The delta time of the game, updated every game second rather than frame.
	 */
	@Override
	public void render(float delta) {

		// MUST BE FIRST: Clear the screen each frame to stop textures blurring
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// ---- 1) Update camera and map properties each iteration -------- //
		
		// Set the TiledMapRenderer view based on what the camera sees
		renderer.setView(this.camera);

		// Align the debug view with the camera
		shapeRenderer.setProjectionMatrix(this.camera.combined);

		// Get the firetruck thats being driven so that the camera can follow it
		Firetruck focusedTruck = getFiretruckInFocus();

		// Tell the camera to update to the sprites position with a delay based on lerp and game time
		Vector3 cameraPosition = this.camera.position;
		float xDifference = focusedTruck.getCentreX() - cameraPosition.x;
		float yDifference = focusedTruck.getCentreY() - cameraPosition.y;
		cameraPosition.x += xDifference * LERP * delta;
		cameraPosition.y += yDifference * LERP * delta;
		
		// Zoom the camera out when firetruck moves
		float maxZoomHoldTime = MAX_ZOOM * 4, zoomSpeed = MIN_ZOOM * 0.01f, timeIncrement = MIN_ZOOM * 0.1f; 
		double speed = Math.max(Math.abs(focusedTruck.getSpeed().x), Math.abs(focusedTruck.getSpeed().y));
		boolean isMovingOrSpraying = speed > focusedTruck.getMaxSpeed() / 2  || focusedTruck.isSpraying();
//		// If moving, increase delay before zooming out up until the limit
//		if (isMovingOrSpraying && this.zoomDelay < maxZoomHoldTime) {
//			this.zoomDelay += timeIncrement;
//		} else if (this.zoomDelay > MIN_ZOOM) {
//			this.zoomDelay -= 0.1f;
//		}
		// If delay has been reached, zoom out, then hold until stationary
		if (this.zoomDelay > maxZoomHoldTime / 4) {
			this.camera.zoom = Math.min(this.camera.zoom + zoomSpeed, MAX_ZOOM);
		} else if (this.camera.zoom > MIN_ZOOM) {
			this.camera.zoom -= zoomSpeed * 2;
		}
		this.camera.update();

		// Set font scale
		this.game.getFont().getData().setScale(this.camera.zoom * 1.5f);

		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			System.out.println("hi");
			this.focusedID += 1;
			if (this.focusedID > this.firetrucks.size()) {
				this.focusedID = 1;
			}
			setFireStationFocus(this.focusedID);
		}

		// ---- 3) Draw background, firetruck then foreground layers ----- //

		// Render background map layers
		renderer.render(this.backgroundLayers);

		// Render the firetrucks on top of the background
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		batch.begin();

		// Call the update function of the sprites to draw and update them
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.update(batch, this.camera);
			if (firetruck.getHealthBar().getCurrentAmount() <= 0) this.firetrucksToRemove.add(firetruck);
			if (DEBUG_ENABLED) firetruck.drawDebug(shapeRenderer);
		}

		// Close layer 
		batch.end();
		if (DEBUG_ENABLED) shapeRenderer.end();

		// Render map foreground layers
		renderer.render(foregroundLayers);

		// Render the remaining sprites, font last to be on top of all
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		batch.begin();

		// Render sprites
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.update(batch);
			if (DEBUG_ENABLED) ETFortress.drawDebug(shapeRenderer);
		}
		for (Projectile projectile : this.projectiles) {
			projectile.update(batch);
			if (DEBUG_ENABLED) projectile.drawDebug(shapeRenderer);
			if (projectile.isOutOfMap()) this.projectilesToRemove.add(projectile);
		}
		this.firestation.update(batch);
		if (DEBUG_ENABLED) firestation.drawDebug(shapeRenderer);

		// Draw the score, time and FPS to the screen at given co-ordinates
		game.drawFont("Score: " + this.score,
			cameraPosition.x - this.camera.viewportWidth * SCORE_X * camera.zoom,
			cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom);
		game.drawFont("Time: " + this.time, 
			cameraPosition.x + this.camera.viewportWidth * TIME_X * camera.zoom,
			cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom);
		if (DEBUG_ENABLED) game.drawFont("FPS: "
			+ Gdx.graphics.getFramesPerSecond(),
			cameraPosition.x + this.camera.viewportWidth * TIME_X * camera.zoom,
			cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom - 30
		);

		// Finish rendering
		batch.end();
		if (DEBUG_ENABLED) shapeRenderer.end();

		// ---- 4) Perform any calulcation needed after sprites are drawn - //

		// Remove projectiles that are off the screen and firetrucks that are dead
		this.projectiles.removeAll(this.projectilesToRemove);
		this.firetrucks.removeAll(this.firetrucksToRemove);

		// Check for any collisions
		checkForCollisions();

		// Check if the game should end
		checkIfGameOver();
	}

	/**
     * Checks to see if the player has won or lost the game. Navigates back to the main menu
	 * if they won or lost.
     */
	private void checkIfGameOver() {
		boolean gameWon = true, gameLost = true;
		// Check if any firetrucks are still alive
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getHealthBar().getCurrentAmount() > 0) gameLost = false;
		}
		// Check if any fortresses are still alive
		for (ETFortress ETFortress : this.ETFortresses) {
			if (ETFortress.getHealthBar().getCurrentAmount() > 0) gameWon = false;
		}
		if (gameWon || gameLost) {
			dispose();
			this.game.setScreen(new MainMenuScreen(this.game));
		}
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
				}
			}
			// Check if it overlaps with an ETFortress
			for (ETFortress ETFortress : this.ETFortresses) {
				if (ETFortress.getHealthBar().getCurrentAmount() > 0 && firetruckA.isInHoseRange(ETFortress.getHitBox())) {
					ETFortress.getHealthBar().subtractResourceAmount(FIRETRUCK_DAMAGE);
					this.score += 10;
				}
				if (ETFortress.isInRadius(firetruckA.getHitBox()) && ETFortress.canShootProjectile()) {
					Projectile projectile = new Projectile(this.projectileTexture, ETFortress.getCentreX(), ETFortress.getCentreY());
					projectile.calculateTrajectory(firetruckA.getHitBox());
					this.projectiles.add(projectile);
				}
			}
			// Check if firetruck is hit with a projectile
			for (Projectile projectile : this.projectiles) {
				if (Intersector.overlapConvexPolygons(firetruckA.getHitBox(), projectile.getHitBox())) {
					firetruckA.getHealthBar().subtractResourceAmount(PROJECTILE_DAMAGE);
					if (this.score > 10) this.score -= 10;
					projectilesToRemove.add(projectile);
				}
			}
			// Check if it is in the firestation's radius. Only repair the truck if it needs repairing.
			// Allows multiple trucks to be in the radius and be repaired or refilled every second.
			if (this.time > 0 && (firetruckA.isDamaged() || firetruckA.isLowOnWater()) && this.firestation.isInRadius(firetruckA.getHitBox())) {
				this.firestation.repair(firetruckA);
			}
		}
	}

	/**
	 * Decreases time by 1, called every second by the timer
	 */
	private void decreaseTime() {
		if (this.time > 0) this.time -= 1;
	}

	/**
	 * Get the firetruck the user is currently controlling.
	 * 
	 * @return The firetruck with user's focus.
	 */
	public Firetruck getFiretruckInFocus() {
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.isFocused() && firetruck.getHealthBar().getCurrentAmount() > 0) {
				return firetruck;
			}
		}
		// If no firetruck alive focus next one
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getHealthBar().getCurrentAmount() > 0) {
				firetruck.setFocus(firetruck.getFocusID());
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

	public void setFireStationFocus(int focusID) {
		/* TODO we want this method to move the camera to the station when a fire truck dies,
		when it is there it should open the new station GUI */
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getHealthBar().getCurrentAmount() > 0) {
				firetruck.setFocus(focusID);
			}
		}
	}

	/**
	 * Resize the screen.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	@Override
	public void resize(int width, int height) {
		this.camera.viewportHeight = height;
		this.camera.viewportWidth = width;
        this.camera.update();
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
		this.projectileTexture.dispose();
		this.firestation.dispose();
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.dispose();
		}
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.dispose();
		}
	}

}