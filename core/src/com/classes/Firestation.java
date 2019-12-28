package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.FIRESTATION_HEALTH;
import static com.config.Constants.FIRESTATION_HEIGHT;
import static com.config.Constants.FIRESTATION_WIDTH;

/**
 * The Firestation implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class Firestation extends SimpleSprite {

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture  The texture used to draw the Firestation with.
     * @param xPos     The x-coordinate for the Firestation.
     * @param yPos     The y-coordinate for the Firestation.
     */
    public Firestation(Texture texture, float xPos, float yPos) {
        super(texture);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Simplfied constructor for the Firestation, that doesn't require a position.
     * Drawn with the given texture at (0,0).
     * 
     * @param texture  The texture used to draw the Firestation with.
     */
    public Firestation(Texture texture) {
        super(texture);
        this.create();
    }

    /**
     * Sets the health of the Firestation and its size provided in CONSTANTS.
     */
    private void create() {
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     */
    public void update(Batch batch) {
        super.update(batch);
    }
}