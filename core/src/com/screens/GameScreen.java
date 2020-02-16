package com.screens;
//
// LibGDX imports
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.entities.Firestation;
import com.misc.SFX;
import com.pathFinding.Junction;
import com.pathFinding.MapGraph;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.utils.Timer.Task;

// Tiled map imports from LibGDX
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Java util imports
import java.util.ArrayList;

// Class imports
import com.entities.*;
import com.Kroy;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.sprites.MinigameSprite;

// Constants import
import static com.misc.Constants.*;


/**
 * Display the main game.
 *
 * @author Archie
 * @since 23/11/2019
 */
public class GameScreen implements Screen {

	// A constant variable to store the game
	final Kroy game;

	// Private values for rendering
	private final ShapeRenderer shapeRenderer;
	private final OrthographicCamera camera;
	private final ShaderProgram vignetteSepiaShader;

	// Private values for tiled map
	private final TiledMap map;
	private final OrthogonalTiledMapRenderer renderer;
	private final int[] foregroundLayers;
    private final int[] backgroundLayers;

	// Private values for the game
	private int score;
	private int time;
	private float zoomTarget;

	// Private sprite related objects
	private final ArrayList<ETFortress> ETFortresses;
	private final ArrayList<Projectile> projectiles;
	private final ArrayList<MinigameSprite> minigameSprites;
	private ArrayList<Projectile> projectilesToRemove;
	private final ArrayList<Patrol> ETPatrols;
	private final Firestation firestation;
	private final ArrayList<Texture> waterFrames;
	private final Texture projectileTexture;
	private ArrayList<Texture> patrolTextures;

	// Objects for the patrol graph
	final MapGraph mapGraph;
	final ArrayList<Junction> junctionsInMap;

	// Private stage values
	private final Stage stage;
	private final Label scoreLabel;
	private final Label timeLabel;
	private final Label fpsLabel;

	// objects for the popups and tutorial
	private Queue<String> popupMessages;
	private final TypingLabel tip;
	private boolean isInTutorial;

	// timers to manage timed events
	private final Timer popupTimer;
	private final Timer firestationTimer;
	private final Timer ETPatrolsTimer;

	private final CarparkScreen carparkScreen;
	private final GameInputHandler gameInputHandler;

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
		// Zoom that the user has set with their scroll wheel
		this.zoomTarget = 1.5f;
		this.camera.zoom = 2f;

		// Load the map, set the unit scale
		this.map = new TmxMapLoader().load("MapAssets/York_galletcity.tmx");
		this.renderer = new OrthogonalTiledMapRenderer(map, MAP_SCALE);
		this.shapeRenderer = new ShapeRenderer();

		ShaderProgram.pedantic = false;
		this.vignetteSepiaShader = new ShaderProgram(Gdx.files.internal("shaders/vignetteSepia.vsh"), Gdx.files.internal("shaders/vignetteSepia.fsh"));
		this.renderer.getBatch().setShader(vignetteSepiaShader);

		// Create an array to store all projectiles in motion
		this.projectiles = new ArrayList<>();

		// Decrease time every second, starting at 3 minutes
		this.time = TIME_STATION_VULNERABLE;

		gameInputHandler = new GameInputHandler(this);

		// ---- 2) Initialise and set game properties ----------------------------- //

		// Initialise map renderer as batch to draw textures to
		this.game.setBatch(renderer.getBatch());

		// Set the Batch to render in the coordinate system specified by the camera.
		this.game.batch.setProjectionMatrix(this.camera.combined);

		generateTutorial();

		this.stage = new Stage(new ScreenViewport());
		this.stage.setDebugAll(DEBUG_ENABLED);

		Table table = new Table();
		table.row().colspan(3).expand().pad(40).padBottom(150);
		table.setFillParent(true);

		scoreLabel = new Label("", game.getFont10());
		table.add(scoreLabel).top();

		Stack tipStack = new Stack();
		tip = new TypingLabel("", game.getFont10());
		tip.setAlignment(Align.center);
		tip.setWrap(true);

		tipStack.add(tip);
		table.add(tipStack).bottom().fillX();

		VerticalGroup vg = new VerticalGroup();

		timeLabel = new Label("", game.getFont10());
		vg.addActor(timeLabel);

		fpsLabel = new Label("", game.getFont10());
		vg.addActor(fpsLabel);

		table.add(vg).top();

		stage.addActor(table);

		SFX.sfx_soundtrack_2.setLooping(true);

		// ---- 3) Construct all textures to be used in the game here, ONCE ------ //

		// Select background and foreground map layers, order matters
        MapLayers mapLayers = map.getLayers();
        this.foregroundLayers = new int[] {
			mapLayers.getIndex("Buildings"),
			mapLayers.getIndex("Carpark")
        };
        this.backgroundLayers = new int[] {
			mapLayers.getIndex("River"),
			mapLayers.getIndex("Road"),
			mapLayers.getIndex("Trees")
        };

        // creates mini game sprites around the map
		minigameSprites = new ArrayList<>();
		minigameSprites.add(new MinigameSprite(87, 68));
		minigameSprites.add(new MinigameSprite(30.5f, 55));
		minigameSprites.add(new MinigameSprite(10, 92));
		minigameSprites.add(new MinigameSprite(93, 106));

		// Initialise textures to use for sprites
		Texture firestationTexture = new Texture("MapAssets/UniqueBuildings/firestation.png");
		Texture firestationDestroyedTexture = new Texture("MapAssets/UniqueBuildings/firestation_destroyed.png");
		Texture cliffordsTowerTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower.png");
		Texture cliffordsTowerWetTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower_wet.png");
		Texture railstationTexture = new Texture("MapAssets/UniqueBuildings/railstation.png");
		Texture railstationWetTexture = new Texture("MapAssets/UniqueBuildings/railstation_wet.png");
		Texture yorkMinsterTexture = new Texture("MapAssets/UniqueBuildings/Yorkminster.png");
		Texture yorkMinsterWetTexture = new Texture("MapAssets/UniqueBuildings/Yorkminster_wet.png");
		Texture castle1Texture = new Texture("MapAssets/UniqueBuildings/fortress_1.png");
		Texture castle1WetTexture = new Texture("MapAssets/UniqueBuildings/fortress_1_wet.png");
		Texture castle2Texture = new Texture("MapAssets/UniqueBuildings/fortress_2.png");
		Texture castle2WetTexture = new Texture("MapAssets/UniqueBuildings/fortress_2_wet.png");
		Texture mossyTexture = new Texture("MapAssets/UniqueBuildings/mossy.png");
		Texture mossyWetTexture = new Texture("MapAssets/UniqueBuildings/mossy_wet.png");

		this.projectileTexture = new Texture("alienProjectile.png");

		// Create arrays of textures for animations
		waterFrames = new ArrayList<Texture>();

		// Create patrol texture
		buildPatrolTextures();

		for (int i = 1; i <= 3; i++) {
			Texture texture = new Texture("waterSplash" + i + ".png");
			waterFrames.add(texture);
		}

		// ---- 4) Create entities that will be around for entire game duration - //

		// Create a new firestation
		this.firestation = new Firestation(firestationTexture, firestationDestroyedTexture, 77.5f * TILE_DIMS, 35.5f * TILE_DIMS, this);
		this.carparkScreen = new CarparkScreen(this.game, this, this.firestation);

		// need to make it take away from  the number of points

		// Initialise firetrucks array and add firetrucks to it
		constructFireTruck(true, TruckType.RED);
		constructFireTruck(false, TruckType.BLUE);
		constructFireTruck(false, TruckType.YELLOW);
		constructFireTruck(false, TruckType.GREEN);

		// Initialise ETFortresses array and add ETFortresses to it
		this.ETFortresses = new ArrayList<ETFortress>();
		this.ETFortresses.add(new ETFortress(cliffordsTowerTexture, cliffordsTowerWetTexture, 1, 1, 69 * TILE_DIMS, 51 * TILE_DIMS, FortressType.CLIFFORD, this));
		this.ETFortresses.add(new ETFortress(yorkMinsterTexture, yorkMinsterWetTexture, 2, 3.25f, 68.25f * TILE_DIMS, 82.25f * TILE_DIMS, FortressType.MINSTER, this));
		this.ETFortresses.add(new ETFortress(railstationTexture, railstationWetTexture, 2, 2.5f, TILE_DIMS, 72.75f * TILE_DIMS, FortressType.RAIL, this));
		this.ETFortresses.add(new ETFortress(castle2Texture, castle2WetTexture, 2, 2, 10 * TILE_DIMS, TILE_DIMS, FortressType.CASTLE2, this));
		this.ETFortresses.add(new ETFortress(castle1Texture, castle1WetTexture, 2, 2, 98 * TILE_DIMS, TILE_DIMS, FortressType.CASTLE1, this));
		this.ETFortresses.add(new ETFortress(mossyTexture, mossyWetTexture, 1.5f, 1.5f, 106 * TILE_DIMS, 101 * TILE_DIMS, FortressType.MOSSY, this));

		// Create array to collect entities that are no longer used
		this.projectilesToRemove = new ArrayList<Projectile>();

		this.junctionsInMap = new ArrayList<>();
		mapGraph = new MapGraph();
		populateMap();

		firestationTimer = new Timer();
		firestationTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				decreaseTime();
			}
		}, 1, 1);
		firestationTimer.stop();

		popupTimer = new Timer();
		popupTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				nextPopup();
			}
		}, 3f, 10f);
		popupTimer.stop();

		ETPatrols = new ArrayList<>();
		ETPatrolsTimer = new Timer();
		ETPatrolsTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				createPatrol();
			}
		}, 7,10);

		isInTutorial = true;

	}

	/**
	 * Actions to perform on first render cycle of the game
	 */
	@Override
	public void show() {
		this.resume();
		this.camera.setToOrtho(false);
		this.camera.position.set(this.firestation.getActiveFireTruck().getCentreX(), this.firestation.getActiveFireTruck().getCentreY(), 0);
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

		vignetteSepiaShader.begin();
		if (isInTutorial) {
			vignetteSepiaShader.setUniformf("u_intensity", 0.8f);
			vignetteSepiaShader.setUniformf("u_outerRadius", 0.6f);
			vignetteSepiaShader.setUniformf("u_sepia", 0.2f);
		} else {
			vignetteSepiaShader.setUniformf("u_intensity", camera.zoom-0.4f);
			vignetteSepiaShader.setUniformf("u_outerRadius", calculateValueForProgress(0.5f, 0.8f));
			vignetteSepiaShader.setUniformf("u_sepia", calculateValueForProgress(0.65f, 0.3f));
		}
		vignetteSepiaShader.end();

		// ---- 1) Update camera and map properties each iteration -------- //

		// Set the TiledMapRenderer view based on what the camera sees
		renderer.setView(this.camera);

		// Align the debug view with the camera
		shapeRenderer.setProjectionMatrix(this.camera.combined);

		// Get the firetruck thats being driven so that the camera can follow it
		Firetruck focusedTruck = this.firestation.getActiveFireTruck();

		// ==============================================================
		//					Modified for assessment 3
		// ==============================================================
		// Tell the camera to update to the sprites position with a delay based on lerp and game time
		Vector3 cameraPosition = this.camera.position;
		float xDifference = focusedTruck.getCentreX() - cameraPosition.x;
		float yDifference = focusedTruck.getCentreY() - cameraPosition.y;
		cameraPosition.x += xDifference * LERP * delta;
		cameraPosition.y += yDifference * LERP * delta;

		if (this.camera.zoom - ZOOM_SPEED > zoomTarget) {
			this.camera.zoom -= ZOOM_SPEED;
		} else if (this.camera.zoom + ZOOM_SPEED < zoomTarget) {
			this.camera.zoom += ZOOM_SPEED;
		}

		this.camera.update();

		// ---- 3) Draw background, firetruck then foreground layers ----- //

		// Render background map layers
		renderer.render(backgroundLayers);

		// Render map foreground layers
		renderer.render(foregroundLayers);

		// Render the arrow
		firestation.updateActiveArrow(shapeRenderer, ETFortresses);

		// Render the remaining sprites, font last to be on top of all
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		this.game.batch.begin();

		// Render sprites
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.update(this.game.batch);
			if (DEBUG_ENABLED) ETFortress.drawDebug(shapeRenderer);
		}
		for (Projectile projectile : this.projectiles) {
			projectile.update(this.game.batch);
			if (DEBUG_ENABLED) projectile.drawDebug(shapeRenderer);
			if (projectile.isOutOfMap()) this.projectilesToRemove.add(projectile);
		}

		// Call the update function of the sprites to draw and update them
		firestation.updateFiretruck(this.game.batch, this.shapeRenderer, this.camera);

		// Updates and render patrols
		for (Patrol patrol : this.ETPatrols) {
			patrol.update(this.game.batch);
			if (DEBUG_ENABLED) patrol.drawDebug(shapeRenderer);
		}

		// Render mini game sprites
		for (MinigameSprite minigameSprite : minigameSprites) {
			minigameSprite.update(this.game.batch);
		}

		this.firestation.update(this.game.batch);

		if (DEBUG_ENABLED) firestation.drawDebug(shapeRenderer);

		// Finish rendering
		this.game.batch.end();
		shapeRenderer.end();

		// Draw the score, time and FPS to the screen at given co-ordinates
		this.scoreLabel.setText("Score: " + this.score);
		this.timeLabel.setText("Time: " + this.getFireStationTime());
		if (DEBUG_ENABLED) {
			this.fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		} else {
			this.fpsLabel.clear();
		}

		this.stage.act(delta);
		this.stage.draw();

		// ---- 4) Perform any calulcation needed after sprites are drawn - //

		// Check for any collisions
		if (!isInTutorial) checkForCollisions();

		// Remove projectiles that are off the screen and firetrucks that are dead
		this.projectiles.removeAll(this.projectilesToRemove);

		// Check if the game should end
		checkIfGameOver();

		checkIfCarpark();
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
		vignetteSepiaShader.begin();
		vignetteSepiaShader.setUniformf("u_resolution", width, height);
		vignetteSepiaShader.end();
	}

	/**
	 * Actions to perform when the main game is hidden.
	 */
	@Override
	public void hide() {
	}

	/** ==================================================
	 * 			 New function for assessment 3
	 * ===================================================
	 *
	 * Pauses the game and changes the screen to PauseScreen
	 *
	 * Actions to perform when the main game is paused.
	 */
	@Override
	public void pause() {
		if (!isInTutorial) firestationTimer.stop();
		popupTimer.stop();
		ETPatrolsTimer.stop();
		game.setScreen(new PauseScreen(game, this));
	}

	/**
	 * 	==============================================================
	 * 						Overridden for assessment 3
	 *  ==============================================================
	 * Actions to perform when the main game is resumed.
	 */
	@Override
	public void resume() {
		if (!isInTutorial) firestationTimer.start();
		popupTimer.start();
		ETPatrolsTimer.start();
		this.camera.position.set(this.firestation.getActiveFireTruck().getCentre(), 0);
	}

	/**
	 * Dispose of main game assets upon completion.
	 */
	@Override
	public void dispose() {
		projectileTexture.dispose();
		for (Firetruck firetruck : firestation.getParkedFireTrucks()) {
			firetruck.dispose();
		}
		firestation.getActiveFireTruck().dispose();
		firestation.dispose();
		for (ETFortress ETFortress : ETFortresses) {
			ETFortress.dispose();
		}
		renderer.dispose();
		map.dispose();
		vignetteSepiaShader.dispose();
		stage.dispose();
		carparkScreen.dispose();
		shapeRenderer.dispose();
	}

	/**
	 * Spawns a patrol, up to a certain number
	 * */
	public void createPatrol() {
		if (this.ETPatrols.size() < PATROL_MAX) {
			spawnPatrol();
		}
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Updates only the movement of the patrol when the
	 * player is in the car park screen
	 */
	public void updatePatrolMovements() {
		for (Patrol patrol : this.ETPatrols) {
			patrol.updateMovement();
		}
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Checks to see if the fire truck is to be opened, if so change screen
	 */
	public void checkIfCarpark() {
		if (this.firestation.isMenuOpen()) {
			popupTimer.stop();
			game.setScreen(this.carparkScreen);
		}
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Checks to see if the player has won or lost the game. Navigates back to the main menu
	 * if they won or lost.
	 */
	public void checkIfGameOver() {
		boolean gameWon = true, gameLost = true;
		// Check if any firetrucks are still alive
		if (this.firestation.hasParkedFiretrucks() || this.firestation.getActiveFireTruck().isAlive()) gameLost = false;

		// Check if any fortresses are still alive
		for (ETFortress ETFortress : this.ETFortresses) {
			if (ETFortress.getHealthBar().getCurrentAmount() > 0) gameWon = false;
		}
		if (gameWon) this.game.setScreen(new GameOverScreen(this.game, Outcome.WON, this.score));
		else if (gameLost) this.game.setScreen(new GameOverScreen(this.game, Outcome.LOST, this.score));
	}

	/*
	 *  =======================================================================
	 *                          Modified for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Checks to see if any collisions have occurred
	 */
	public void checkForCollisions() {
		// Check each firetruck to see if it has collided with anything
		Firetruck firetruck = this.firestation.getActiveFireTruck();
		// Check if it overlaps with an ETFortress
		for (ETFortress ETFortress : this.ETFortresses) {
			if (ETFortress.getHealthBar().getCurrentAmount() > 0 && firetruck.isInHoseRange(ETFortress.getDamageHitBox())) {
				ETFortress.getHealthBar().subtractResourceAmount((int) firetruck.getDamage());
				this.score += 10;
			}
			if (ETFortress.isInRadius(firetruck.getCentre()) && ETFortress.canShootProjectile()) {
				Projectile projectile = new Projectile(this.projectileTexture, ETFortress.getCentreX(), ETFortress.getCentreY(), ETFortress.getType().getDamage());
				projectile.calculateTrajectory(firetruck);
				SFX.sfx_projectile.play();
				this.projectiles.add(projectile);
			}
		}

		// ==============================================================
		//					Added for assessment 3
		// ==============================================================
		// Checks to see if a patrol is dead and removes it if it has died
		for (int i=0; i<this.ETPatrols.size(); i++) {
			Patrol patrol = this.ETPatrols.get(i);
			if (patrol.isDead()) {
				patrol.removeDead(mapGraph);
				this.ETPatrols.remove(patrol);
			}
		}

		// ==============================================================
		//					Added for assessment 3
		// ==============================================================
		// Checks if a patrol has attacked a fire truck and vice versa, also if patrol can attack fire station
		for (Patrol patrol : this.ETPatrols) {
			if (patrol.getHealthBar().getCurrentAmount() > 0 && firetruck.isInHoseRange(patrol.getDamageHitBox())) {
				patrol.getHealthBar().subtractResourceAmount((int) firetruck.getDamage());
				this.score += 10;
			}
			if (patrol.isInRadius(firetruck.getCentre()) && patrol.canShootProjectile()) {
				Projectile projectile = new Projectile(this.projectileTexture, patrol.getCentreX(), patrol.getCentreY(), 5);
				projectile.calculateTrajectory(firetruck);
				SFX.sfx_projectile.play();
				this.projectiles.add(projectile);
			} else if (!firestation.isDestroyed() && firestation.isVulnerable() && patrol.isInRadius(firestation.getCentre()) && patrol.canShootProjectile()) {
				Projectile projectile = new Projectile(this.projectileTexture, patrol.getCentreX(), patrol.getCentreY(), 5);
				projectile.calculateTrajectory(firestation);
				SFX.sfx_projectile.play();
				this.projectiles.add(projectile);
			}
		}
		// ==============================================================
		//					Added for assessment 3
		// ==============================================================
		// Checks if truck has driven over a minigame sprite
		for (int i=0; i<this.minigameSprites.size(); i++) {
			MinigameSprite minigameSprite = this.minigameSprites.get(i);
			if (Intersector.overlapConvexPolygons(firetruck.getMovementHitBox(), minigameSprite.getHitBox())) {
				if (!isInTutorial) firestationTimer.stop();
				popupTimer.stop();
				ETPatrolsTimer.stop();
				this.minigameSprites.remove(minigameSprite);
				this.firestation.getActiveFireTruck().setSpeed(new Vector2(0, 0));
				this.firestation.getActiveFireTruck().setHose(false);
				this.game.setScreen(new MinigameScreen(this.game, this));
			}
		}

		// Check if firetruck is hit with a projectile
		for (int i=0; i<this.projectiles.size(); i++) {
			Projectile projectile = this.projectiles.get(i);
			if (Intersector.overlapConvexPolygons(firetruck.getDamageHitBox(), projectile.getDamageHitBox())) {
				SFX.sfx_truck_damage.play();
				firetruck.getHealthBar().subtractResourceAmount(projectile.getDamage());
				if (this.score >= 10) this.score -= 10;
				this.projectiles.remove(projectile);
			} else if (!firestation.isDestroyed() && firestation.isVulnerable() && Intersector.overlapConvexPolygons(firestation.getDamageHitBox(), projectile.getDamageHitBox())) {
				firestation.getHealthBar().subtractResourceAmount(projectile.getDamage());
				this.projectiles.remove(projectile);
			}
		}
		/* Check if it is in the firestation's radius. Only repair the truck if it needs repairing.
		Allows multiple trucks to be in the radius and be repaired or refilled every second.*/
		this.firestation.checkRepairRefill(this.time, false);

	}

	/**
	 * Decreases time by 1, called every second by the timer
	 */
	private void decreaseTime() {
		this.time -= 1;
	}

	/*
	 *  =======================================================================
	 *                          Modified for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Sets the zoomTarget that the user sets with the scroll wheel
	 *
	 * @param zoom	positive for zoom out, negative for zoom in
	 */
	public void cameraZoom(float zoom) {
		if (this.zoomTarget + zoom < MAX_ZOOM && this.zoomTarget + zoom > MIN_ZOOM) {
			this.zoomTarget += zoom;
		}
	}

	/**
	 * Creates a fire truck
	 *
	 * @param isActive	whether this truck is the active, starting truck
	 * @param type		the type of truck to spawn
	 */
	public void constructFireTruck(boolean isActive, TruckType type) {
		// there is a bug where if you buy another truck then you die then
		ArrayList<Texture> truckTextures = this.buildFiretuckTextures(type.getColourString());
		Firetruck firetruck = new Firetruck(truckTextures, this.waterFrames, type,
				(TiledMapTileLayer) map.getLayers().get("Collision"), (TiledMapTileLayer) map.getLayers().get("Carpark"),
				this.firestation, isActive);
		if (isActive) {
			if (this.firestation.getActiveFireTruck() == null) {
				this.firestation.setActiveFireTruck(firetruck);
			} else {
				throw new GdxRuntimeException("There should only be at most 1 active fire truck at any one time");
			}
		} else {
			this.firestation.parkFireTruck(firetruck);
		}
	}

	/**
	 * Builds an array of textures that is used to render the firetruck
	 *
	 * @param colourString	specifies the colour of fire truck to build
	 * @return				array of textures
	 */
	private ArrayList<Texture> buildFiretuckTextures(String colourString) {
		ArrayList<Texture> truckTextures = new ArrayList<Texture>();
		for (int i = 20; i > 0; i--) {
			if (i == 6) { // Texture 6 contains identical slices except the lights are different
				Texture texture = new Texture("FireTrucks/" + colourString + "/Firetruck(6) A.png");
				truckTextures.add(texture);
				texture = new Texture("FireTrucks/" + colourString + "/Firetruck(6) B.png");
				truckTextures.add(texture);
			} else {
				Texture texture = new Texture("FireTrucks/" + colourString + "/Firetruck(" + i + ").png");
				truckTextures.add(texture);
			}
		}
		return truckTextures;
	}

	/** ===============================================
	 * 			New function for assessment 3
	 * ================================================
	 * Creates Patrol and adds it to the list of patrols
	 */
	private void spawnPatrol() {
		this.ETPatrols.add(new Patrol(this.patrolTextures, mapGraph));
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Builds an array of textures that is used to render patrols
	 */
	private void buildPatrolTextures() {
		ArrayList<Texture> patrolTextures = new ArrayList<Texture>();
		for (int i = 99; i >= 0; i--) {
			String numberFormat = String.format("%03d", i);
			Texture texture = new Texture("AlienSlices/tile" + numberFormat + ".png");
			patrolTextures.add(texture);
		}
		this.patrolTextures = patrolTextures;
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Adds junctions to the mapGraph. A junction is a place in the map where the
	 * patrol has to make a decision about where to move to next.
	 */
	private void populateMap(){
		Junction one = new Junction(4987, 572, "bottom right corner");
		Junction two = new Junction(3743, 572, "Bottom 4 junction R.H.S");
		Junction three = new Junction(2728, 572, " Bottom turn left to dead end");
		Junction four = new Junction(2538, 572, "Bottom turn up to four junction");
		Junction five = new Junction(1069, 572, "bottom 5 left 4 junction");
		Junction six = new Junction(3745, 1199, "bottom left of fire station");
		Junction seven = new Junction(4123, 1199, "bottom right of fire station");
		Junction eight = new Junction(4128, 1910, "Top right of fire station");
		Junction nine = new Junction(3738, 1918, "Top left of fire station");
		Junction ten = new Junction(3412, 1920, "Across bridge turn up to tower");
		Junction eleven = new Junction(3406, 2204, "First corner to fortress coming up from bridge");
		Junction twelve = new Junction(3223, 2223, "second corner to attack fortress after coming up from bridge");
		Junction thirteen = new Junction(3256, 2787, "Bottom left of island");
		Junction fourteen = new Junction (3885, 2784, "Bottom right of island");
		Junction fifteen = new Junction (3889, 3018, "Top right of island");
		Junction sixteen = new Junction (3274, 3021, "Top left of island");
		Junction seventeen = new Junction (4129, 2100, "T junction top right of fire station");
		Junction eighteen = new Junction (4554, 2106, "First bend after right from t-junction top right of station");
		Junction nineteen = new Junction (4558, 2333, "Second bend after right from t-juncion top right of fire station");
		Junction twenty = new Junction (4991, 2345, "Mid 4 junction on right hand side");
		Junction twentyOne = new Junction (3887, 2111, "left of fortress");
		Junction twentyTwo = new Junction(2546, 1920, "4 junction mid left hand side");
		Junction twentyThree = new Junction(2546, 3016, "Up from 4 junction mid left hand side");
		Junction twentyFour = new Junction(2785, 3016, "up and right from 4 junction mid left hand side");
		Junction twentyFive = new Junction (2789, 2794, "Turn left from bottom left of island");
		Junction twentySix = new Junction(2162, 1964, "Turn left from 4 junction mid left hand side");
		Junction twentySeven = new Junction(2162, 2205, "Bottom right of block on left hand side");
		Junction twentyEight = new Junction (2162, 3165, "Top right of block on left hand side");
		Junction twentyNine = new Junction (1149, 3168, "right of fork on left hand side");
		Junction thirty = new Junction (958, 3168, "Middle of fork on left hand side");
		Junction thirtyOne = new Junction (718, 3168, "Right of fork on left hand side");
		Junction thirtyTwo = new Junction (718, 4203, "Top left hand of map");
		Junction thirtyThree = new Junction (966, 2210, "Bottom left of block on left hand side");
		Junction thirtyFour = new Junction (1052, 2207, "Up from bottom right 4 junction");
		Junction thirtyFive = new Junction (1157, 4261, "Left from top right 4 junction");
		Junction thirtySix = new Junction (1921, 4270, "Top right 4 junction");
		Junction thirtySeven = new Junction (1921, 3592, "Bottom from top 4 junction");
		Junction thirtyEight = new Junction (2161, 3592, "Middle junction between 37 and 39, top left");
		Junction thirtyNine = new Junction (3037, 3590, "2nd bottom from bottom left of fortress");
		Junction forty = new Junction (3026, 3739, "Bottom left of fortress");
		Junction fortyOne = new Junction (3077, 3025, "2nd left from top left of island");
		Junction fortyTwo = new Junction (4220, 3736, "Bottom right of fortress left");
		Junction fortyThree = new Junction (4228, 4502, "Right of fortress");
		Junction fortyFour = new Junction (4228, 4930, "Top right of fortress");
		Junction fortyFive = new Junction (3030, 4947, "Top 4 junction");
		Junction fortySix = new Junction (2439, 4948, "Left of mid top 4 junction");
		Junction fortySeven = new Junction (2450, 4278, "Right of the top left mid 4 junction");
		Junction fortyEight = new Junction (4991, 4506, "Top right 4 junction");

		mapGraph.addJunction(one);
		mapGraph.addJunction(two);
		mapGraph.addJunction(three);
		mapGraph.addJunction(four);
		mapGraph.addJunction(five);
		mapGraph.addJunction(six);
		mapGraph.addJunction(seven);
		mapGraph.addJunction(eight);
		mapGraph.addJunction(nine);
		mapGraph.addJunction(ten);
		mapGraph.addJunction(eleven);
		mapGraph.addJunction(twelve);
		mapGraph.addJunction(thirteen);
		mapGraph.addJunction(fourteen);
		mapGraph.addJunction(fifteen);
		mapGraph.addJunction(sixteen);
		mapGraph.addJunction(seventeen);
		mapGraph.addJunction(eighteen);
		mapGraph.addJunction(nineteen);
		mapGraph.addJunction(twenty);
		mapGraph.addJunction(twentyOne);
		mapGraph.addJunction(twentyTwo);
		mapGraph.addJunction(twentyThree);
		mapGraph.addJunction(twentyFour);
		mapGraph.addJunction(twentyFive);
		mapGraph.addJunction(twentySix);
		mapGraph.addJunction(twentySeven);
		mapGraph.addJunction(twentyEight);
		mapGraph.addJunction(twentyNine);
		mapGraph.addJunction(thirty);
		mapGraph.addJunction(thirtyOne);
		mapGraph.addJunction(thirtyTwo);
		mapGraph.addJunction(thirtyThree);
		mapGraph.addJunction(thirtyFour);
		mapGraph.addJunction(thirtyFive);
		mapGraph.addJunction(thirtySix);
		mapGraph.addJunction(thirtySeven);
		mapGraph.addJunction(thirtyEight);
		mapGraph.addJunction(thirtyNine);
		mapGraph.addJunction(forty);
		mapGraph.addJunction(fortyOne);
		mapGraph.addJunction(fortyTwo);
		mapGraph.addJunction(fortyThree);
		mapGraph.addJunction(fortyFour);
		mapGraph.addJunction(fortyFive);
		mapGraph.addJunction(fortySix);
		mapGraph.addJunction(fortySeven);
		mapGraph.addJunction(fortyEight);

		mapGraph.connectJunctions(one, two);
		mapGraph.connectJunctions(one, twenty);

	 	mapGraph.connectJunctions(two, one);
		mapGraph.connectJunctions(two, six);
		mapGraph.connectJunctions(two, three);

		mapGraph.connectJunctions(three, two);
		mapGraph.connectJunctions(three, four);

		mapGraph.connectJunctions(four, three);
		mapGraph.connectJunctions(four, five);
		mapGraph.connectJunctions(four, twentyTwo);

		mapGraph.connectJunctions(five, four);
		mapGraph.connectJunctions(five, thirtyFour);

		mapGraph.connectJunctions(six, two);
		mapGraph.connectJunctions(six, seven);
		mapGraph.connectJunctions(six, nine);

		mapGraph.connectJunctions(seven, six);
		mapGraph.connectJunctions(seven, eight);

		mapGraph.connectJunctions(eight, seven);
		mapGraph.connectJunctions(eight, nine);
		mapGraph.connectJunctions(eight, seventeen);

		mapGraph.connectJunctions(nine, six);
		mapGraph.connectJunctions(nine, eight);
		mapGraph.connectJunctions(nine, ten);

		mapGraph.connectJunctions(ten, nine);
		mapGraph.connectJunctions(ten, eleven);
		mapGraph.connectJunctions(ten, twentyTwo);

		mapGraph.connectJunctions(eleven, ten);
		mapGraph.connectJunctions(eleven, twelve);

		mapGraph.connectJunctions(twelve, eleven);
		mapGraph.connectJunctions(twelve, thirteen);

		mapGraph.connectJunctions(thirteen, fourteen);
		mapGraph.connectJunctions(thirteen, sixteen);
		mapGraph.connectJunctions(thirteen, twelve);
		mapGraph.connectJunctions(thirteen, twentyFive);

		mapGraph.connectJunctions(fourteen, thirteen);
		mapGraph.connectJunctions(fourteen, fifteen);
		mapGraph.connectJunctions(fourteen, twentyOne);

		mapGraph.connectJunctions(fifteen, fourteen);
		mapGraph.connectJunctions(fifteen, sixteen);

		mapGraph.connectJunctions(sixteen, thirteen);
		mapGraph.connectJunctions(sixteen, fifteen);
		mapGraph.connectJunctions(sixteen, fortyOne);

		mapGraph.connectJunctions(seventeen, eight);
		mapGraph.connectJunctions(seventeen, eighteen);
		mapGraph.connectJunctions(seventeen, twentyOne);

		mapGraph.connectJunctions(eighteen, seventeen);
		mapGraph.connectJunctions(eighteen, nineteen);

		mapGraph.connectJunctions(nineteen, eighteen);
		mapGraph.connectJunctions(nineteen, twenty);

		mapGraph.connectJunctions(twenty, one);
		mapGraph.connectJunctions(twenty, nineteen);
		mapGraph.connectJunctions(twenty, fortyEight);

		mapGraph.connectJunctions(twentyOne, fourteen);
		mapGraph.connectJunctions(twentyOne, seventeen);

		mapGraph.connectJunctions(twentyTwo, four);
		mapGraph.connectJunctions(twentyTwo, ten);
		mapGraph.connectJunctions(twentyTwo, twentyThree);
		mapGraph.connectJunctions(twentyTwo, twentySix);

		mapGraph.connectJunctions(twentyThree, twentyTwo);
		mapGraph.connectJunctions(twentyThree, twentyFour);

		mapGraph.connectJunctions(twentyFour, twentyThree);
		mapGraph.connectJunctions(twentyFour, twentyFive);

		mapGraph.connectJunctions(twentyFive, twentyFour);
		mapGraph.connectJunctions(twentyFive, thirteen);

		mapGraph.connectJunctions(twentySix, twentyTwo);
		mapGraph.connectJunctions(twentySix, twentySeven);

		mapGraph.connectJunctions(twentySeven, twentySix);
		mapGraph.connectJunctions(twentySeven, twentyEight);
		mapGraph.connectJunctions(twentySeven, thirtyFour);

		mapGraph.connectJunctions(twentyEight, twentySeven);
		mapGraph.connectJunctions(twentyEight, twentyNine);
		mapGraph.connectJunctions(twentyEight, thirtyEight);

		mapGraph.connectJunctions(twentyNine, twentyEight);
		mapGraph.connectJunctions(twentyNine, thirty);

		mapGraph.connectJunctions(thirty, twentyNine);
		mapGraph.connectJunctions(thirty, thirtyThree);
		mapGraph.connectJunctions(thirty, thirtyOne);

		mapGraph.connectJunctions(thirtyOne, thirty);
		mapGraph.connectJunctions(thirtyOne, thirtyTwo);

		mapGraph.connectJunctions(thirtyTwo, thirtyOne);

		mapGraph.connectJunctions(thirtyThree, thirty);
		mapGraph.connectJunctions(thirtyThree, thirtyFour);

		mapGraph.connectJunctions(thirtyFour, thirtyThree);
		mapGraph.connectJunctions(thirtyFour, twentySeven);
		mapGraph.connectJunctions(thirtyFour, five);

		mapGraph.connectJunctions(thirtyFive, twentyNine);
		mapGraph.connectJunctions(thirtyFive, thirtySix);

		mapGraph.connectJunctions(thirtySix, thirtyFive);
		mapGraph.connectJunctions(thirtySix, thirtySeven);
		mapGraph.connectJunctions(thirtySix, fortySeven);

		mapGraph.connectJunctions(thirtySeven, thirtySix);
		mapGraph.connectJunctions(thirtySeven, thirtyEight);

		mapGraph.connectJunctions(thirtyEight, thirtySeven);
		mapGraph.connectJunctions(thirtyEight, twentyEight);
		mapGraph.connectJunctions(thirtyEight, thirtyNine);

		mapGraph.connectJunctions(thirtyNine, forty);
		mapGraph.connectJunctions(thirtyNine, thirtyEight);
		mapGraph.connectJunctions(thirtyNine, fortyOne);

		mapGraph.connectJunctions(forty, thirtyNine );
		mapGraph.connectJunctions(forty, fortyTwo);
		mapGraph.connectJunctions(forty, fortyFive);

		mapGraph.connectJunctions(fortyOne, sixteen);
		mapGraph.connectJunctions(fortyOne, thirtyNine);

		mapGraph.connectJunctions(fortyTwo, forty);
		mapGraph.connectJunctions(fortyTwo, fortyThree);

		mapGraph.connectJunctions(fortyThree, fortyTwo);
		mapGraph.connectJunctions(fortyThree, fortyFour);
		mapGraph.connectJunctions(fortyThree, fortyEight);

		mapGraph.connectJunctions(fortyFour, fortyThree);
		mapGraph.connectJunctions(fortyFour, fortyFive);

		mapGraph.connectJunctions(fortyFive, forty);
		mapGraph.connectJunctions(fortyFive, fortyFour);
		mapGraph.connectJunctions(fortyFive, fortySix);

		mapGraph.connectJunctions(fortySix, fortyFive);
		mapGraph.connectJunctions(fortySix, fortySeven);

		mapGraph.connectJunctions(fortySeven, fortySix);
		mapGraph.connectJunctions(fortySeven, thirtySix);

		mapGraph.connectJunctions(fortyEight, twenty);
		mapGraph.connectJunctions(fortyEight, fortyThree);
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Initially populates the popup messages queue with
	 * the tutorial to teach the player how to play the game
	 */
	private void generateTutorial() {
		popupMessages = new Queue<>();
		popupMessages.addLast("{SLOW}{COLOR=#FFFFFFC0}Veteran fire fighter? Press ENTER to skip tutorial\n" +
				"Otherwise, hold tight, we will begin in a moment...");
		popupMessages.addLast("{SLOW}{COLOR=#FFFFFFC0}You are currently in a safe haven, nothing can harm you... so relax...");
		popupMessages.addLast("{SLOW}{COLOR=#FFFFFFC0}Feel free to roam around and explore the city, " +
				"get accustomed to your new environment...");
		popupMessages.addLast("{FADE=0;0.75;1}Basic Controls\n{ENDFADE}" +
				"{SLOW}{COLOR=#FFFFFFC0}WSAD to drive the truck \n" +
				"MOUSE operates the water cannon \n" +
				"SCROLL controls camera zoom");
		popupMessages.addLast("{FADE=0;0.75;1}Fire Station{ENDFADE} \n" +
				"{SLOW}{COLOR=#FFFFFFC0}You spawned right outside here. This is where you can repair and refill Fire Trucks...");
		popupMessages.addLast("{SLOW}{COLOR=#FFFFFFC0}Top right, once that timer reaches zero, the Fire Station is vulnerable and can be destroyed, " +
				"then you can no longer repair or refill...");
		popupMessages.addLast("{FADE=0;0.75;1}Score{ENDFADE} \n" +
				"{SLOW}{COLOR=#FFFFFFC0}Top left, achieved by attacking Patrols and Fortresses, and can be spent to unlock new trucks " +
				"at the Fire Station...");
		popupMessages.addLast("{FADE=0;0.75;1}Minigame{ENDFADE} \n" +
				"{SLOW}{COLOR=#FFFFFFC0}Gain extra score in a minigame, accessed through controller icons dotted around the map");
		popupMessages.addLast("{FADE=0;0.75;1}The Mission{ENDFADE} \n" +
				"{SLOW}{COLOR=#FFFFFFC0}Your aim is to eliminate all ET Fortresses that have inhabited York.\n" +
				"Use SPACE to locate the nearest enemy Fortress...");
		popupMessages.addLast("{SLOW}{COLOR=#FFFFFFC0}Be wary though, if all your Fire Trucks get destroyed," +
				"you lose, and York will fall to the ETs...");
		popupMessages.addLast("{SLOW}{COLOR=#FFFFFFC0}You're all set, the mission will start in 10 seconds...");

	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Runs every 5 seconds to generate the next tip
	 * if there is one, and the first time the tips list
	 * is empty, the tutorial has finished
	 */
	private void nextPopup() {
		tip.setText("");
		if (popupMessages.notEmpty()) {
			tip.setText(popupMessages.removeFirst());
		} else {
			finishTutorial();
		}
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Queues a popup message and resets the popup timer
	 *
	 * @param text		text to display
	 * @param repeat	how many times the message should appear
	 * @param interval	how long the message should stay up for
	 */
	public void showPopupText(String text, int repeat, int interval) {
		if (!isInTutorial) {
			popupTimer.clear();
			for (int i=0; i<repeat; i++) {
				popupMessages.addLast("{FADE=0;0.75;1}" + text);
			}
			popupTimer.scheduleTask(new Task() {
				@Override
				public void run() {
					nextPopup();
				}
			}, 0, interval);
		}
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Returns a tuple containing the number of fortresses
	 * destroyed and total number of fortresses
	 *
	 * @return	tuple of ints
	 */
	public int[] getETFortressesDestroyed() {
		int fortressesDestroyed = 0;
		for (ETFortress fortress : this.ETFortresses)
			if (fortress.isFlooded())
				fortressesDestroyed++;
		return new int[]{fortressesDestroyed, this.ETFortresses.size()};
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Multiple statements to reset the game after the
	 * tutorial has terminated:
	 * - reset truck water + position
	 * - show good luck test
	 * - start fire station timer
	 * - reset zoom
	 * - reset patrols
	 *
	 * The shader will also change, and collisions will now
	 * begin to occur, starting the game
	 */
	public void finishTutorial() {
		if (isInTutorial) {
			isInTutorial = false;
			popupMessages.clear();
			showPopupText("Good luck!", 1, 5);
			firestationTimer.start();
			firestation.getActiveFireTruck().getWaterBar().resetResourceAmount();
			firestation.getActiveFireTruck().setRespawnLocation(0);
			firestation.getActiveFireTruck().respawn();
			firestation.getActiveFireTruck().setHose(false);
			this.ETPatrols.clear();
			this.camera.zoom = 1.3f;
			this.zoomTarget = 1.2f;
			popupMessages.addLast("{FADE=0;0.75;1}Pro Tip: Killing your enemies makes them less likely to kill you.");
			popupMessages.addLast("{FADE=0;0.75;1}Pro Tip: Drive straight into a wall to perform a sick 180 flip.");
			SFX.playGameMusic();
		}
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Calculates the sepia and vignette values which
	 * change as the user destroyed
	 *
	 * @param start	starting value at start of game
	 * @param end	final value at end of game
	 * @return		intensity of sepia or vignette
	 * 				radius depending on progress
	 */
	private float calculateValueForProgress(float start, float end) {
		float progress = (float) getETFortressesDestroyed()[0] / (float) getETFortressesDestroyed()[1];
		return start - (progress*(start-end));
	}

	/**
	 * Returns the time for the fire station
	 * @return	<code>if time greater than 0</code> time
	 * 			<code>if time less than 0</code> 0
	 */
	public int getFireStationTime() { return Math.max(this.time, 0); }

	public int getTime() { return this.time; }

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Firestation getFirestation() {
		return this.firestation;
	}

	public Firetruck getActiveTruck() {return this.firestation.getActiveFireTruck();}

}