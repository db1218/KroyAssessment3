package com.sprites;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Class import
import com.sprites.ResourceBar;

// Constants import
import static com.config.Constants.SPRITE_HEIGHT;
import static com.config.Constants.SPRITE_WIDTH;

/**
 * Simplify the sprite object provided by libGDX.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class SimpleSprite extends Sprite {

    // Private values to be used in this class only
    private Batch batch;
    private Texture texture;

    // Allows the health bar to be changed by subclasses
    public ResourceBar healthBar;

    /**
     * Constructor for this class. Gathers the required information so the
     * sprite can be drawn.
     * 
     * @param spriteBatch   The batch that the sprite should be drawn on.
     * @param spriteTexture The texture the sprite should use.
     */
    public SimpleSprite(Batch spriteBatch, Texture spriteTexture) {
        batch = spriteBatch;
        texture = spriteTexture;
        healthBar = new ResourceBar(batch, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.setPosition(0, 0);
        this.setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    /**
     * Overload constructor for this class, taking a position to draw the sprite at.
     * 
     * @param spriteBatch    The batch that the sprite should be drawn on.
     * @param spriteTexture  The texture the sprite should use.
     * @param xPos           The x-coordinate for the sprite to be drawn.
     * @param yPos           The y-coordinate for the sprite to be drawn.
     */
    public SimpleSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        batch = spriteBatch;
        texture = spriteTexture;
        healthBar = new ResourceBar(batch, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.setPosition(xPos, yPos);
        this.setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    /**
     * Update the sprite position and health bar.
     */
    public void update() {
        healthBar.setPosition(this.getX(), this.getY());
        healthBar.update();
        draw();
	}
    
    /**
     * Draw the sprite at a new position, using current texture.
     * 
     * @param xPos The x-coordinate for the sprite.
     * @param yPos The y-coordinate for the sprite.
     */
    public void update(float xPos, float yPos) {
        setPosition(xPos, yPos);
        this.healthBar.setPosition(xPos, yPos);
        this.healthBar.update();
        draw();
	}

    // Draw the sprite at a new position, using a new texture
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    // Texture spriteTexture -  the texture the sprite should use
    /**
     * Draw the sprite at a new position, using a new texture.
     * 
     * @param spriteTexture The new texture for the sprite to use.
     * @param xPos          The x-coorinate for the sprite.
     * @param yPos          The y-coorinate for the sprite.
     */
    public void update(Texture spriteTexture, float xPos, float yPos) {
        this.texture = spriteTexture;
        this.setPosition(xPos, yPos);
        draw();
    }

    /**
     * Helper function to simplify the drawing of a sprite.
     */
    private void draw() {
        batch.begin();
        batch.draw(texture, getX(), getY(), SPRITE_WIDTH, SPRITE_HEIGHT);
        batch.end();
    }

    /**
     * Get the value of the centre x-coordinate of the sprite.
     * @return The cente X-coordinate of the sprite.
     */
    public float getCentreX() {
        // Add half sprite width to get centre
        return getX() + SPRITE_WIDTH / 2; 
    }

    /**
     * Get the value of the centre y-coordinate of the sprite.
     * @return The centre y-coordinate of the sprite.
     */
    public float getCentreY() {
        //Add half sprite height to get centre
        return getY() + SPRITE_HEIGHT / 2;
    }

    /**
     * Dispose of assets used by the class.
     */
    public void dispose() {
        texture.dispose();
    }

}