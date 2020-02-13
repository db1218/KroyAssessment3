package com.entities;

import com.pathFinding.MapGraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.sprites.PatrolMovementSprite;

import java.util.ArrayList;

/** This class is used to create a patrol. A patrol can
 * detect and attack a firetruck if a firetruck is within
 * it's range. It can also be attacked by a firetruck. It
 * extends PatrolMovementSprite which determines how the
 * patrol moves around the map
 */

public class Patrol extends PatrolMovementSprite {

    // List of textures which are drawn on top of each other
    // to create a 3D looking image
    final ArrayList<Texture> textureSlices;

    // The range where a patrol can 'see' a firetruck
    private final Circle detectionRange;

    // Whether the patrol has been killed by a firetruck or not
    private  boolean isDead;

    /** The constructor for Patrol
     *
     * @param textureSlices List of textures that are layered on top of each other to
     *                      draw the patrol
     * @param mapGraph      Graph of junctions - used by PatrolMovementSprite to
     *                      determine the patrol's movement
     */
    public Patrol(ArrayList<Texture> textureSlices, MapGraph mapGraph){
        super(textureSlices.get(textureSlices.size() - 1), mapGraph);
        this.getHealthBar().setMaxResource(25);
        this.textureSlices = textureSlices;
        this.isDead = false;
        this.detectionRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth() * 3);
    }

    /** Called from gameScreen, first checks whether the patrol has
     * been killed, it draws the patrol calls super to update the
     * patrols movement and updates its detection range so that it
     * moves with the movement of the patrol.
     *
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        checkIfDead();
        drawVoxelImage(batch);
        super.update(batch);
        updateDetectionRange();
    }

    /**
     * Just updates the movement of the patrol, doesn't
     * draw the sprite or check detection range
     */
    public void updateMovement() {
        super.step();
    }

    /**
     * Draws the voxel representation of the patrol. Incrementally builds the patrol
     * from layers of images with each image slightly higher than the last
     */
    private void drawVoxelImage(Batch batch) {
        int slicesLength = this.textureSlices.size() - 1;
        float x = getX();
        float y = getY();
        float angle = this.getRotation();
        float width = this.getWidth();
        float height = this.getHeight();

        for (int i = 0; i < slicesLength; i++) {
            Texture texture = this.textureSlices.get(i);
            batch.draw(new TextureRegion(texture), x, (y - slicesLength / 3f) + i, width / 2, height / 2, width, height, 1, 1, angle, true);
        }
    }

    /** updates the x and y position of the patrol's detection
     * range so that it is at the same x and y position as the
     * patrol as the patrol moves
     */
    private void updateDetectionRange() {
        this.detectionRange.setX(this.getX());
        this.detectionRange.setY(this.getY());
    }

    /** Checks if the patrol is dead and if so sets this.isDead to true */
    private void checkIfDead() {
        if (this.getHealthBar().getCurrentAmount() == 0) this.isDead = true;
    }

    /**
     * Checks if a polygon is within the range of the patrol.
     * Usually used to see if a firetruck is close enough to be attacked.
     *
     * @param polygon  The polygon that needs to be checked.
     * @return <code> true </code>  If given polygon is in the radius of the patrol
     *          <code> false </code> If given polygon is not in the radius of the patrol
     */
    public boolean isInRadius(Polygon polygon) {
        float []vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(this.detectionRange.x, this.detectionRange.y);
        float squareRadius = this.detectionRange.radius * this.detectionRange.radius;
        for (int i = 0; i < vertices.length; i+=2){
            if (i == 0){
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                    return true;
            }
        }
        return polygon.contains(this.detectionRange.x, this.detectionRange.y);
    }

    /** Returns if a patrol can shoot a projectile
     * @return <code> true </code> if 30ms has passed since the
     *                              patrol last fired a projectile
     *          <code> false </code> if less than 30ms has passed
     *                              since the patrol last fired a
     *                               projectile*/
    public boolean canShootProjectile() {
        return this.getInternalTime() % 30 == 0;
    }

    /** If a patrol has died have to remove the road the
     * patrol locked when it died from lockedRoads in mapGraph
     * Otherwise no patrols will be able to travel on that road
     *
     * @param mapGraph Graph of junctions - used by PatrolMovementSprite
     *      *          to determine the patrol's movement
     */
    public void removeDead(MapGraph mapGraph){
        mapGraph.removeDead(super.getThis());
    }

    public boolean isDead() {
        return this.isDead;
    }

}





