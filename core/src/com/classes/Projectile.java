package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.MovementSprite;

// Constants imports
import static com.config.Constants.PROJECTILE_SPEED;
import static com.config.Constants.PROJECTILE_WIDTH;
import static com.config.Constants.PROJECTILE_HEIGHT;
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.MAP_HEIGHT;
import static com.config.Constants.MAP_WIDTH;

/**
 * The Projectile implementation. A sprite capable of moving and colliding with other sprites.
 * 
 * @author Archie
 * @since 09/01/2020
 */
public class Projectile extends MovementSprite {

    // Private values to be used in this class only
    Vector2 trajectory;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a projectile capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param texture        The texture used to draw the projectile with.
     * @param x              The x-coordinate the projectile will start at.
     * @param y              The y-coordinate the projectile will start at.
     */
    public Projectile(Texture texture, float x, float y) {
        super(texture);
        this.setPosition(x, y);
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
    public void calculateTrajectory(Polygon target) {
        // Convert the two points into vectors
        Vector2 targetVector = new Vector2(target.getX(),target.getY()); 
        Vector2 projectileVector = new Vector2(this.getCentreX(),this.getCentreY()); 

        // Work out the vector between them
        this.trajectory = targetVector.sub(projectileVector);
        this.trajectory.nor();

        // Scale the vector by the speed we want it to travel
        this.trajectory.scl(PROJECTILE_SPEED);

        // Give the projectile the vector to travel at
        this.rotate(this.trajectory.angle());
        this.setSpeed(trajectory);
    }
    
    /**
     * Gets whether the projectile is ready to be removed. It needs to be offscreen
     * so it doesn't disappear in view.
     * @param cameraPosition The position of the camera, needed to calculate the edge of the screen
     * 
     * @return Whether the projectile can be removed.
     */
    public boolean isOutOfView(Vector3 cameraPosition) {
        boolean outVerticalView = this.getCentreY() > cameraPosition.y + SCREEN_HEIGHT;
        outVerticalView = outVerticalView || this.getCentreY() < cameraPosition.y - SCREEN_HEIGHT;
        boolean outHorizontalView = this.getCentreX() > cameraPosition.x + SCREEN_WIDTH;
        outHorizontalView = outHorizontalView || this.getCentreX() < cameraPosition.x - SCREEN_WIDTH;
        return outVerticalView || outHorizontalView;
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

}