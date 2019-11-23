package com.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class SimpleSprite extends Sprite {

    Sprite sprite;
    SpriteBatch batch;
    Texture texture;
    float x,y;

    public SimpleSprite(SpriteBatch spriteBatch, Texture spriteTexture) {
        batch = spriteBatch;
        texture = spriteTexture;
        x = 0;
        y = 0;
    }

    public SimpleSprite(SpriteBatch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        batch = spriteBatch;
        texture = spriteTexture;
        x = xPos;
        y = yPos;
    }

    public void drawSprite() {
		Sprite sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        batch.begin();
        sprite.draw(batch);
        batch.end();
	}

    public void drawSprite(Texture spriteTexture) {
		Sprite sprite = new Sprite(spriteTexture);
        sprite.setPosition(x, y);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
    
    public void drawSprite(float xPos, float yPos) {
		Sprite sprite = new Sprite(texture);
        sprite.setPosition(xPos, yPos);
        batch.begin();
        sprite.draw(batch);
        batch.end();
	}

    public void drawSprite(Texture spriteTexture, float xPos, float yPos) {
		Sprite sprite = new Sprite(spriteTexture);
        sprite.setPosition(xPos, yPos);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void dispose() {
        texture.dispose();
    }

}