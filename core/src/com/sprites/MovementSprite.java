package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.misc.Constants.Direction;
//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

// Constants import

import static com.misc.Constants.TILE_DIMS;

/**
 * MovementSprite adds movement facilities to a sprite.
 * @author Archie
 * @since 08/12/2019
 */
public class MovementSprite extends SimpleSprite {

    // physics values
    private float accelerationRate, decelerationRate, maxSpeed, rotationLockTime;
    private Vector2 speed;

    // layer that provides collisions
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
        this.maxSpeed = 200;
    }

    /**
     * Update the sprite position and direction based on acceleration and
     * boundaries. This is called every game frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        // Calculate the acceleration on the sprite and apply it
        accelerate();

        // Rotate sprite to face the direction its moving in
        updateRotation();

        super.update(batch);
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
        if (this.speed.len() >= this.accelerationRate && this.rotationLockTime <= 0) {
            // Use the shortest angle
            angle = (angle + 180) % 360 - 180;
            float rotationSpeed = 0.05f * this.speed.len();
            this.rotate(angle * rotationSpeed * Gdx.graphics.getDeltaTime());
        }
    }

    /*
     *  =======================================================================
     *                          Modified for Assessment 3
     *  =======================================================================
     */
    /**
     * Apply acceleration to the sprite, based on collision boundaries and
     * existing acceleration.
     */
    private void accelerate() {
        // Calculate whether it hits any boundaries
        int collisions = collidesWithBlockedTile(this.collisionLayer);
        // Check if it collides with any tiles, then move the sprite
        if (collisions == 0) {
            this.setX(this.getX() + this.speed.x * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed.y * Gdx.graphics.getDeltaTime());
            if (this.decelerationRate != 0) decelerate();
        } else if (collisions == 1){
            // Separate the sprite from the tile and stop sprite movement
            if (Math.abs(this.speed.x) > Math.abs(this.speed.y)) {
                this.speed = new Vector2(this.speed.x*.85f, -this.speed.y*.75f);
            } else {
                this.speed = new Vector2(-this.speed.x*.75f, this.speed.y*.75f);
            }
        } else {
            this.speed = new Vector2(-(this.speed.x*0.7f), -(this.speed.y*0.7f));
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

    /*
     *  =======================================================================
     *                          Modified for Assessment 3
     *  =======================================================================
     */
    /**
     * Checks if the tile at a location is a "blocked" tile or not.
     * @return Whether the hits a collision object (true) or not (false)
     */
    private int collidesWithBlockedTile(TiledMapTileLayer layer) {
        int collisions = 0;
        if (layer != null) {
            for (Vector2 vertex : getPolygonVertices(super.getMovementHitBox())) {
                if (layer.getCell(((int) (vertex.x / TILE_DIMS)), ((int) (vertex.y / TILE_DIMS))) != null) {
                    collisions++;
                }
            }
        }
        return collisions;
    }

    /*
     *  =======================================================================
     *                          Added for Assessment 3
     *  =======================================================================
     */
    /**
     * Gets the coordinates, as Vector2s for each vertex
     * of the polygon
     *
     * @param polygon   to get coordinates of
     * @return          list of coordinates
     */
    protected Array<Vector2> getPolygonVertices(Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
        Array<Vector2> result = new Array<>();
        for (int i = 0; i < vertices.length/2; i++) {
            float x = vertices[i * 2];
            float y = vertices[i * 2 + 1];
            result.add(new Vector2(x, y));
        }
        return result;
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
}