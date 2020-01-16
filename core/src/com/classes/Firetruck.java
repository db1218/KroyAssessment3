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
import com.sprites.MovementSprite;
import com.classes.ResourceBar;

// Constants imports
import static com.config.Constants.Direction;
import static com.config.Constants.FIRETRUCK_HEIGHT;
import static com.config.Constants.FIRETRUCK_WIDTH;
import static com.config.Constants.SCREEN_CENTRE_X;
import static com.config.Constants.SCREEN_CENTRE_Y;

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
    private int focusID, animationTime, toggleDelay;
    private float hoseWidth, hoseHeight;
    private float[] firetruckProperties;
    private ArrayList<Texture> firetruckSlices, waterFrames;
    private Polygon hoseRange;
    private ResourceBar waterBar;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param waterFrames        The texture used to draw the water with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     * @param xPos           The x-coordinate for the firetruck.
     * @param yPos           The y-coordinate for the firetruck.
     */
    public Firetruck(ArrayList<Texture> textureSlices, ArrayList<Texture> frames, float[] properties, TiledMapTileLayer collisionLayer, int ID, float xPos, float yPos) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.focusID = ID;
        this.waterFrames = frames;
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
     * @param waterFrames        The texture used to draw the water with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     */
    public Firetruck(ArrayList<Texture> textureSlices, ArrayList<Texture> frames, float[] properties, TiledMapTileLayer collisionLayer, int ID) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.focusID = ID;
        this.waterFrames = frames;
        this.firetruckSlices = textureSlices;
        this.firetruckProperties = properties;
        this.create();
    }

    /**
     * Sets the health of the firetruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the firetruck.
     */
    private void create() {
        this.animationTime = 0;
        this.isSpraying = true;
        this.setSize(FIRETRUCK_WIDTH, FIRETRUCK_HEIGHT);
        this.getHealthBar().setMaxResource((int) this.firetruckProperties[0]);
        this.setAccelerationRate(this.firetruckProperties[1]);
        this.setDecelerationRate(this.firetruckProperties[1] * 0.6f);
        this.setMaxSpeed(this.firetruckProperties[2]);
        this.setRestitution(this.firetruckProperties[3]);
        this.createWaterHose();
        // Start the firetruck facing left
        this.rotate(-90);
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

            // Convert the two points into vectors
            
            Vector2 hoseVector = new Vector2((Gdx.input.getX()), (SCREEN_CENTRE_Y - Gdx.input.getY())); 
            Vector2 centreVector = new Vector2(SCREEN_CENTRE_X, SCREEN_CENTRE_Y); 

            // Work out the vector between them
            hoseVector = centreVector.sub(hoseVector);
            //hoseVector.rotate(180);

            // Scale the vector by the speed we want it to travel
            //hoseVector.scl(this.hoseWidth, this.hoseHeight);

            System.out.println("Mouse " + (this.getX() / 2 + this.getWidth() - Gdx.input.getX()) + " " + (this.getCentreY() + this.getHeight() - SCREEN_CENTRE_Y - Gdx.input.getY()));
            System.out.println("Firetruck " + this.getCentreX() + " " + this.getCentreY());
            System.out.println("Result " + hoseVector.x + " " + hoseVector.y + " " + hoseVector.angle());

            // Give the projectile the vector to travel at
            //this.rotate(hoseVector.angle());
            //this.setSpeed(hoseVector);

            // Change batch aplha to match bar to fade hose in and out
            batch.setColor(1.0f, 1.0f, 1.0f, this.waterBar.getFade() * 0.7f);
            batch.draw(new TextureRegion(this.waterTexture), this.getCentreX(),this.getCentreY(), 0,0,
                hoseVector.x, hoseVector.y, 1,1, hoseVector.angle(), false);
            // Return the batch to its original colours
            batch.setColor(1.0f, 1.0f, 1.0f, 1f);

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

        // Update the hose size position
        float scale = this.isSpraying && this.hoseRange.getScaleX() < this.firetruckProperties[4] ?
            0.05f : !this.isSpraying && this.hoseRange.getScaleX() > 0 ? -0.05f : 0;
        this.hoseRange.setScale(this.hoseRange.getScaleX() + scale, this.hoseRange.getScaleY() + scale);
        this.hoseRange.setPosition(this.getCentreX(), this.getCentreY());
        this.hoseRange.setRotation(this.getRotation());

        // Change batch aplha to match bar to fade hose in and out
        batch.setColor(1.0f, 1.0f, 1.0f, this.waterBar.getFade() * 0.9f);
        batch.draw(new TextureRegion(this.waterFrames.get(Math.round(this.animationTime / 10) % 3)), this.hoseRange.getX(), this.hoseRange.getY() - this.hoseHeight / 2,
            0, this.hoseHeight / 2, this.hoseWidth, this.hoseHeight, this.hoseRange.getScaleX(), this.hoseRange.getScaleY(), this.getRotation(), true);
        // Return the batch to its original colours
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);

        // Decrease timeouts, used for keeping track of time
        if (this.animationTime > 0) this.animationTime -= 1;
        if (this.toggleDelay > 0) this.toggleDelay -= 1;

        // If dead, move out of screen
        if (this.getHealthBar().getCurrentAmount() <= 0) this.setPosition(-1000, -1000);
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
            Texture texture = this.animationTime > 45 ? this.firetruckSlices.get(index + 1) : this.firetruckSlices.get(index);
            if (this.animationTime <= 0) this.animationTime = 90;
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
     * Gets whether the firetruck is in focus.
     * 
     * @return Whether the firetruck is in focus (true) or not (false).
     */
    public boolean isFocused() {
        return this.isFocused;
    }

    /**
     * Gets the firetruck's focus ID
     * 
     * @return The firetruck's focus ID
     */
    public int getFocusID() {
        return this.focusID;
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
        for (Texture texture : this.waterFrames) {
            texture.dispose();
        }
    }
}