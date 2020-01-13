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
import static com.config.Constants.ETFORTRESS_HEALTH;
import static com.config.Constants.ETFORTRESS_HEIGHT;
import static com.config.Constants.ETFORTRESS_WIDTH;

/**
 * The ET Fortress implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class ETFortress extends SimpleSprite {

    // Private values for this class to use
    private Texture destroyed;
    private Circle detectionRange;
    private float timeBetweenProjectiles;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture  The texture used to draw the ETFortress with.
     * @param scaleX   The scaling in the x-axis.
     * @param scaleY   The scaling in the y-axis.
     * @param xPos     The x-coordinate for the ETFortress.
     * @param yPos     The y-coordinate for the ETFortress.
     */
    public ETFortress(Texture texture, Texture destroyedTexture, float scaleX, float scaleY, float xPos, float yPos) {
        super(texture);
        this.destroyed = destroyedTexture;
        this.setScale(scaleX, scaleY);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture   The texture used to draw the ETFortress with.
     * @param scaleX    The scaling in the x-axis.
     * @param scaleY    The scaling in the y-axis.
     */
    public ETFortress(Texture texture, Texture destroyedTexture, float scaleX, float scaleY) {
        super(texture);
        this.destroyed = destroyedTexture;
        this.setScale(scaleX, scaleY);
        this.create();
    }

    /**
     * Simplfied constructor for the ETFortress, that doesn't require a position.
     * Drawn with the given texture at (0,0).
     * 
     * @param texture  The texture used to draw the ETFortress with.
     */
    public ETFortress(Texture texture, Texture destroyedTexture) {
        super(texture);
        this.destroyed = destroyedTexture;
        this.create();
    }

    /**
     * Sets the health of the ETFortress and its size provided in CONSTANTS.
     */
    private void create() {
        this.getHealthBar().setMaxResource(ETFORTRESS_HEALTH);
        this.setSize(ETFORTRESS_WIDTH * this.getScaleX(), ETFORTRESS_HEIGHT * this.getScaleY());
        this.detectionRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth() * 2);
    }

    /**
     * Update the fortress so that it is drawn every frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        if (this.getHealthBar().getCurrentAmount() <= 0) this.removeSprite(this.destroyed);
        this.detectionRange.setPosition(this.getCentreX(), this.getCentreY());
        if (this.timeBetweenProjectiles > 0) this.timeBetweenProjectiles -= 1;
    }

    /**
     * Check to see if the ETFortress should fire another projectile. The pattern the ETFortress
     * uses to fire can be modified here. 
     * 
     * @return boolean  Whether the ETFortress is ready to fire again (true) or not (false)
     */
    public boolean canShootProjectile() {
        if (this.getHealthBar().getCurrentAmount() > 0 && this.timeBetweenProjectiles < 120 && this.timeBetweenProjectiles % 30 == 0) {
            if (this.timeBetweenProjectiles <= 0) this.timeBetweenProjectiles = 150;
            return true;
        }
        return false;
    }

    /**
     * Checks if a polygon is within the range of the firestation.
     * Usually used to see if a firetruck is close enough to be repaired.
     * 
     * @param polygon  The polygon that needs to be checked.
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


    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the range circle.
     * 
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
        renderer.circle(this.detectionRange.x, this.detectionRange.y, this.detectionRange.radius);
    }
}