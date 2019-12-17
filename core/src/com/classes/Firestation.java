package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.FIRESTATION_HEALTH;

/**
 * The Firestation implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class Firestation extends SimpleSprite {

    /**
     * The constructor for the class, gathering required information for it to
     * be drawn.
     * 
     * @param spriteBatch The batch the sprite should be drawn on.
     * @param spriteTexture The texture the sprite should use.
     */
    public Firestation(Batch spriteBatch, Texture spriteTexture) {
        super(spriteBatch, spriteTexture);
        this.healthBar.setMaxResource(FIRESTATION_HEALTH);
    }

    // Overload constructor for this class, takes a position to draw the sprite at
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    /**
     * The constructor for the class, gathering required information for it to be
     * drawn, also giving information about a required position.
     * 
     * @param spriteBatch   The batch the sprite should be drawn on.
     * @param spriteTexture The texture the fortress should use.
     * @param xPos          The x-coordinate for the fortress.
     * @param yPos          The y-coordinate for the fortress.
     */
    public Firestation(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        super(spriteBatch, spriteTexture, xPos, yPos);
        this.healthBar.setMaxResource(FIRESTATION_HEALTH);
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     */
    public void update() {
        super.update();
    }
}