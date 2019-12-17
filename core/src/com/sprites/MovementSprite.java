package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
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
import static com.config.Constants.Direction;

/**
 * MovementSprite adds movement facilities to a sprite.
 * @author Archie
 * @since 08/12/2019
 */
public class MovementSprite extends SimpleSprite {

    // Private values to be used in this class only
    private Direction direction;
    private Float accelerationRate = 15f, accelerationX = 0f, accelerationY = 0f;
    private TiledMapTileLayer collisionLayer;

    /**
     * Constructors for this class, gathers required information so that it can be drawn.
     * @param spriteBatch    The batch that the sprite should be drawn on.
     * @param spriteTexture  The texture the sprite should use.
     * @param collisionLayer The layer of the map the sprite will collide with.
     */
    public MovementSprite(Batch spriteBatch, Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture);
        this.collisionLayer = collisionLayer;
    }

    /**
     * Overload constructor for this class, taking a position to draw the sprite at.
     * 
     * @param spriteBatch    The batch that the sprite should be drawn on.
     * @param spriteTexture  The texture the sprite should use.
     * @param collisionLayer The layer of the map the sprite will collide with.
     * @param xPos           The x-coordinate for the sprite to be drawn.
     * @param yPos           The y-coordinate for the sprite to be drawn.
     */
    public MovementSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture, xPos, yPos);
        this.collisionLayer = collisionLayer;
    }

    /**
     * Update the sprite position and direction based on acceleration and
     * boundaries. This is called every game frame.
     */
    public void update() {  
        // Calculate the acceleration on the sprite and apply it
        applyAcceleration();
        // Check the sprite is within the map boundaries then draw
        checkBoundaries();
        // Draw the sprite at the new location
        super.update();
    }

    /**
     * Apply acceleration to the sprite, based on collision boundaries and
     * existing acceleration.
     */
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

    /**
     * Checks if the tile at a location is a "blocked" tile or not.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return Whether the sprite can enter the tile (true) or not (false).
     */
    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(COLLISION_TILE);
	}

    /**
     * Checks all tiles the sprite will cover in the rightward direction to see
     * if they are "blocked". Steps through each tile, with step length
     * determined by the size of the sprite.
     * @return Whether any tiles on route are blocked (true) or no blockages (false).
     */
	private boolean collidesRight() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}

    /**
     * Checks all tiles the sprite will cover in the leftward direction to see if
     * they are "blocked". Steps through each tile, with step length determined by
     * the size of the sprite.
     * 
     * @return Whether any tiles on route are blocked (true) or no blockages
     *         (false).
     */
	private boolean collidesLeft() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX(), getY() + step))
				return true;
		return false;
	}

    /**
     * Checks all tiles the sprite will cover in the upward direction to see if
     * they are "blocked". Steps through each tile, with step length determined by
     * the size of the sprite.
     * 
     * @return Whether any tiles on route are blocked (true) or no blockages
     *         (false).
     */
	private boolean collidesTop() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

    /**
     * Checks all tiles the sprite will cover in the downward direction to see if
     * they are "blocked". Steps through each tile, with step length determined by
     * the size of the sprite.
     * 
     * @return Whether any tiles on route are blocked (true) or no blockages
     *         (false).
     */
	private boolean collidesBottom() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}
    
    /**
     * Ensures the sprite stays within the bounds set by the map.
     */
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

    /**
     * Increases the speed of the sprite (direction irrelevant). Acceleration
     * rate is based upon the sprite's properties.
     */
    public void accelerate() {
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

    /**
     * Decreases the speed of the sprite (direction irrelevant). Deceleration rate
     * is based upon the sprite's properties.
     */
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

    /**
     * Sets the current direction of the sprite.
     * @param dir The direction for the sprite.
     */
    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    /**
     * Gets the current direction of the sprite.
     * @return The direction of the sprite.
     */
    public Direction getDirection() {
        return this.direction;
    }
}