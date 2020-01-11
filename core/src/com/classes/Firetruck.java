package com.classes;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.Input.Keys;

// Custom class import
import com.sprites.MovementSprite;
import com.classes.ResourceBar;

// Constants imports
import static com.config.Constants.Direction;
import static com.config.Constants.FIRETRUCK_HEIGHT;
import static com.config.Constants.FIRETRUCK_WIDTH;

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
    private Boolean isFocused, isSpraying;
    private int focusID, redLightDelay, toggleDelay;
    private float[] firetruckProperties;
    private ArrayList<Texture> firetruckSlices;
    private Polygon hoseRange;
    private ResourceBar waterBar;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     * @param xPos           The x-coordinate for the firetruck.
     * @param yPos           The y-coordinate for the firetruck.
     */
    public Firetruck(ArrayList<Texture> textureSlices, float[] properties, TiledMapTileLayer collisionLayer, int ID, float xPos, float yPos) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.focusID = ID;
        this.firetruckSlices = textureSlices;
        this.firetruckProperties = properties;
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
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     */
    public Firetruck(ArrayList<Texture> textureSlices, float[] properties, TiledMapTileLayer collisionLayer, int ID) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.focusID = ID;
        this.firetruckSlices = textureSlices;
        this.firetruckProperties = properties;
        this.create();
    }

    /**
     * Sets the health of the firetruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the firetruck.
     */
    private void create() {
        this.redLightDelay = 0;
        this.isSpraying = true;
        this.setSize(FIRETRUCK_WIDTH, FIRETRUCK_HEIGHT);
        this.getHealthBar().setMaxResource((int) this.firetruckProperties[0]);
        this.setAccelerationRate(this.firetruckProperties[1]);
        this.setDecelerationRate(this.firetruckProperties[1] * 0.6f);
        this.setMaxSpeed(this.firetruckProperties[2]);
        this.setRestitution(this.firetruckProperties[3]);
        this.createWaterHose();
    }

    /**
     * Update the position and direction of the firetruck every frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        drawVoxelImage(batch);
        if (this.isFocused) {
            // Look for key press input, then accelerate the firetruck in that direction
            if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
                super.applyAcceleration(Direction.LEFT);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
                super.applyAcceleration(Direction.RIGHT);
            }          
            if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
                super.applyAcceleration(Direction.DOWN);
            } 
            if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
                super.applyAcceleration(Direction.UP);
            }
        }
        
        // If spraying
        if (this.isSpraying) {
            // Deplete water if spraying
            this.waterBar.subtractResourceAmount(1);

            // Position the hose on the firetruck
            this.hoseRange.setPosition(this.getCentreX(), this.getCentreY());
            this.hoseRange.setRotation(this.getRotation());
        } else {
            this.waterBar.setFade(false, true);
        }
        this.waterBar.setPosition(this.getX(), this.getCentreY());
        this.waterBar.update(batch);

        // Decrease timeouts, used for keeping track of time
        if (this.redLightDelay > 0) this.redLightDelay -= 1;
        if (this.toggleDelay > 0) this.toggleDelay -= 1;
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
            batch.draw(new TextureRegion(texture), x, (y - slicesLength / 3) + i, width / 2, height / 2, width, height, 1, 1, angle, true);
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
     * Creates the polygon for the hose and the water bar to store the firetruck's
     * water level.
     */
    private void createWaterHose() {
        // Get the scale of the hose and create its shape
        float rangeScale = this.firetruckProperties[4];
        float[] hoseVertices = { // Starts facing right
            0, 0,
            (this.getWidth() * 1.50f * rangeScale),  (this.getHeight() / 1.5f * rangeScale),
            (this.getWidth() * 1.75f * rangeScale),  (this.getHeight() / 2.0f * rangeScale),
            (this.getWidth() * 1.85f * rangeScale),  (this.getHeight() / 5.0f * rangeScale),
            (this.getWidth() * 1.85f * rangeScale), -(this.getHeight() / 5.0f * rangeScale),
            (this.getWidth() * 1.75f * rangeScale), -(this.getHeight() / 2.0f * rangeScale),
            (this.getWidth() * 1.50f * rangeScale), -(this.getHeight() / 1.5f * rangeScale)
        }; 
        this.hoseRange = new Polygon(hoseVertices);
        // Create the water bar
        this.waterBar = new ResourceBar(Math.max(this.getWidth(), this.getHeight()), Math.min(this.getWidth(), this.getHeight()));
        this.waterBar.setColourRange(new Color[] { Color.BLUE });
        this.waterBar.setMaxResource((int) this.firetruckProperties[5]);
        // Start with the hose off
        this.toggleHose();
    } 

    /**
     * Checks if a polygon is within the range of the firetrucks hose.
     * @param polygon  The polygon that needs to be checked.
     * 
     * @return Whether the polygon is in the hose's range
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
        return this.getHealthBar().getCurrentAmount() < this.firetruckProperties[0];
    }

    /**
     * Gets whether the firetruck has used any water.
     * 
     * @return Whether the firetruck has used any water.
     */
    public boolean isLowOnWater() {
        return this.waterBar.getCurrentAmount() < this.firetruckProperties[5];
    }

    /**
     * Toggles the fireturck's hose to spray if off and stop if on.
     */
    public void toggleHose() {
        if (this.toggleDelay <= 0) {
            this.toggleDelay = 10;
            // Scale the hose to hide it
            float scale = !this.isSpraying ? this.firetruckProperties[4] : 0;
            this.hoseRange.setScale(scale,scale);
            this.isSpraying = !this.isSpraying;
        }
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
     * Gets the firetruck's water bar so it can be manipulated.
     * 
     * @return The firetruck's water bar.
     */
    public ResourceBar getWaterBar() {
        return this.waterBar;
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