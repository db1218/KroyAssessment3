package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.FIRESTATION_HEALTH;
import static com.config.Constants.FIRESTATION_HEIGHT;
import static com.config.Constants.FIRESTATION_WIDTH;
import static com.config.Constants.FIRETRUCK_REPAIR_SPEED;

/**
 * The Firestation implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class Firestation extends SimpleSprite {

    // Private values for this class to use
    private Circle repairRange;
    private float repairTimeout;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture  The texture used to draw the Firestation with.
     * @param xPos     The x-coordinate for the Firestation.
     * @param yPos     The y-coordinate for the Firestation.
     */
    public Firestation(Texture texture, float xPos, float yPos) {
        super(texture);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Simplfied constructor for the Firestation, that doesn't require a position.
     * Drawn with the given texture at (0,0).
     * 
     * @param texture  The texture used to draw the Firestation with.
     */
    public Firestation(Texture texture) {
        super(texture);
        this.create();
    }

    /**
     * Sets the health of the Firestation and its size provided in CONSTANTS.
     * Also creates a circle to indicate the range firetrucks should be within
     * in order to be repaired by the firestation.
     */
    private void create() {
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
        this.repairRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth());
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     * Also reduces the time before next repair can occur.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        this.repairRange.setPosition(this.getCentreX(), this.getCentreY());
        if (this.repairTimeout >= 0) this.repairTimeout -= 1;
    }

    /**
     * Repair a firetruck over time.
     * 
     * @param firetruck  The firetruck that will be repaired.
     */
    public void repair(Firetruck firetruck) {
        if (this.repairTimeout <= 0) {
            firetruck.getHealthBar().addResourceAmount((int) firetruck.getHealthBar().getMaxAmount() / FIRETRUCK_REPAIR_SPEED);
            firetruck.getWaterBar().addResourceAmount((int) firetruck.getWaterBar().getMaxAmount() / FIRETRUCK_REPAIR_SPEED);
            this.repairTimeout = 10;
        }
    }

    /**
     * Checks if a polygon is within the range of the firestation.
     * Usually used to see if a firetruck is close enough to be repaired.
     * 
     * @param polygon  The polygon that needs to be checked.
     */
    public boolean isInRadius(Polygon polygon) {
        float []vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(this.repairRange.x, this.repairRange.y);
        float squareRadius = this.repairRange.radius * this.repairRange.radius;
        for (int i = 0; i < vertices.length; i+=2){
            if (i == 0){
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                    return true;
            }
        }
        return polygon.contains(this.repairRange.x, this.repairRange.y);
    }

    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the range circle.
     * 
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
        renderer.circle(this.repairRange.x, this.repairRange.y, this.repairRange.radius);
    }
}