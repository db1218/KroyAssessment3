package com.kroy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

// Class imports
import com.screens.MainMenuScreen;

public class Kroy extends Game {

  	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public void init(OrthographicCamera camera) {
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
	}

	public void drawFontSingle(String text, Double x, Double y) {
		batch.begin();
		font.draw(batch, text, x.floatValue(), y.floatValue());
		batch.end();
	}

	public void drawFontMultiple(String[] text, Double[] x, Double[] y) {
		batch.begin();
		if (text.length == x.length && x.length == y.length) {
			for (int i = 0; i < text.length; i ++) {
				font.draw(batch, text[i], x[i].floatValue(), y[i].floatValue());
			}
		}
		batch.end();
	}
}
