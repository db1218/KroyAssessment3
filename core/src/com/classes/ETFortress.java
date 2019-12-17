package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.ETFORTRESS_HEALTH;

// Class to add movement code to a sprite
public class ETFortress extends SimpleSprite {

    // Constructor for this class, gathers required information so that it can be drawn
    // Params:
    // Batch spriteBatch -  the batch that the sprite should be drawn on
    // Texture spriteTexture - the texture the sprite should use
    public ETFortress(Batch spriteBatch, Texture spriteTexture) {
        super(spriteBatch, spriteTexture);
        this.healthBar.setMaxResource(ETFORTRESS_HEALTH);
    }

    // Overload constructor for this class, takes a position to draw the sprite at
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    public ETFortress(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        super(spriteBatch, spriteTexture, xPos, yPos);
        this.healthBar.setMaxResource(ETFORTRESS_HEALTH);
    }

    // Update the sprites position and direction. Called every game frame
    public void update() {
        super.update();
    }
}