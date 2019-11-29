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

    public SimpleSprite(Batch spriteBatch, Texture spriteTexture) {
        batch = spriteBatch;
        texture = spriteTexture;
        sprite = new Sprite(texture);
        setPosition(0, 0);
        setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    public SimpleSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        batch = spriteBatch;
        texture = spriteTexture;
        sprite = new Sprite(texture);
        setPosition(xPos, yPos);
        setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    public void drawSprite() {
        draw();
	}
    
    public void drawSprite(float xPos, float yPos) {
        setPosition(xPos, yPos);
        draw();
	}

    public void drawSprite(Texture spriteTexture, float xPos, float yPos) {
		sprite = new Sprite(spriteTexture);
        setPosition(xPos, yPos);
        draw();
    }

    private void draw() {
        batch.begin();
        batch.draw(texture, getX(), getY(), SPRITE_WIDTH, SPRITE_HEIGHT);
        batch.end();
    }

    public float getCentreX() {
        return getX() + SPRITE_WIDTH / 2; //Add half sprite width to get centre
    }

    public float getCentreY() {
        return getY() + SPRITE_HEIGHT / 2; //Add half sprite height to get centre
    }

    public void dispose() {
        texture.dispose();
    }

}