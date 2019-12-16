package com.sprites;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Constants import
import static com.config.Constants.SPRITE_HEIGHT;
import static com.config.Constants.SPRITE_WIDTH;

// Class to simplify the Sprite object provided by LibGDX
public class SimpleSprite extends Sprite {

    // Private values to be used in this class only
    private Batch batch;
    private Texture texture;

    // Constructor for this class, gathers required information so that it can be drawn
    // Params:
    // Batch spriteBatch -  the batch that the sprite should be drawn on
    // Texture spriteTexture - the texture the sprite should use
    // TiledMapTileLayer collisionLayer - which layer of the map the sprite will collide with
    public SimpleSprite(Batch spriteBatch, Texture spriteTexture) {
        batch = spriteBatch;
        texture = spriteTexture;
        setPosition(0, 0);
        setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    // Overload constructor for this class, takes a position to draw the sprite at
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    public SimpleSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        batch = spriteBatch;
        texture = spriteTexture;
        setPosition(xPos, yPos);
        setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    // Draw the sprite at it current position, using current texture
    public void update() {
        draw();
	}
    
    // Draw the sprite at a new position, using current texture
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    public void update(float xPos, float yPos) {
        setPosition(xPos, yPos);
        draw();
	}

    // Draw the sprite at a new position, using a new texture
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    // Texture spriteTexture -  the texture the sprite should use
    public void update(Texture spriteTexture, float xPos, float yPos) {
        texture = spriteTexture;
        setPosition(xPos, yPos);
        draw();
    }

    // Helper function to simplify drawing of sprite
    private void draw() {
        batch.begin();
        batch.draw(texture, getX(), getY(), SPRITE_WIDTH, SPRITE_HEIGHT);
        batch.end();
    }

    // Get the value of the centre x co-ordinate of the sprite
    public float getCentreX() {
        // Add half sprite width to get centre
        return getX() + SPRITE_WIDTH / 2; 
    }

    // Get the value of the centre y co-ordinate of the sprite
    public float getCentreY() {
        //Add half sprite height to get centre
        return getY() + SPRITE_HEIGHT / 2;
    }

    // Dispose of assets used by this class
    public void dispose() {
        texture.dispose();
    }

}