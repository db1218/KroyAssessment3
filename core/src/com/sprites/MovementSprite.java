package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

// Constants import
import static com.config.Constants.MAP_HEIGHT;
import static com.config.Constants.MAP_WIDTH;
import static com.config.Constants.COLLISION_TILE;
import static com.config.Constants.Direction;
import static com.config.Constants.DirectionToAngle;
import static com.config.Constants.TILE_DIMS;

/**
 * MovementSprite adds movement facilities to a sprite.
 * @author Archie
 * @since 08/12/2019
 */
public class MovementSprite extends SimpleSprite {

    // Private values to be used in this class only
    private Direction direction;
    private float accelerationRate, maxSpeed, speedX, speedY, restitution, rotationLockTime;
    private TiledMapTileLayer collisionLayer;

    /**
     * Creates a sprite capable of moving and colliding with the tiledMap and other sprites.
     * 
     * @param spriteTexture  The texture the sprite should use.
     * @param collisionLayer The layer of the map the sprite will collide with.
     */
    public MovementSprite(Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteTexture);
        this.collisionLayer = collisionLayer;
        this.create();
    }

    /**
     * Sets the inital values for all properties needed by the sprite.
     */
    private void create() {
        this.direction = Direction.LEFT;
        this.speedX = 0f;
        this.speedY = 0f; 
        this.rotationLockTime = 0;
    }

    /**
     * Update the sprite position and direction based on acceleration and
     * boundaries. This is called every game frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        // Calculate the acceleration on the sprite and apply it
        accelerate();
        // Set the sprites direction based on its speed
        setDirection(getDirectionFromSpeed());
        // Rotate sprite to face the direction its moving in
        updateRotation();
        // Update rotationLockout if set
        if (this.rotationLockTime >= 0) this.rotationLockTime -= 1;
        // Check the sprite is within the map boundaries then draw
        checkBoundaries();
        // Draw the sprite at the new location
        super.update(batch);
    }

    /**
     * Increases the speed of the sprite in the given direction.
     * @param direction The direction to accelerate in.
     */
    public void applyAcceleration(Direction direction) {
        if (this.speedY < this.maxSpeed && direction == Direction.UP) {
            this.speedY += this.accelerationRate;
        }
        if (this.speedY > -this.maxSpeed && direction == Direction.DOWN) {
            this.speedY -= this.accelerationRate;
        }
        if (this.speedX < this.maxSpeed && direction == Direction.RIGHT) {
            this.speedX += this.accelerationRate;
        }
        if (this.speedX > -this.maxSpeed && direction == Direction.LEFT) {
            this.speedX -= this.accelerationRate;
        }
    }

    /**
     * Checks what direction the sprite is facing and bounces it the opposite way.
     * @param seperationVector Vector containing the minimum distance needed to travel to seperate two sprites.
     */
    public void collisionOccurred(Vector2 seperationVector) {
        // Calculate how far to push the sprite back
        float pushBackX = seperationVector.x;
        float pushBackY = seperationVector.y;
        // For each direction, reverse the speed and set the sprite back a few coordinates out of the collision
        this.speedY *= -this.restitution;
        this.speedX *= -this.restitution;
        this.setRotationLock(0.5f);
        this.setY(this.getY() + pushBackY);
        this.setX(this.getX() + pushBackX);
    }

    /**
     * Sets the amount of time the sprite cannot rotate for.
     * @param milliseconds The duration the sprite cannot rotate in.
     */
    public void setRotationLock(float duration) {
        if (duration > 0 && this.rotationLockTime <= 0) {
            this.rotationLockTime = duration * 100;
        }
    }

    /**
     * Checks if the tile at a location is a "blocked" tile or not.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return Whether the sprite can enter the tile (true) or not (false).
     */
    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) (x / TILE_DIMS), (int) (y / TILE_DIMS));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(COLLISION_TILE);
	}

    /**
     * Checks all tiles the sprite will cover in the rightward direction to see
     * if they are "blocked". Steps through each tile, with step length
     * determined by the size of the sprite.
     * @return Whether any tiles on route are blocked (true) or no blockages (false).
     */
	private boolean collidesRight() {
		for(float step = 0; step < this.getHeight(); step += TILE_DIMS / 2)
			if(isCellBlocked(this.getX() + this.getWidth(), this.getY() + step))
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
		for(float step = 0; step < this.getHeight(); step += TILE_DIMS / 2)
			if(isCellBlocked(this.getX(), this.getY() + step))
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
		for(float step = 0; step < this.getWidth(); step += TILE_DIMS / 2)
			if(isCellBlocked(this.getX() + step, this.getY() + this.getHeight()))
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
		for(float step = 0; step < this.getWidth(); step += TILE_DIMS / 2)
			if(isCellBlocked(this.getX() + step, this.getY()))
				return true;
		return false;
	}
    
    /**
     * Ensures the sprite stays within the bounds set by the map.
     */
    private void checkBoundaries() {
        if (getY() < 0)
            setY(0);
        if (getY() > MAP_HEIGHT - this.getHeight())
            setY(MAP_HEIGHT - this.getHeight());
        if (getX() < 0)
            setX(0);
        if (getX() > MAP_WIDTH - this.getWidth())
            setX(MAP_WIDTH - this.getWidth());
    }

    /**
     * Calculate the angle the sprite needs to rotate from it's current rotation to the new rotation.
     */
    private void updateRotation() {
        float currentRotation = this.getRotation(), desiredRotation = DirectionToAngle(this.direction);
        float angle = desiredRotation - currentRotation;
        if (currentRotation != desiredRotation) {
            // Use the shortest angle
            angle = (angle + 180) % 360 - 180;
            this.rotate(angle * 2 * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Calculate the sprite's direction from its speed
     * @return The direction the sprite is travelling in
     */
    private Direction getDirectionFromSpeed() {
        // Allow the sprite to reverse in any direction if going slow enough
        float speedBeforeRotation = this.accelerationRate * 3.5f;
        boolean left = this.speedX < -speedBeforeRotation, right = this.speedX > speedBeforeRotation;
        boolean up = this.speedY > speedBeforeRotation, down = this.speedY < -speedBeforeRotation;
        boolean vertical = up || down, horizontal = left || right;
        if (vertical && (this.rotationLockTime <= 0)) {
            if (up && horizontal) {
                return left ? Direction.UPLEFT : Direction.UPRIGHT;
            } else if (down && horizontal) {
                return left ? Direction.DOWNLEFT : Direction.DOWNRIGHT;
            } else if (up) {
                return Direction.UP;
            }
            return Direction.DOWN;
        } else if (horizontal && (this.rotationLockTime <= 0)) {
            return left ? Direction.LEFT : Direction.RIGHT;
        }
        // If stationary return last direction
        return this.direction;
    }

    /**
     * Apply acceleration to the sprite, based on collision boundaries and
     * existing acceleration.
     */
    private void accelerate() {
        // Calculate whether it hits any boundaries
        boolean collides = collidesLeft() || collidesRight() || collidesTop() || collidesBottom();
        // Check if it collides with any tiles, then move the sprite
        if (!collides) {
            setX(getX() + this.speedX * Gdx.graphics.getDeltaTime());
            setY(getY() + this.speedY * Gdx.graphics.getDeltaTime());
            decelerate();
        } else {
            // Seperate the sprite from the tile depending on where its collided
            collisionOccurred(new Vector2(collidesRight() ? -1 : 1, collidesTop() ? -1 : 1));
        }
    }

    /**
     * Decreases the speed of the sprite (direction irrelevant). Deceleration rate
     * is based upon the sprite's properties.
     */
    private void decelerate() {
        float decelerationRate = this.accelerationRate * 0.6f;
        // Stops it bouncing from decelerating in one direction and then another etc..
        if (this.speedY < decelerationRate && this.speedY > -decelerationRate) {
            this.speedY = 0f;
        } else {
            this.speedY -= this.speedY > 0 ? decelerationRate : -decelerationRate;
        }
        if (this.speedX < decelerationRate && this.speedX > -decelerationRate) {
            this.speedX = 0f;
        } else {
            this.speedX -= this.speedX > 0 ? decelerationRate : -decelerationRate;
        }
    }
    

    /**
     * Sets the amount the sprite will bounce upon collisions.
     * @param bounce The restitution of the sprite.
     */
    public void setRestitution(float bounce) {
        this.restitution = bounce;
    }

    /**
     * Sets the rate at which the sprite will accelerate.
     * @param rate The acceleration rate for the sprite.
     */
    public void setAccelerationRate(float rate) {
        this.accelerationRate = rate;
    }

    /**
     * Sets the max speed the sprite can accelerate to.
     * @param amount The max speed value for the sprite.
     */
    public void setMaxSpeed(float amount) {
        this.maxSpeed = amount;
    }

     /**
     * Returns the max speed the sprite can accelerate to.
     * @return  The max speed value for the sprite.
     */
    public float getMaxSpeed() {
        return this.maxSpeed;
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

    /**
     * Sets the current speed of the sprite in the X axis.
     * @param speed The speed the sprite should travel.
     */
    public void setSpeedX(Float speed) {
        this.speedX = speed;
    }

    /**
     * Gets the current speed of the sprite in the X axis.
     * @return The current speed of the sprite in the X axis.
     */
    public float getSpeedX() {
        return this.speedX;
    }

    /**
     * Sets the current speed of the sprite in the Y axis.
     * @param speed The speed the sprite should travel.
     */
    public void setSpeedY(Float speed) {
        this.speedY = speed;
    }

    /**
     * Gets the current speed of the sprite in the Y axis.
     * @return The current speed of the sprite in the Y axis.
     */
    public float getSpeedY() {
        return this.speedY;
    }
}