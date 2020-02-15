package com;

// LibGDX imports
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;

// Class imports
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.screens.MainMenuScreen;
import com.screens.MinigameScreen;

/**
 * Entry point to the main game, called by DesktopLauncher.
 * 
 * @author Archie
 * @since 23/11/2019
 */
public class Kroy extends Game {

	// shared objects to use throughout the screens as quite heavy
  	public Batch batch;
  	public SpriteBatch spriteBatch;
	public BitmapFont coolFont;
	private Label.LabelStyle font10;
	private Skin skin;

	/**
	 * Display the main menu screen upon game start.
	 */
	public void create() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());


		this.setFonts();
		this.spriteBatch = new SpriteBatch();
		this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
		// Instantly transition to the main menu screen when game starts
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
		this.batch.dispose();
		this.spriteBatch.dispose();
		this.screen.dispose();
		this.coolFont.dispose();
		this.skin.dispose();
	}

	/**
	 * Set the batch that should be used to render all the textures.
	 * @param batch The batch to be used for the game.
	 */
	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	/**
	 * Helper method to create the shared fonts and styles
	 * used throughout the game by various screens.
	 */
	private void setFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Xolonium-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.borderWidth = 1;
		parameter.color = Color.WHITE;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		parameter.shadowColor = Color.DARK_GRAY;

		parameter.size = 20;
		BitmapFont font2 = generator.generateFont(parameter);

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font2;

		this.font10 = labelStyle;
		this.coolFont = generator.generateFont(parameter);

		generator.dispose();
	}

	public Label.LabelStyle getFont10() {
		return this.font10;
	}

	/*
	 *  =======================================================================
	 *                          Added for Assessment 3
	 *  =======================================================================
	 */
	public Skin getSkin() {
		return this.skin;
	}
}
