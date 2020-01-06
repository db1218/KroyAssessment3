package com.classes;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.Input.Keys;

// Custom class import
import com.sprites.MovementSprite;

// Constants imports
import static com.config.Constants.Direction;
import static com.config.Constants.FIRETRUCK_HEALTH;
import static com.config.Constants.FIRETRUCK_HEIGHT;
import static com.config.Constants.FIRETRUCK_WIDTH;
import static com.config.Constants.FIRETRUCK_ACCELERATION;
import static com.config.Constants.FIRETRUCK_MAX_SPEED;
import static com.config.Constants.FIRETRUCK_RESTITUTION;

// Java util import
import java.util.ArrayList;

/**
 * The Firetruck implementation. A sprite capable of moving and colliding with other sprites.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class Firetruck extends MovementSprite {

    // Private values to be used in this class only
    private Boolean isFocused;
    private int focusID, redLightDelay;
    private ArrayList<Texture> firetruckSlices;
    private Polygon hoseRange;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     * @param xPos           The x-coordinate for the firetruck.
     * @param yPos           The y-coordinate for the firetruck.
     */
    public Firetruck(ArrayList<Texture> textureSlices, float xPos, float yPos, TiledMapTileLayer collisionLayer, int ID) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.focusID = ID;
        this.firetruckSlices = textureSlices;
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Simplfied constructor for the firetruck, that doesn't require a position.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at (0,0).
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     */
    public Firetruck(ArrayList<Texture> textureSlices, TiledMapTileLayer collisionLayer, int ID) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.focusID = ID;
        this.firetruckSlices = textureSlices;
        this.create();
    }

    /**
     * Sets the health of the firetruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the firetruck.
     */
    private void create() {
        this.redLightDelay = 0;
        this.getHealthBar().setMaxResource(FIRETRUCK_HEALTH);
        this.setSize(FIRETRUCK_WIDTH, FIRETRUCK_HEIGHT);
        this.setAccelerationRate(FIRETRUCK_ACCELERATION);
        this.setMaxSpeed(FIRETRUCK_MAX_SPEED);
        this.setRestitution(FIRETRUCK_RESTITUTION);
        float[] hoseVertices = { // Starts facing left
            0, 0,
            -(this.getWidth() * 1.5f), (this.getHeight() / 1.5f),
            -(this.getWidth() * 1.75f), (this.getHeight() / 2f),
            -(this.getWidth() * 1.85f), (this.getHeight() / 5f),
            -(this.getWidth() * 1.85f), -(this.getHeight() / 5f),
            -(this.getWidth() * 1.75f), -(this.getHeight() / 2f),
            -(this.getWidth() * 1.5f), -(this.getHeight() / 1.5f)
        }; 
        this.hoseRange = new Polygon(hoseVertices);
        
    }

    /**
     * Update the position and direction of the firetruck every frame.
     */
    public void update(Batch batch) {
        super.update(batch);
        drawVoxelImage(batch);
        this.hoseRange.setPosition(this.getCentreX(), this.getCentreY());
        this.hoseRange.setRotation(this.getRotation());
        if (isFocused) {
            // Look for key press input, then accelerate the firetruck in that direction
            if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
                super.accelerate(Direction.LEFT);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
                super.accelerate(Direction.RIGHT);
            }          
            if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
                super.accelerate(Direction.DOWN);
            } 
            if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
                super.accelerate(Direction.UP);
            }
        }
        if (redLightDelay > 0) redLightDelay -= 1;
    }

    /**
     * Draws the voxel representation of the firetruck. Incrementally builds the firetruck
     * from layers of images with each image slightly higher than the last
     */
    private void drawVoxelImage(Batch batch) {
        // Length of array containing image slices
        int slicesLength = this.firetruckSlices.size() - 1;
        float x = getX(), y = getY(), angle = this.getRotation();
        float width = this.getWidth(), height = this.getHeight();
        for (int i = 0; i < slicesLength; i++) {
            Texture texture = animateLights(i);
            batch.draw(new TextureRegion(texture), x, (y - slicesLength / 3) + i, width / 2, height / 2, width, height, 1, 1, angle, false);
        }
    }

    /**
     * Alternates between showing the red and blue light on the truck.
     * Returns the texture at the given index offset to the correct index.
     * 
     * @param index The index of the next texture to draw the sprite with.
     * @return      The next texture to draw the sprite with.
     */
    private Texture animateLights(int index) {
        if (index == 14) { // The index of the texture containing the first light colour
            Texture texture = this.redLightDelay > 50 ? this.firetruckSlices.get(index + 1) : this.firetruckSlices.get(index);
            if (this.redLightDelay <= 0) this.redLightDelay = 100;
            return texture;
        } else if (index > 14) { // Offset remaining in order to not repeat a texture
            return this.firetruckSlices.get(index + 1);
        }
        return this.firetruckSlices.get(index);
    }

    /**
     * Checks if a polygon is within the range of the firetrucks hose.
     * 
     * @param polygon  The polygon that needs to be checked.
     */
    public boolean isInHoseRange(Polygon polygon) {
        return Intersector.overlapConvexPolygons(polygon, this.hoseRange);
    }

    /**
     * Gets whether the firetruck is damaged.
     * 
     * @return Whether the firetruck is damaged.
     */
    public boolean isDamaged() {
        return this.getHealthBar().getCurrentAmount() < FIRETRUCK_HEALTH;
    }

    /**
     * Gets whether the firetruck is in focus.
     * 
     * @return Whether the firetruck is in focus (true) or not (false).
     */
    public boolean isFocused() {
        return this.isFocused;
    }

    /**
     * Sets the firetruck in focus if its ID matches the one to focus.
     * @param focus The ID of the firetruck to focus on.
     */
    public void setFocus(int focus) {
        if (focus == focusID) {
            this.isFocused = true;
        } else {
            this.isFocused = false;
        }
    }

    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the hose range indicator.
     * 
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
        renderer.polygon(this.hoseRange.getTransformedVertices());
    }

    /**
     * Dispose of all textures used by this class and its parents.
     */
    @Override
    public void dispose() {
        super.dispose();
        for (Texture texture : this.firetruckSlices) {
            texture.dispose();
        }
    }
}