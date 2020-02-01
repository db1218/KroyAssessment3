package com.screens;

// LibGDX imports
import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.PathFinding.Road;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.ai.pfa.GraphPath;

// Java util imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// Class imports
import com.classes.*;
import com.config.Constants;
import com.kroy.Kroy;

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
	private int score;
	private int time;
	private Texture projectileTexture;

	private float zoomTarget;

	private Timer collisionTask;

	// Private arrays to group sprites
	private ArrayList<Firetruck> firetrucksToRemove;
	private ArrayList<ETFortress> ETFortresses;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> projectilesToRemove;
	private ArrayList<Patrols> ETPatrols;
	private Firestation firestation;


	MapGraph mapGraph;
	GraphPath<Junction> cityPath;
	ArrayList<Junction> junctionsInMap;

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
		}, 1, 1);

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

		// Initialise textures to use for sprites
		Texture firestationTexture = new Texture("MapAssets/UniqueBuildings/firestation.png");
		Texture firestationDestroyedTexture = new Texture("MapAssets/UniqueBuildings/firestation_destroyed.png");
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
		this.firestation = new Firestation(firestationTexture, firestationDestroyedTexture, 77.5f * TILE_DIMS, 35.5f * TILE_DIMS, game, this);

		// Initialise firetrucks array and add firetrucks to it
		constructFireTruck(Constants.TruckColours.RED, true, FiretruckOneProperties);
		constructFireTruck(Constants.TruckColours.BLUE, false, FiretruckTwoProperties);
		constructFireTruck(TruckColours.YELLOW, false, FiretruckOneProperties);


		// Initialise ETFortresses array and add ETFortresses to it
		this.ETFortresses = new ArrayList<ETFortress>();
		this.ETFortresses.add(new ETFortress(cliffordsTowerTexture, cliffordsTowerWetTexture, 1, 1, 69 * TILE_DIMS, 51 * TILE_DIMS));
		this.ETFortresses.add(new ETFortress(yorkMinisterTexture, yorkMinisterWetTexture, 2, 3.25f, 68.25f * TILE_DIMS, 82.25f * TILE_DIMS));
		this.ETFortresses.add(new ETFortress(railstationTexture, railstationWetTexture, 2, 2.5f, 1 * TILE_DIMS, 72.75f * TILE_DIMS));

		this.junctionsInMap = new ArrayList<>();
		mapGraph = new MapGraph();
		populateMap();

		ETPatrols = new ArrayList<>();

		spawnPatrol();
		spawnPatrol();
		spawnPatrol();

		collisionTask = new Timer();
		collisionTask.scheduleTask(new Task()
		{
			@Override
			public void run() {
				checkForCollisions();
			}
		}, .5f, .5f);

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

		// Render map foreground layers
		renderer.render(foregroundLayers);

		// Render the arrow
		firestation.updateActiveArrow(shapeRenderer, ETFortresses);

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

		// Call the update function of the sprites to draw and update them
		firestation.updateFiretruck(this.batch, this.shapeRenderer, this.camera);

		for (Patrols patrol : this.ETPatrols) {
			patrol.update(this.batch);
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
		if (DEBUG_ENABLED) game.drawFont("FPS: " + Gdx.graphics.getFramesPerSecond(),
				cameraPosition.x + this.camera.viewportWidth * TIME_X * camera.zoom,
				cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom - 30
		);

		// Finish rendering
		batch.end();


		if (DEBUG_ENABLED) shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);

		if(DEBUG_ENABLED) { // Draws all the nodes and the paths between them
			shapeRenderer.setColor(Color.RED);
			for (Road road : mapGraph.getRoads()) {
			//	shapeRenderer.rectLine(road.getFromNode().getVector(), road.getToNode().getVector(), 3);
			//	shapeRenderer.line(road.getFromNode().getVector(), road.getToNode().getVector());
			}
			for (Junction junction : mapGraph.getJunctions()) {
			//	shapeRenderer.circle(junction.getX(), junction.getY(), 30);
			}
		}
		shapeRenderer.setColor(Color.WHITE);

		shapeRenderer.end();

		// ---- 4) Perform any calulcation needed after sprites are drawn - //

		// Remove projectiles that are off the screen and firetrucks that are dead
		this.projectiles.removeAll(this.projectilesToRemove);

		// Check for any collisions
		checkForCollisions();

		// Check if the game should end
		checkIfGameOver();

		checkIfCarpark();
	}

	public void checkIfCarpark() {
		if (this.firestation.isMenuOpen()) {
			game.setScreen(this.firestation.getCarparkScreen());
		}
	}

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
		if (gameWon || gameLost) {
			dispose();
			this.game.setScreen(new MainMenuScreen(this.game));
		}
	}

	/**
     * Checks to see if any collisions have occurred
     */
	public void checkForCollisions() {
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


		// Checks to see if a patrol is dead and removes it if it has died
		for (Iterator<Patrols> it = this.ETPatrols.iterator(); it.hasNext();) {
			if (it.next().isDead()) {
				it.remove();
			}
		}

		// Checks if a patrol has attacked a fire truck and vice versa
		for (Patrols patrol : this.ETPatrols) {
			if (patrol.getHealthBar().getCurrentAmount() > 0 && firetruck.isInHoseRange(patrol.getHitBox())) {
				patrol.getHealthBar().subtractResourceAmount(FIRETRUCK_DAMAGE);
				this.score += 10;
			}
			if (patrol.isInRadius(firetruck.getHitBox()) && patrol.canShootProjectile()) {
				Projectile projectile = new Projectile(this.projectileTexture, patrol.getCentreX(), patrol.getCentreY());
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
		this.firestation.checkRepairRefill(this.time, false);

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

	public void constructFireTruck(Constants.TruckColours colour, boolean isActive, float[] firetruckProperties) {
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

	private void spawnPatrol(){
		ArrayList<Texture> patrolTexture = buildFiretuckTextures(TruckColours.BLUE);
		Patrols patrol = new Patrols(patrolTexture, mapGraph.getJunctions().random(), mapGraph);
		this.ETPatrols.add(patrol);
	}

	Firestation getFirestation() {
		return this.firestation;
	}

	Firetruck getTruck() {
		return this.firestation.getActiveFireTruck();
	}

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

	public int getTime() {
		return this.time;
	}

}