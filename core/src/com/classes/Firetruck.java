package com.classes;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

// Custom class import
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.config.Constants;
import com.sprites.MovementSprite;

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

    private final Constants.TruckColours colour;
    // Private values to be used in this class only
    private Boolean isFocused, isSpraying;
    private int focusID, internalTime, toggleDelay;
    private float hoseWidth, hoseHeight;
    private float[] firetruckProperties;
    private ArrayList<Texture> firetruckSlices, waterFrames;
    private Polygon hoseRange;
    private ResourceBar waterBar;

    private boolean alive;

    private Firestation fireStation;

    private ShapeRenderer shapeRenderer;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param frames         The texture used to draw the water with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     */
    public Firetruck(ArrayList<Texture> textureSlices, ArrayList<Texture> frames, float[] properties, TiledMapTileLayer collisionLayer, TiledMapTileLayer carparkLayer, Firestation fireStation, Constants.TruckColours colour) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer, carparkLayer, fireStation);
        this.waterFrames = frames;
        this.firetruckSlices = textureSlices;
        this.firetruckProperties = properties;
        this.setPosition(fireStation.getSpawnLocation().x, fireStation.getSpawnLocation().y);
        this.fireStation = fireStation;
        this.create();
        this.shapeRenderer = new ShapeRenderer();
        this.colour = colour;
    }

    /**
     * Sets the health of the firetruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the firetruck.
     */
    private void create() {
        this.isSpraying = true;
        this.setSize(FIRETRUCK_WIDTH, FIRETRUCK_HEIGHT);
        this.getHealthBar().setMaxResource((int) this.firetruckProperties[0]);
        this.setAccelerationRate(this.firetruckProperties[1]);
        this.setDecelerationRate(this.firetruckProperties[1] * 0.6f);
        this.setMaxSpeed(this.firetruckProperties[2]);
        this.setRestitution(this.firetruckProperties[3]);
        this.createWaterHose();
        this.alive = true;

        // Start the firetruck facing left
        this.rotate(-90);
    }

    /**
     * Update the position and direction of the firetruck every frame.
     * @param batch  The batch to draw onto.
     * @param camera Used to get the centre of the screen.
     */
    public void update(Batch batch, Camera camera) {
        super.update(batch);
        drawVoxelImage(batch);
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

        // Deplete water if spraying, toggle off when depleted
        if (this.isSpraying && this.waterBar.getCurrentAmount() > 0) {
            this.waterBar.subtractResourceAmount(1);
        } else if (this.isSpraying) {
            this.toggleHose();
        }

        // Update the water bar position
        this.waterBar.setPosition(this.getX(), this.getCentreY());
        this.waterBar.update(batch);

        // Get the mouse input and get the angle from the truck to it. Get vector, normalise then get angle
        Vector2 hoseVector = new Vector2((this.getCentreX() - (camera.viewportWidth / 2) + Gdx.input.getX()), (this.getCentreY() + (camera.viewportHeight / 2) - Gdx.input.getY())); 
        Vector2 centreVector = new Vector2(this.getCentreX(),this.getCentreY()); 

        // Work out the vector between them
        hoseVector = hoseVector.sub(centreVector);
        hoseVector.nor();

        // Update the hose size and position. Angle it towards the mouse
        float scale = this.isSpraying && this.hoseRange.getScaleX() < this.firetruckProperties[4] ?
            0.05f : !this.isSpraying && this.hoseRange.getScaleX() > 0 ? -0.05f : 0;
        this.hoseRange.setScale(this.hoseRange.getScaleX() + scale, this.hoseRange.getScaleY() + scale);
        this.hoseRange.setPosition(this.getCentreX(), this.getCentreY());
        this.hoseRange.setRotation(hoseVector.angle());

        // Change batch aplha to match bar to fade hose in and out
        batch.setColor(1.0f, 1.0f, 1.0f, this.waterBar.getFade() * 0.9f);
        batch.draw(new TextureRegion(this.waterFrames.get(Math.round(this.getInternalTime() / 10) % 3)), this.hoseRange.getX(), this.hoseRange.getY() - this.hoseHeight / 2,
            0, this.hoseHeight / 2, this.hoseWidth, this.hoseHeight, this.hoseRange.getScaleX(), this.hoseRange.getScaleY(), hoseVector.angle(), true);

        // Return the batch to its original colours
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);

        // Decrease timeout, used for keeping track of time between toggle presses
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
            Texture texture = this.getInternalTime() / 5 > 15 ? this.firetruckSlices.get(index + 1) : this.firetruckSlices.get(index);
            return texture;
        } else if (index > 14) { // Offset remaining in order to not repeat a texture
            return this.firetruckSlices.get(index + 1);
        }
        return this.firetruckSlices.get(index);
    }

    public Image getFireTruckImage() {
        System.out.println(this.firetruckProperties[0]);
        Image rotated = new Image(new Texture(Gdx.files.internal("Firetruck" + colour.getColourLower() + "/Firetruck" + colour.getColourUpper() + " Full.png")));

        return rotated;
    }

    /**
     * Creates the polygon for the hose and the water bar to store the firetruck's
     * water level.
     */
    private void createWaterHose() {
        // Get the scale of the hose and create its shape
        float rangeScale = this.firetruckProperties[4];
        this.hoseWidth = this.getHeight() * 4.5f * rangeScale;
        this.hoseHeight =  this.getWidth() * 0.65f * rangeScale;
        float[] hoseVertices = { // Starts facing right
            0, 0,
            (hoseWidth * 0.5f),  (hoseHeight / 2),
            (hoseWidth * 0.9f),  (hoseHeight / 2),
            (hoseWidth),  (hoseHeight / 2.25f),
            (hoseWidth), -(hoseHeight / 2.25f),
            (hoseWidth * 0.9f), -(hoseHeight / 2),
            (hoseWidth * 0.5f), -(hoseHeight / 2)
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
        if (this.getInternalTime() % 10 != 0) return false;
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
     * Gets whether the firetruck is spraying water.
     * 
     * @return Whether the firetruck is spraying water.
     */
    public boolean isSpraying() {
        return this.isSpraying;
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
            this.toggleDelay = 20;
            this.isSpraying = !this.isSpraying && this.waterBar.getCurrentAmount() > 0;
            this.waterBar.setFade(false, !this.isSpraying);
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

    public void destroyed() {
        this.alive = false;
    }

    public boolean isAlive() {
        return this.alive;
    }

    void resetRotation() {
        super.setRotation(0);
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
        for (Texture texture : this.waterFrames) {
            texture.dispose();
        }
    }
}