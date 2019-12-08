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
    private Float accelerationRate = 15f, accelerationX = 0f, accelerationY = 0f;
    private TiledMapTileLayer collisionLayer;

    // Constructor for this class, gathers required information so that it can be drawn
    // Params:
    // Batch spriteBatch -  the batch that the sprite should be drawn on
    // Texture spriteTexture - the texture the sprite should use
    // TiledMapTileLayer collisionLayer - which layer of the map the sprite will collide with
    public MovementSprite(Batch spriteBatch, Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture);
        this.collisionLayer = collisionLayer;
    }

    // Overload constructor for this class, takes a position to draw the sprite at
    // Params:
    // float xPos, yPos -  the co-ordinates the sprite should be drawn at
    public MovementSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture, xPos, yPos);
        this.collisionLayer = collisionLayer;
    }

    // Update the sprites position and direction. Called every game frame
    public void update() {
        // Look for key press input, then accelerate the sprite in that direction
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction = Direction.LEFT;
            accelerate();
        }
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            direction = Direction.RIGHT;
            accelerate();
        }          
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction = Direction.DOWN;
            accelerate();
        } 
		if (Gdx.input.isKeyPressed(Keys.UP)) {
            direction = Direction.UP;
            accelerate();
        }
        // Calculate the acceleration on the sprite and apply it
        applyAcceleration();
        // Check the sprite is within the map boundaries then draw
        checkBoundaries();
        // Draw the srpite at the new location
        this.drawSprite();
    }

    // Apply acceleration to the sprite
    private void applyAcceleration() {
        // Calculate whether it hits any boundaries
        // Do this once here rather than multiple times in code
        boolean hitLeft = collidesLeft();
        boolean hitRight = collidesRight();
        boolean hitTop = collidesTop();
        boolean hitBottom = collidesBottom();
        // Apply acceleration and check if it collides with any tiles
        if (!hitLeft && this.accelerationX < 0) {
            setX(getX() + this.accelerationX * Gdx.graphics.getDeltaTime());
        } else if (hitLeft) {
            this.accelerationX = 0f;
        }
        if (!hitRight && this.accelerationX > 0) {
            setX(getX() + this.accelerationX * Gdx.graphics.getDeltaTime());
        } else if (hitRight) {
            this.accelerationX = 0f;
        }
        if (!hitTop && this.accelerationY > 0) {
            setY(getY() + this.accelerationY * Gdx.graphics.getDeltaTime());
        } else if (hitTop) {
            this.accelerationY = 0f;
        }
        if (!hitBottom && this.accelerationY < 0) {
            setY(getY() + this.accelerationY * Gdx.graphics.getDeltaTime());
        } else if (hitBottom) {
            this.accelerationY = 0f;
        }
        if (this.accelerationY != 0f || this.accelerationX != 0f) {
            decelerate();
        }
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

    // Accelerate the sprite in the direction it is facing
    private void accelerate() {
        float maxSpeed = 300f;
        if (this.accelerationY < maxSpeed && direction == Direction.UP) {
            this.accelerationY += this.accelerationRate;
        }
        if (this.accelerationY > -maxSpeed && direction == Direction.DOWN) {
            this.accelerationY -= this.accelerationRate;
        }
        if (this.accelerationX < maxSpeed && direction == Direction.RIGHT) {
            this.accelerationX += this.accelerationRate;
        }
        if (this.accelerationX > -maxSpeed && direction == Direction.LEFT) {
            this.accelerationX -= this.accelerationRate;
        }
    }

    // Decelerate the sprite
    private void decelerate() {
        float decelerationRate = this.accelerationRate * 0.5f;
        float restThreshold = this.accelerationRate;
        // Check the direction the sprite is moving based on its velocity
        if (this.accelerationY > 0) {
            // If within a threshold stop the spirte
            // Stops it bouncing from decelerating in one direction and then another etc..
            if (this.accelerationY < restThreshold) {
                this.accelerationY = 0f;
            } else {
                this.accelerationY -= decelerationRate;
            }
        } else {
            if (this.accelerationY > -restThreshold) {
                this.accelerationY = 0f;
            } else {
                this.accelerationY += decelerationRate;
            }
        }
        // Repeat for the x axis
        if (this.accelerationX > 0) {
            if (this.accelerationX < restThreshold) {
                this.accelerationX = 0f;
            } else {
                this.accelerationX -= decelerationRate;
            }
        } else {
            if (this.accelerationX > -restThreshold) {
                this.accelerationX = 0f;
            } else {
                this.accelerationX += decelerationRate;
            }
        }
    }
}