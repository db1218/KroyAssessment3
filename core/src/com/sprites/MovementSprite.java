package com.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Constants import
import static com.config.Constants.MAP_HEIGHT;
import static com.config.Constants.MAP_WIDTH;
import static com.config.Constants.SPRITE_HEIGHT;
import static com.config.Constants.SPRITE_WIDTH;

public class MovementSprite extends SimpleSprite {

    // For animations later
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    Direction direction;

    public MovementSprite(Batch spriteBatch, Texture spriteTexture) {
        super(spriteBatch, spriteTexture);
    }

    public MovementSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        super(spriteBatch, spriteTexture, xPos, yPos);
    }

    // Update the sprites position and direction
    public void update() {
        // look for key press input
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            this.x -= 200 * Gdx.graphics.getDeltaTime();
            direction = Direction.LEFT;
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
            this.x += 200 * Gdx.graphics.getDeltaTime();
            direction = Direction.RIGHT;
            
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            this.y -= 200 * Gdx.graphics.getDeltaTime();
            direction = Direction.DOWN;
		if (Gdx.input.isKeyPressed(Keys.UP))
            this.y += 200 * Gdx.graphics.getDeltaTime();
            direction = Direction.UP;
        
        // Check boundaries then draw
        checkBoundaries();
        this.drawSprite();
    }
    
    // Make sure the sprite stays within the screen bounds
    private void checkBoundaries() {
        if (this.y < 0)
            this.y = 0;
        if (this.y > MAP_HEIGHT - SPRITE_HEIGHT)
            this.y = MAP_HEIGHT - SPRITE_HEIGHT;
        if (this.x < 0)
            this.x = 0;
        if (this.x > MAP_WIDTH - SPRITE_WIDTH)
            this.x = MAP_WIDTH - SPRITE_WIDTH;
    }
}