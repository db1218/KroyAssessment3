package com.kroy;

// LibGDX imports
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

// Class imports
import com.screens.MainMenuScreen;

// Entry point to the Game, called from DesktopLauncher
public class Kroy extends Game {

	// Batches to store drawn elements
  	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	// Render the game elements
	public void render() {
		// Calls the parent class Game's render function
		super.render();
	}

	// Dispose of all assets when game finishes
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	// 
	public void init(OrthographicCamera camera) {
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// Set font scale
		font.getData().setScale(1.5f);
	}

	// A simplified draw function to reduce code
	// Params: 
	// String text - the text to be written to the screen
	// Double x,y - the coordinates of where to draw the text
	public void drawFont(String text, Double x, Double y) {
		batch.begin();
		font.draw(batch, text, x.floatValue(), y.floatValue());
		batch.end();
	}

	// An overloaded version of drawFont that allows multiple lines of text to be drawn at once
	// Params: 
	// Array of String text - the text to be written to the screen
	// Array of Double x,y - the coordinates of where to draw the text
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
