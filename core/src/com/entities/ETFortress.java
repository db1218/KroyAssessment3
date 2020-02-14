package com.entities;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.misc.Constants.*;
import com.screens.GameScreen;
import com.sprites.SimpleSprite;

// Constants import
import static com.misc.Constants.ETFORTRESS_HEIGHT;
import static com.misc.Constants.ETFORTRESS_WIDTH;

/**
 * The ET Fortress implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class ETFortress extends SimpleSprite {

    // Private values for this class to use
    private final Texture destroyed;
    private Circle detectionRange;
    private boolean flooded;
    private final FortressType type;
    private final GameScreen gameScreen;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture           The texture used to draw the ETFortress with.
     * @param destroyedTexture  The texture used to draw the ETFortress with. when it has been destroyed.
     * @param scaleX            The scaling in the x-axis.
     * @param scaleY            The scaling in the y-axis.
     * @param xPos              The x-coordinate for the ETFortress.
     * @param yPos              The y-coordinate for the ETFortress.
     */
    public ETFortress(Texture texture, Texture destroyedTexture, float scaleX, float scaleY, float xPos, float yPos, FortressType type, GameScreen gameScreen) {
        super(texture);
        this.gameScreen = gameScreen;
        this.destroyed = destroyedTexture;
        this.flooded = false;
        this.type = type;
        this.setScale(scaleX, scaleY);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Sets the health of the ETFortress and its size provided in CONSTANTS.
     */
    private void create() {
        this.getHealthBar().setMaxResource((int) Math.max(ETFORTRESS_WIDTH * this.getScaleX(), ETFORTRESS_HEIGHT * this.getScaleY()));
        this.setSize(ETFORTRESS_WIDTH * this.getScaleX(), ETFORTRESS_HEIGHT * this.getScaleY());
        this.detectionRange = new Circle(this.getCentreX(), this.getCentreY(), type.getRange());
    }

    /**
     * Update the fortress so that it is drawn every frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        // If ETFortress is destroyed, change to flooded texture
        // If ETFortress is damaged, heal over time
        if (!flooded && this.getHealthBar().getCurrentAmount() <= 0) {
            this.removeSprite(this.destroyed);
            this.flooded = true;
            this.gameScreen.showPopupText("You have destroyed " + gameScreen.getETFortressesDestroyed()[0] + " / " + gameScreen.getETFortressesDestroyed()[1] +
                    " fortresses", 1, 7);
        } else if (!flooded && this.getInternalTime() % 150 == 0 && this.getHealthBar().getCurrentAmount() != this.getHealthBar().getMaxAmount()) {
            // Heal ETFortresses every second if not taking damage
			this.getHealthBar().addResourceAmount(type.getHealing());
        }
        // Set the detection radius
        this.detectionRange.setPosition(this.getCentreX(), this.getCentreY());
    }

    /**
     * Check to see if the ETFortress should fire another projectile. The pattern the ETFortress
     * uses to fire can be modified here. 
     * 
     * @return boolean  Whether the ETFortress is ready to fire again (true) or not (false)
     */
    public boolean canShootProjectile() {
        return this.getHealthBar().getCurrentAmount() > 0 && this.getInternalTime() < 120 && this.getInternalTime() % 30 == 0;
    }

    /**
     * Checks if a polygon is within the range of the ETFortress.
     * Usually used to see if a firetruck is close enough to be attacked.
     * 
     * @param polygon  The polygon that needs to be checked.
     * @return         Whether the given polygon is in the radius of the ETFortress
     */
    public boolean isInRadius(Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
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

    public boolean isFlooded() {
        return this.flooded;
    }

    public FortressType getType() {
        return this.type;
    }
}