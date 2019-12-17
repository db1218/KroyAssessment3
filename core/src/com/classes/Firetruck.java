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

// Class to add movement code to a sprite
public class Firetruck extends MovementSprite {

    // Private values to be used in this class only
    private Boolean isFocused;
    private int focusID;

    // Constructor for this class, gathers required information so that it can be drawn
    // Params:
    // Batch spriteBatch -  the batch that the sprite should be drawn on
    // Texture spriteTexture - the texture the sprite should use
    // TiledMapTileLayer collisionLayer - which layer of the map the sprite will collide with
    // ID - the ID of the truck to know when to focus it
    public Firetruck(Batch spriteBatch, Texture spriteTexture, TiledMapTileLayer collisionLayer, int ID) {
        super(spriteBatch, spriteTexture, collisionLayer);
        this.focusID = ID;
        this.healthBar.setMaxResource(FIRETRUCK_HEALTH);
    }

    // Overload constructor for this class, takes a position to draw the sprite at
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    public Firetruck(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos, TiledMapTileLayer collisionLayer, int ID) {
        super(spriteBatch, spriteTexture, xPos, yPos, collisionLayer);
        this.focusID = ID;
        this.healthBar.setMaxResource(FIRETRUCK_HEALTH);
    }

    // Update the sprites position and direction. Called every game frame
    public void update() {
        super.update();
        if (isFocused) {
            // Look for key press input, then accelerate the firetruck in that direction
            if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
                super.setDirection(Direction.LEFT);
                super.accelerate();
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
                super.setDirection(Direction.RIGHT);
                super.accelerate();
            }          
            if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
                super.setDirection(Direction.DOWN);
                super.accelerate();
            } 
            if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
                super.setDirection(Direction.UP);
                super.accelerate();
            }
        }
    }

    public boolean getFocus() {
        return this.isFocused;
    }

    public void setFocus(int focus) {
        if (focus == focusID) {
            this.isFocused = true;
        } else {
            this.isFocused = false;
        }
    }
}