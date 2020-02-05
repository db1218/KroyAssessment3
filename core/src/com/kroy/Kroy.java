package com.kroy;

// LibGDX imports
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;

// Class imports
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.screens.GameScreen;

/**
 * Entry point to the main game, called by DesktopLauncher.
 * 
 * @author Archie
 * @since 23/11/2019
 */
public class Kroy extends Game {

	// Batches to store drawn elements
  	public Batch batch;
  	public SpriteBatch spriteBatch;
	public BitmapFont font;
	public Label.LabelStyle font15;
	public Label.LabelStyle font10;

	/**
	 * Display the main menu screen upon game start.
	 */
	public void create() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		// Use LibGDX's default Arial font.
		this.setTTF();
		this.spriteBatch = new SpriteBatch();

		// Instantly transition to the main menu screen when game starts
		this.setScreen(new GameScreen(this));
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
		this.batch.dispose();
		this.font.dispose();
		this.screen.dispose();
	}

	/**
	 * Set the batch that should be used to render all the textures.
	 * @param batch The batch to be used for the game.
	 */
	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	/**
	 * Get the font used in the game.
	 * @return The font system for the game.
	 */
	public BitmapFont getFont() {
		return this.font;
	}

	/**
	 * Write single line text to the screen.
	 * 
	 * @param text The text to be written to the screen.
	 * @param x The x-coorinate for the text.
	 * @param y The y-coorinate for the text.
	 */
	public void drawFont(String text, float x, float y) {
		this.font.draw(this.batch, text, x, y);
	}

	/**
	 * Write multiple lines of text to the screen at once.
	 * 
	 * @param text An array of lines to be written to the screen.
	 * @param x The x-coorinate for where to draw the text.
	 * @param y The y-coorinate for where to draw the text.
	 */
	public void drawFont(String[] text, float[] x, float[] y) {
		if (text.length == x.length && x.length == y.length) {
			for (int i = 0; i < text.length; i ++) {
				this.font.draw(this.batch, text[i], x[i], y[i]);
			}
		}
	}

	private void setTTF() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Xolonium-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.borderWidth = 1;
		parameter.color = Color.WHITE;
		parameter.shadowOffsetX = 3;
		parameter.shadowOffsetY = 3;
		parameter.shadowColor = Color.DARK_GRAY;

		parameter.size = 15;
		font = generator.generateFont(parameter); // font size 24 pixels
		parameter.size = 20;
		BitmapFont font2 = generator.generateFont(parameter);

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		Label.LabelStyle labelStyle2 = new Label.LabelStyle();

		labelStyle.font = font2;
		this.font10 = labelStyle;

		labelStyle2.font = font;
		this.font15 = labelStyle2;

		generator.dispose();
	}

	public Label.LabelStyle getFont15() {
		return this.font15;
	}

	public Label.LabelStyle getFont10() {
		return this.font10;
	}
}
