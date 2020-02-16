package com.entities;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.MovementSprite;
import com.sprites.SimpleSprite;

// Constants imports
import static com.misc.Constants.PROJECTILE_SPEED;
import static com.misc.Constants.PROJECTILE_WIDTH;
import static com.misc.Constants.PROJECTILE_HEIGHT;
import static com.misc.Constants.MAP_HEIGHT;
import static com.misc.Constants.MAP_WIDTH;

/**
 * The Projectile implementation. A sprite capable of moving and colliding with other sprites.
 * 
 * @author Archie
 * @since 09/01/2020
 */
public class Projectile extends MovementSprite {

    private final int damage;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a projectile capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param texture       The texture used to draw the projectile with.
     * @param x             The x-coordinate the projectile will start at.
     * @param y             The y-coordinate the projectile will start at.
     * @param damage        The amount of damage projectile does when it hits
     */
    public Projectile(Texture texture, float x, float y, int damage) {
        super(texture);
        this.setPosition(x, y);
        this.damage = damage;
        this.create();
    }

    /**
     * Sets the health of the projectile and its size provided in CONSTANTS.
     * Also initialises any properties needed by the projectile.
     */
    private void create() {
        this.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
        this.setAccelerationRate(0);
        this.setDecelerationRate(0);
        this.rotate(-90);
    }

    /**
     * Update the position and direction of the projectile every frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
    }

    /**
     * Calculates the trajectory from the projectiles starting point to the target point.
     * @param target The target the projectile is fired at
     */
    public void calculateTrajectory(SimpleSprite target) {
        // Convert the two points into vectors
        Vector2 targetVector = new Vector2(target.getCentreX(),target.getCentreY());
        Vector2 projectileVector = new Vector2(this.getCentreX(),this.getCentreY()); 

        // Work out the vector between them
        // Private values to be used in this class only
        Vector2 trajectory = targetVector.sub(projectileVector);
        trajectory.nor();

        // Scale the vector by the speed we want it to travel
        trajectory.scl(PROJECTILE_SPEED);

        // Give the projectile the vector to travel at
        this.rotate(trajectory.angle());
        this.setSpeed(trajectory);
    }

    /**
     * Gets whether the projectile is ready to be removed. It needs to be outside
     * of the map before it disappears.
     * 
     * @return Whether the projectile can be removed.
     */
    public boolean isOutOfMap() {
        boolean outVerticalView = this.getCentreY() > MAP_HEIGHT;
        outVerticalView = outVerticalView || this.getCentreY() < 0;
        boolean outHorizontalView = this.getCentreX() > MAP_WIDTH;
        outHorizontalView = outHorizontalView || this.getCentreX() < 0;
        return outVerticalView || outHorizontalView;
    }

    public int getDamage() {
        return this.damage;
    }

}