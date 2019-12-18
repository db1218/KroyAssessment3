package com.classes;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.Input.Keys;

// Custom class import
import com.sprites.MovementSprite;

// Constants import
import static com.config.Constants.Direction;
import static com.config.Constants.FIRETRUCK_HEALTH;

/**
 * The Firetruck implementation.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class Firetruck extends MovementSprite {

    // Private values to be used in this class only
    private Boolean isFocused;
    private int focusID;

    /**
     * Constructor for the firetruck, gathering required information for it to be drawn.
     * 
     * @param spriteBatch The batch the firetruck should be drawn on.
     * @param spriteTexture The texture the firetruck should use.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID The ID of the truck (for object focus).
     */
    public Firetruck(Batch spriteBatch, Texture spriteTexture, TiledMapTileLayer collisionLayer, int ID) {
        super(spriteBatch, spriteTexture, collisionLayer);
        this.focusID = ID;
        this.healthBar.setMaxResource(FIRETRUCK_HEALTH);
    }

    /**
     * Constructor for the firetruck, gathering required information for it to be
     * drawn, given x and y coordinates.
     * 
     * @param spriteBatch    The batch the firetruck should be drawn on.
     * @param spriteTexture  The texture the firetruck should use.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     * @param xPos           The x-coordinate for the firetruck.
     * @param yPos           The y-coordinate for the firetruck.
     */
    public Firetruck(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos, TiledMapTileLayer collisionLayer, int ID) {
        super(spriteBatch, spriteTexture, xPos, yPos, collisionLayer);
        this.focusID = ID;
        this.healthBar.setMaxResource(FIRETRUCK_HEALTH);
    }

    /**
     * Update the position and direction of the firetruck every frame.
     */
    public void update() {
        super.update();
        if (isFocused) {
            // Look for key press input, then accelerate the firetruck in that direction
            if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
                super.accelerate(Direction.LEFT);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
                super.accelerate(Direction.RIGHT);
            }          
            if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
                super.accelerate(Direction.DOWN);
            } 
            if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
                super.accelerate(Direction.UP);
            }
        }
    }

    /**
     * Gets whether the firetruck is in focus.
     * 
     * @return Whether the firetruck is in focus (true) or not (false).
     */
    public boolean getFocus() {
        return this.isFocused;
    }

    /**
     * Sets the firetruck in focus if its ID matches the one to focus.
     * @param focus The ID of the firetruck to focus on.
     */
    public void setFocus(int focus) {
        if (focus == focusID) {
            this.isFocused = true;
        } else {
            this.isFocused = false;
        }
    }
}