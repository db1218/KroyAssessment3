package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

// Class imports
import com.kroy.Kroy;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.SCREEN_CENTRE_X;
import static com.config.Constants.SCREEN_CENTRE_Y;

/**
 * Displays the main menu screen.
 */
public class MainMenuScreen implements Screen {
	
	// A constant variable to store the game
	final Kroy game;
	
	// Private camera to see the screen
	private OrthographicCamera camera;

	/**
	 * The constructor for the main menu screen. All game logic for the main
	 * menu screen is contained.
	 *
	 * @param gam The game object.
	 */
	public MainMenuScreen(final Kroy gam) {
		game = gam;

		// Create an orthographic camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		game.init(camera);
	}

	/**
	 * Render function to display all elements in the main menu.
	 * 
	 * @param delta The delta time of the game, updated every second rather than frame.
	 */
	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1] of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update the camera every game second
        camera.update();
		
		//Draw multiple lines of text to the screen
        game.drawFont(
			new String[] {"Welcome to Kroy!", "Tap anywhere to begin!"}, 
			new Double[] {SCREEN_CENTRE_X - 90.0, SCREEN_CENTRE_X - 120.0}, 
			new Double[] {SCREEN_CENTRE_Y, SCREEN_CENTRE_Y - 50.0}
		);

		// Listen for any input, then transition to the game screen
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			// Dispose of any assets this screen uses
			dispose();
		}
	}

	// Below are all required methods of the screen class
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
	}
}