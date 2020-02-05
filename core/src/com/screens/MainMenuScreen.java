package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
// Class imports
import com.kroy.Kroy;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;

/**
 * Displays the main menu screen with selection buttons.
 * 
 * @author Archie
 * @author Josh
 * @since 23/11/2019
 */
public class MainMenuScreen implements Screen {
	
	// A constant variable to store the game
	final Kroy game;
	
	// Private camera to see the screen
	private OrthographicCamera camera;

	protected Stage stage;
	protected Texture texture;
	protected Skin skin;
	protected TextureAtlas atlas;
	private SpriteBatch batch;
	private Viewport viewport;

	/**
	 * The constructor for the main menu screen. All game logic for the main
	 * menu screen is contained.
	 *
	 * @param game The game object.
	 */
	public MainMenuScreen(final Kroy game) {
		this.game = game;

		atlas = new TextureAtlas("skin/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
		//skin.add("default", new Texture("button.png"));
		
		// Create new sprite batch
		batch = new SpriteBatch();

		// Create an orthographic camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// Set font scale
		this.game.getFont().getData().setScale(1.5f);

		// Create a viewport
		viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
		viewport.apply();

		// Set camera to centre of viewport
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		// Create a stage for buttons
		stage = new Stage(viewport, batch);
	}

	/**
	 * Render function to display all elements in the main menu.
	 * 
	 * @param delta The delta time of the game, updated every second rather than frame.
	 */
	@Override
	public void render(float delta) {
		// MUST BE FIRST: Clear the screen each frame to stop textures blurring
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw the button stage
		stage.draw();
	}

	// Below are all required methods of the screen class
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
        camera.update();
	}

	/**
	 * Create the button stage.
	 */
	@Override
	public void show() {
		// Allow stage to control screen inputs.
		Gdx.input.setInputProcessor(stage);

		// Create table to arrange buttons.
		Table buttonTable = new Table();
		buttonTable.setFillParent(true);
		buttonTable.center();

		// Create buttons
		TextButton playButton = new TextButton("Play", skin);
		TextButton leaderboardButton = new TextButton("Leaderboard", skin);
		TextButton quitButton = new TextButton("Quit", skin);

		// Increase size of text
		playButton.setTransform(true);
		playButton.scaleBy(0.25f);
		leaderboardButton.setTransform(true);
		leaderboardButton.scaleBy(0.25f);
		quitButton.setTransform(true);
		quitButton.scaleBy(0.25f);

		// Add listeners
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Transition to main game screen
				game.setScreen(new GameScreen(game));
				dispose();
			}
		});

		leaderboardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Transition to leaderboard screen
				//
				// TO IMPLEMENT
				//
				// Currently main game screen
				game.setScreen(new GameScreen(game));
				dispose();
			}
		});

		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
				System.exit(1);
			}
		});
		
		// Add buttons to table and style them
		buttonTable.add(playButton).padBottom(40).padRight(40).width(150).height(40);
		buttonTable.row();
		buttonTable.add(leaderboardButton).padBottom(40).padRight(40).width(150).height(40);
		buttonTable.row();
		buttonTable.add(quitButton).width(150).padRight(40).height(40);

		// Add table to stage
		stage.addActor(buttonTable);

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
		skin.dispose();
	}
}