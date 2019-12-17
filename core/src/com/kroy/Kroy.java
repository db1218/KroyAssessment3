package com.kroy;

// LibGDX imports
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

// Class imports
import com.screens.MainMenuScreen;

/**
 * Entry point to the main game, called by DesktopLauncher.
 * 
 * @author Archie
 * @since 23/11/2019
 */
public class Kroy extends Game {

	// Batches to store drawn elements
  	public SpriteBatch batch;
	public BitmapFont font;

	/**
	 * Display the main menu screen upon game start.
	 */
	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	/**
	 * Render the main game using the Game's render function.
	 */
	public void render() {
		// Calls the parent class Game's render function
		super.render();
	}

	/**
	 * Dispose of all game elements upon game completion.
	 */
	public void dispose() {
		batch.dispose();
		font.dispose();
		screen.dispose();
	}

	/**
	 * Initiate the game based upon parameters, including setting up the viewer.
	 * 
	 * @param camera The viewing system for the game.
	 */
	public void init(OrthographicCamera camera) {
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// Set font scale
		font.getData().setScale(1.5f);
	}

	/**
	 * Write single line text to the screen.
	 * 
	 * @param text The text to be written to the screen.
	 * @param x The x-coorinate for the text.
	 * @param y The y-coorinate for the text.
	 */
	public void drawFont(String text, Double x, Double y) {
		batch.begin();
		font.draw(batch, text, x.floatValue(), y.floatValue());
		batch.end();
	}

	/**
	 * Write multiple lines of text to the screen at once.
	 * 
	 * @param text An array of lines to be written to the screen.
	 * @param x The x-coorinate for where to draw the text.
	 * @param y The y-coorinate for where to draw the text.
	 */
	public void drawFont(String[] text, Double[] x, Double[] y) {
		batch.begin();
		if (text.length == x.length && x.length == y.length) {
			for (int i = 0; i < text.length; i ++) {
				font.draw(batch, text[i], x[i].floatValue(), y[i].floatValue());
			}
		}
		batch.end();
	}
}
