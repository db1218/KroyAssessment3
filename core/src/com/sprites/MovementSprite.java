package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.config.Constants.Direction;

// Constants import
import static com.config.Constants.COLLISION_TILE;
import static com.config.Constants.TILE_DIMS;

/**
 * MovementSprite adds movement facilities to a sprite.
 * @author Archie
 * @since 08/12/2019
 */
public class MovementSprite extends SimpleSprite {

    // Private values to be used in this class only
    private float accelerationRate, decelerationRate, maxSpeed, restitution, rotationLockTime;
    private Vector2 speed;
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
     * Creates a sprite capable of moving and but only colliding with other sprites.
     * 
     * @param spriteTexture  The texture the sprite should use.
     */
    public MovementSprite(Texture spriteTexture) {
        super(spriteTexture);
        this.create();
    }

    /**
     * Sets the inital values for all properties needed by the sprite.
     */
    private void create() {
        this.speed = new Vector2(0,0);
        this.accelerationRate = 10;
        this.decelerationRate = 6;
        this.rotationLockTime = 0;
        this.restitution = 0.8f;
        this.maxSpeed = 200;
    }

    /**
     * Update the sprite position and direction based on acceleration and
     * boundaries. This is called every game frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        // Calculate the acceleration on the sprite and apply it
        accelerate();
        // Rotate sprite to face the direction its moving in
        updateRotation();
        // Update rotationLockout if set
        if (this.rotationLockTime >= 0) this.rotationLockTime -= 1; 
    }

    /**
     * Increases the speed of the sprite in the given direction.
     * @param direction The direction to accelerate in.
     */
    public void applyAcceleration(Direction direction) {
        if (this.speed.y < this.maxSpeed && direction == Direction.UP) {
            this.speed.y += this.accelerationRate;
        }
        if (this.speed.y > -this.maxSpeed && direction == Direction.DOWN) {
            this.speed.y -= this.accelerationRate;
        }
        if (this.speed.x < this.maxSpeed && direction == Direction.RIGHT) {
            this.speed.x += this.accelerationRate;
        }
        if (this.speed.x > -this.maxSpeed && direction == Direction.LEFT) {
            this.speed.x -= this.accelerationRate;
        }
    }

    /**
     * Calculate the angle the sprite needs to rotate from it's current rotation to the new rotation.
     */
    private void updateRotation() {
        float currentRotation = this.getRotation();
        float desiredRotation = this.speed.angle();
        float angle = desiredRotation - currentRotation;
        if (this.speed.len() >= this.accelerationRate) {
            // Use the shortest angle
            angle = (angle + 180) % 360 - 180;
            this.rotate(angle * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Apply acceleration to the sprite, based on collision boundaries and
     * existing acceleration.
     */
    private void accelerate() {
        // Calculate whether it hits any boundaries
        boolean collides = this.collisionLayer != null && (collidesLeft() || collidesRight() || collidesTop() || collidesBottom());
        // Check if it collides with any tiles, then move the sprite
        if (!collides) {
            setX(getX() + this.speed.x * Gdx.graphics.getDeltaTime());
            setY(getY() + this.speed.y * Gdx.graphics.getDeltaTime());
            if (this.decelerationRate != 0) decelerate();
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
        // Stops it bouncing from decelerating in one direction and then another etc..
        if (this.speed.y < this.decelerationRate && this.speed.y > -this.decelerationRate) {
            this.speed.y = 0f;
        } else {
            this.speed.y -= this.speed.y > 0 ? this.decelerationRate : -this.decelerationRate;
        }
        if (this.speed.x < this.decelerationRate && this.speed.x > -this.decelerationRate) {
            this.speed.x = 0f;
        } else {
            this.speed.x -= this.speed.x > 0 ? this.decelerationRate : -this.decelerationRate;
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
        this.speed.y *= -this.restitution;
        this.speed.x *= -this.restitution;
        this.setRotationLock(0.5f);
        this.setY(this.getY() + pushBackY);
        this.setX(this.getX() + pushBackX);
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
     * Sets the amount of time the sprite cannot rotate for.
     * @param milliseconds The duration the sprite cannot rotate in.
     */
    public void setRotationLock(float duration) {
        if (duration > 0 && this.rotationLockTime <= 0) {
            this.rotationLockTime = duration * 100;
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
     * Sets the rate at which the sprite will decelerate.
     * @param rate The deceleration rate for the sprite.
     */
    public void setDecelerationRate(float rate) {
        this.decelerationRate = rate;
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
     * Sets the current speed of the sprite.
     * @param speed The speed the sprite should travel.
     */
    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    /**
     * Gets the current speed of the sprite.
     * @return The current speed of the sprite.
     */
    public Vector2 getSpeed() {
        return this.speed;
    }
}