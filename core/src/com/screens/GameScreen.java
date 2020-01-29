package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.config.Constants;
import com.kroy.Kroy;
import com.classes.Firetruck;
import com.classes.Projectile;
import com.classes.Firestation;
import com.classes.ETFortress;

// Constants import
import static com.config.Constants.*;


/**
 * Display the main game.
 *
 * @author Archie
 * @since 23/11/2019
 */
public class GameScreen implements Screen {

	// A constant variable to store the game
	final Kroy game;;

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
	private int score;
	private int time;
	private Texture projectileTexture;

	private float zoomTarget;

	// Private arrays to group sprites
	private ArrayList<Firetruck> firetrucksToRemove;
	private ArrayList<ETFortress> ETFortresses;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> projectilesToRemove;
	private Firestation firestation;

	private GameInputHandler gameInputHandler;

	private ArrayList<Texture> waterFrames;
	/**
	 * The constructor for the main game screen. All main game logic is
	 * contained.
	 *
	 * @param game The game object.
	 */
	public GameScreen(final Kroy game) {
		// Assign the game to a property so it can be used when transitioning screens
		this.game = game;

		// ---- 1) Create new instance for all the objects needed for the game ---- //

		// Create an orthographic camera
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

		gameInputHandler = new GameInputHandler(this);

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
			mapLayers.getIndex("Buildings")
        };
        this.backgroundLayers = new int[] {
			mapLayers.getIndex("River"),
			mapLayers.getIndex("Road"),
			mapLayers.getIndex("Trees")
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
		waterFrames = new ArrayList<Texture>();

		for (int i = 1; i <= 3; i++) {
			Texture texture = new Texture("waterSplash" + i + ".png");
			waterFrames.add(texture);
		}

		// ---- 4) Create entities that will be around for entire game duration - //

		// Create a new firestation
		this.firestation = new Firestation(firestationTexture, 77 * TILE_DIMS, 36 * TILE_DIMS, game, this);

		// Initialise firetrucks array and add firetrucks to it
		constructFireTruck(Constants.TruckColours.RED, true, FiretruckOneProperties);
		constructFireTruck(Constants.TruckColours.BLUE, false, FiretruckTwoProperties);
		constructFireTruck(TruckColours.YELLOW, false, FiretruckOneProperties);

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
		// Zoom that the user has set with their scroll wheel
		this.zoomTarget = 1.2f;

		// Start the camera near the firestation
		this.camera.setToOrtho(false);
		this.camera.position.set(firestation.getSpawnLocation().x, firestation.getSpawnLocation().y, 0);

		// Create array to collect entities that are no longer used
		this.projectilesToRemove = new ArrayList<Projectile>();

		Gdx.input.setInputProcessor(gameInputHandler);
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
		Firetruck focusedTruck = this.firestation.getActiveFireTruck();

		// Tell the camera to update to the sprites position with a delay based on lerp and game time
		Vector3 cameraPosition = this.camera.position;
		float xDifference = focusedTruck.getCentreX() - cameraPosition.x;
		float yDifference = focusedTruck.getCentreY() - cameraPosition.y;
		cameraPosition.x += xDifference * LERP * delta;
		cameraPosition.y += yDifference * LERP * delta;

		if (this.camera.zoom - 0.005f > zoomTarget) {
			this.camera.zoom -= 0.005f;
		} else if (this.camera.zoom + 0.005f < zoomTarget) {
			this.camera.zoom += 0.005f;
		}

		this.camera.update();

		// Set font scale
		this.game.getFont().getData().setScale(this.camera.zoom * 1.5f);

		// ---- 3) Draw background, firetruck then foreground layers ----- //

		// Render background map layers
		renderer.render(this.backgroundLayers);

		// Render the firetrucks on top of the background
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		batch.begin();

		// Call the update function of the sprites to draw and update them
		firestation.updateFiretruck(this.batch, this.shapeRenderer, this.camera);

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

		// Check for any collisions
		checkForCollisions();

		// Check if the game should end
		checkIfGameOver();

		checkIfCarpark();
	}

	private void checkIfCarpark() {
		if (this.firestation.isMenuOpen()) {
			game.setScreen(this.firestation.getCarparkScreen());
		}
	}

	/**
     * Checks to see if the player has won or lost the game. Navigates back to the main menu
	 * if they won or lost.
     */
	private void checkIfGameOver() {
		boolean gameWon = true, gameLost = true;
		// Check if any firetrucks are still alive
		if (this.firestation.hasParkedFiretrucks() || this.firestation.getActiveFireTruck().isAlive()) gameLost = false;

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
		// Check each firetruck to see if it has collided with anything
		Firetruck firetruck = this.firestation.getActiveFireTruck();
		// Check if it overlaps with an ETFortress
		for (ETFortress ETFortress : this.ETFortresses) {
			if (ETFortress.getHealthBar().getCurrentAmount() > 0 && firetruck.isInHoseRange(ETFortress.getHitBox())) {
				ETFortress.getHealthBar().subtractResourceAmount(FIRETRUCK_DAMAGE);
				this.score += 10;
			}
			if (ETFortress.isInRadius(firetruck.getHitBox()) && ETFortress.canShootProjectile()) {
				Projectile projectile = new Projectile(this.projectileTexture, ETFortress.getCentreX(), ETFortress.getCentreY());
				projectile.calculateTrajectory(firetruck.getHitBox());
				this.projectiles.add(projectile);
			}
		}
		// Check if firetruck is hit with a projectile
		for (Projectile projectile : this.projectiles) {
			if (Intersector.overlapConvexPolygons(firetruck.getHitBox(), projectile.getHitBox())) {
				firetruck.getHealthBar().subtractResourceAmount(PROJECTILE_DAMAGE);
				if (this.score > 10) this.score -= 10;
				projectilesToRemove.add(projectile);
			}
		}
		/* Check if it is in the firestation's radius. Only repair the truck if it needs repairing.
		Allows multiple trucks to be in the radius and be repaired or refilled every second.*/
		this.firestation.checkRepairRefill(this.time);

	}

	/**
	 * Decreases time by 1, called every second by the timer
	 */
	private void decreaseTime() {
		if (this.time > 0) this.time -= 1;
	}

	public void cameraZoom(float zoom) {
		if (this.zoomTarget + zoom < 2f && this.zoomTarget + zoom > 0.8f) {
			this.zoomTarget += zoom;
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
		for (Firetruck firetruck : this.firestation.getParkedFireTrucks()) {
			firetruck.dispose();
		}
		this.firestation.getActiveFireTruck().dispose();
		this.firestation.dispose();
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.dispose();
		}
	}

	private void constructFireTruck(Constants.TruckColours colour, boolean isActive, float[] firetruckProperties) {
		ArrayList<Texture> truckTextures = this.buildFiretuckTextures(colour);
		if (isActive) {
			this.firestation.setActiveFireTruck(new Firetruck(truckTextures, this.waterFrames, firetruckProperties,
					(TiledMapTileLayer) map.getLayers().get("Collision"), (TiledMapTileLayer) map.getLayers().get("Carpark"),
					 this.firestation, colour));
		} else {
			this.firestation.parkFireTruck(new Firetruck(truckTextures, this.waterFrames, firetruckProperties,
					(TiledMapTileLayer) map.getLayers().get("Collision"), (TiledMapTileLayer) map.getLayers().get("Carpark"),
					this.firestation, colour));
		}
	}

	private ArrayList<Texture> buildFiretuckTextures(Constants.TruckColours colour) {
		ArrayList<Texture> truckTextures = new ArrayList<Texture>();
		for (int i = 20; i > 0; i--) {
			if (i == 6) { // Texture 5 contains identical slices except the lights are different
				Texture texture = new Texture("Firetruck" + colour.getColourLower() + "/Firetruck" + colour.getColourUpper() + " (6) A.png");
				truckTextures.add(texture);
				texture = new Texture("Firetruck" + colour.getColourLower() + "/Firetruck" + colour.getColourUpper() + " (6) B.png");
				truckTextures.add(texture);
			} else {
				Texture texture = new Texture("Firetruck" + colour.getColourLower() + "/Firetruck" + colour.getColourUpper() + " (" + i + ").png");
				truckTextures.add(texture);
			}
		}
		return truckTextures;
	}

	Firestation getFirestation() {
		return this.firestation;
	}

}