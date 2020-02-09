package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
// Class imports
import com.kroy.Kroy;

import static com.config.Constants.DEBUG_ENABLED;

// Constants import


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
	private final OrthographicCamera camera;

	private final Stage stage;
	private final Skin skin;
	private final Viewport viewport;

	/**
	 * The constructor for the main menu screen. All game logic for the main
	 * menu screen is contained.
	 *
	 * @param game The game object.
	 */
	public MainMenuScreen(final Kroy game) {
		this.game = game;

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
		
		// Create new sprite batch

		// Create an orthographic camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		/* tell the SpriteBatch to render in the
		   coordinate system specified by the camera. */
		game.spriteBatch.setProjectionMatrix(camera.combined);

		// Create a viewport
		viewport = new ScreenViewport(camera);
		viewport.apply(true);

		// Set camera to centre of viewport
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		// Create a stage for buttons
		stage = new Stage(viewport, game.spriteBatch);
		stage.setDebugAll(DEBUG_ENABLED);
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
		Label heading = new Label("Kroy", new Label.LabelStyle(game.coolFont, Color.WHITE));
		heading.setFontScale(2);
		Label subHeading = new Label("Destroy the Fortresses and Save the City", new Label.LabelStyle(game.coolFont, Color.WHITE));
		TextButton playButton = new TextButton("Play", skin);
		TextButton controlsButton = new TextButton("Controls", skin);
		TextButton quitButton = new TextButton("Quit", skin);

		// Add buttons to table and style them
		buttonTable.add(heading).padBottom(10);
		buttonTable.row();
		buttonTable.add(subHeading).padBottom(20);
		buttonTable.row();
		buttonTable.add(playButton).padBottom(20).width(200).height(40);
		buttonTable.row();
		buttonTable.add(controlsButton).padBottom(20).width(200).height(40);
		buttonTable.row();
		buttonTable.add(quitButton).width(200).height(40);

		// Add listeners
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new StoryScreen(game));
				dispose();
			}
		});

		controlsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				game.setScreen(new ControlsScreen(game));
//				dispose();
			}
		});

		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
				System.exit(1);
			}
		});

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
		stage.dispose();
	}
}