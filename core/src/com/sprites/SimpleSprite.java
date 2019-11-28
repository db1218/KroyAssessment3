package com.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Constants import
import static com.config.Constants.SPRITE_HEIGHT;
import static com.config.Constants.SPRITE_WIDTH;

public class SimpleSprite extends Sprite {

    Sprite sprite;
    Batch batch;
    Texture texture;
    float x,y;

    public SimpleSprite(Batch spriteBatch, Texture spriteTexture) {
        batch = spriteBatch;
        texture = spriteTexture;
        sprite = new Sprite(texture);
        x = 0;
        y = 0;
    }

    public SimpleSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        batch = spriteBatch;
        texture = spriteTexture;
        sprite = new Sprite(texture);
        x = xPos;
        y = yPos;
    }

    public void drawSprite() {
        draw();
	}
    
    public void drawSprite(float xPos, float yPos) {
        x = xPos;
        y = yPos;
        draw();
	}

    public void drawSprite(Texture spriteTexture, float xPos, float yPos) {
		sprite = new Sprite(spriteTexture);
        x = xPos;
        y = yPos;
        draw();
    }

    private void draw() {
        batch.begin();
        batch.draw(texture, x, y);
        batch.end();
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public float getCentreX() {
        return x + SPRITE_WIDTH / 2; //Add half sprite width to get centre
    }

    public float getCentreY() {
        return y + SPRITE_HEIGHT / 2; //Add half sprite height to get centre
    }

    public void dispose() {
        texture.dispose();
    }

}