package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

// Constants import
import static com.config.Constants.MAP_HEIGHT;
import static com.config.Constants.MAP_WIDTH;
import static com.config.Constants.SPRITE_HEIGHT;
import static com.config.Constants.SPRITE_WIDTH;
import static com.config.Constants.COLLISION_TILE;

// Class to add movement code to a sprite
public class MovementSprite extends SimpleSprite {

    // For animations later
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    // Private values to be used in this class only
    private Direction direction;
    private Float movementSpeed;
    private TiledMapTileLayer collisionLayer;

    // Constructor for this class, gathers required information so that it can be drawn
    // Params:
    // Batch spriteBatch -  the batch that the sprite should be drawn on
    // Texture spriteTexture - the texture the sprite should use
    // TiledMapTileLayer collisionLayer - which layer of the map the sprite will collide with
    public MovementSprite(Batch spriteBatch, Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture);
        this.movementSpeed = 200f;
        this.collisionLayer = collisionLayer;
    }

    // Overload constructor for this class, takes a position to draw the sprite at
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    public MovementSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture, xPos, yPos);
        this.movementSpeed = 200f;
        this.collisionLayer = collisionLayer;
    }

    // Update the sprites position and direction. Called every game frame
    public void update() {
        // Look for key press input, then check if it collides with a collision tile
        // If it doesn't collide, move the sprite in that direction
        // and update the sprites direction so that animations can use later
        if (!collidesLeft() && Gdx.input.isKeyPressed(Keys.LEFT))
            setX(getX() - this.movementSpeed * Gdx.graphics.getDeltaTime());
            direction = Direction.LEFT;

		if (!collidesRight() && Gdx.input.isKeyPressed(Keys.RIGHT))
            setX(getX() + this.movementSpeed * Gdx.graphics.getDeltaTime());
            direction = Direction.RIGHT;
            
        if (!collidesBottom() && Gdx.input.isKeyPressed(Keys.DOWN))
            setY(getY() - this.movementSpeed * Gdx.graphics.getDeltaTime());
            direction = Direction.DOWN;

		if (!collidesTop() && Gdx.input.isKeyPressed(Keys.UP))
            setY(getY() + this.movementSpeed * Gdx.graphics.getDeltaTime());
            direction = Direction.UP;
        
        // Check the sprite is within the map boundaries then draw
        checkBoundaries();
        this.drawSprite();
    }

    // Checks if the tile at a location is a "Blocked" tile or not
    // Params:
    // float x, y - the coordinates the player is trying to move to
    // Return value:
    // Returns a boolean of whether the sprite can enter the tile or not
    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(COLLISION_TILE);
	}

    // Checks all tiles the sprite will cover in the specified direction to see if they're "Blocked"
    // Steps through each tile, step length is determined by the size of the sprite
    // Return value:
    // Returns a boolean of whether any tiles are "Blocked"
	private boolean collidesRight() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}

    // Same as above, checks left of sprite
	private boolean collidesLeft() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX(), getY() + step))
				return true;
		return false;
	}

    // Same as above, checks on top of sprite
	private boolean collidesTop() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

    // Same as above, checks the bottom of sprite
	private boolean collidesBottom() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}
    
    // Make sure the sprite stays within the map bounds
    private void checkBoundaries() {
        if (getY() < 0)
            setY(0);
        if (getY() > MAP_HEIGHT - SPRITE_HEIGHT)
            setY(MAP_HEIGHT - SPRITE_HEIGHT);
        if (getX() < 0)
            setX(0);
        if (getX() > MAP_WIDTH - SPRITE_WIDTH)
            setX(MAP_WIDTH - SPRITE_WIDTH);
    }
}