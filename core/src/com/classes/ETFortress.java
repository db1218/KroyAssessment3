package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.ETFORTRESS_HEALTH;
import static com.config.Constants.ETFORTRESS_HEIGHT;
import static com.config.Constants.ETFORTRESS_WIDTH;

/**
 * The ET Fortress implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class ETFortress extends SimpleSprite {

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture  The texture used to draw the ETFortress with.
     * @param xPos     The x-coordinate for the ETFortress.
     * @param yPos     The y-coordinate for the ETFortress.
     */
    public ETFortress(Texture texture, float xPos, float yPos) {
        super(texture);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Simplfied constructor for the ETFortress, that doesn't require a position.
     * Drawn with the given texture at (0,0).
     * 
     * @param texture  The texture used to draw the ETFortress with.
     */
    public ETFortress(Texture texture) {
        super(texture);
        this.create();
    }

    /**
     * Sets the health of the ETFortress and its size provided in CONSTANTS.
     */
    private void create() {
        this.getHealthBar().setMaxResource(ETFORTRESS_HEALTH);
        this.setSize(ETFORTRESS_WIDTH, ETFORTRESS_HEIGHT);
    }

    /**
     * Update the fortress so that it is drawn every frame.
     */
    public void update(Batch batch) {
        super.update(batch);
    }
}