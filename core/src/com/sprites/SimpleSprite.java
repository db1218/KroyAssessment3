package com.sprites;

// LibGDX imports
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Class import
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.classes.ResourceBar;

/**
 * Simplify and add functionality to the sprite object provided by libGDX.
 *
 * @author Archie
 * @since 17/12/2019
 */
public class SimpleSprite extends Sprite {

    // Private values to be used in this class only
    private Texture texture;
    private float width, height, internalTime;
    private ResourceBar healthBar;
    private Polygon hitBox;

    /**
     * Constructor that creates a sprite at a given position using a given texture..
     * Creates a sprite at (0,0) using a given texture.
     *
     * @param spriteTexture  The texture the sprite should use.
     */
    public SimpleSprite(Texture spriteTexture) {
        super(spriteTexture);
        this.texture = spriteTexture;
        this.create();
    }

    /**
     * Creates a healthbar and hitbox for the sprite.
     */
    private void create() {
        // Use the longest side of the sprite as the bar width
        this.healthBar = new ResourceBar(Math.max(this.getWidth(), this.getHeight()), Math.min(this.getWidth(), this.getHeight()));
        this.hitBox = new Polygon(new float[]{0,0,this.getWidth(),0,this.getWidth(),this.getHeight(),0,this.getHeight()});
        // Rotate 90 to be same rotation as textures
        this.rotate(-90);
        // Start internal time at 150, used for animations/timeouts
        this.internalTime = 150;
    }

    /**
     * Update the sprite position, hitbox and health bar.
     * Must be called every frame in order to draw the sprite.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        // Keep the healthbar and hitbox located on the sprite
        this.healthBar.setPosition(this.getX(), this.getY());
        this.hitBox.setPosition(this.getX(), this.getY());
        this.healthBar.update(batch);
        // Draw the sprite and update the healthbar
        batch.draw(new TextureRegion(this.texture), this.getX(), this.getY(), this.getWidth() / 2, this.getHeight() / 2,
                this.getWidth(), this.getHeight(), 1, 1, this.getRotation(), true);
        // Decrease internal time
        this.decreaseInternalTime();
    }

    public void decreaseInternalTime() {
        if (this.internalTime > 0) {
            this.internalTime -= 1;
        } else if (this.getInternalTime() <= 0) {
            this.internalTime = 150;
           // System.out.println("reset internal time");
        }
    }

    /**
     * Enables drawing of the hitbox to it can be seen. 
     * @param renderer   The shape renderer to draw onto.
     */
    public void drawDebug(ShapeRenderer renderer) {
        renderer.polygon(this.hitBox.getTransformedVertices());
    }

    /**
     * Enables drawing of the hitbox to it can be seen.
     * @param destroyedTexture The flooded texture to replace the sprite with
     */
    public void removeSprite(Texture destroyedTexture) {
        this.texture = destroyedTexture;
    }

    /**
     * Overrides Sprite class method to keep seperate values for width and height.
     * Also updates the sprites width and height and creates a new hitbox and health bar.
     * @param width    The width the sprite should be set to.
     * @param height   The height the sprite should be set to.
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
        this.create();
        this.hitBox.setOrigin(width/2, height/2);
    }

    /**
     * Overrides Sprite class method to rotate the sprite and hitbox at once.
     * @param degrees   The degree to rotate to.
     */
    @Override
    public void rotate(float degrees) {
        super.rotate(degrees);
        this.hitBox.rotate(degrees);
    }

    @Override
    public void setRotation(float degrees) {
        super.setRotation(degrees);
        this.hitBox.setRotation(degrees);
    }

    /**
     * Get the health bar of the sprite.
     * @return The health bar of the sprite.
     */
    public ResourceBar getHealthBar() {
        return this.healthBar;
    }

    /**
     * Get the internal time of the sprite.
     * @return The internal time of the sprite.
     */
    public float getInternalTime() {
        return this.internalTime;
    }

    /**
     * Get the hit box of the sprite.
     * @return The hit box of the sprite.
     */
    public Polygon getHitBox() {
        return this.hitBox;
    }

    /**
     * Get the current width of the sprite.
     * @return The width of the sprite.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Get the current height of the sprite.
     * @return The height of the sprite.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Get the value of the centre x-coordinate of the sprite.
     * @return The cente X-coordinate of the sprite.
     */
    public float getCentreX() {
        // Add half sprite width to get centre
        return this.getX() + this.getWidth() / 2;
    }

    /**
     * Get the value of the centre y-coordinate of the sprite.
     * @return The centre y-coordinate of the sprite.
     */
    public float getCentreY() {
        //Add half sprite height to get centre
        return this.getY() + this.getHeight() / 2;
    }

    /**
     * Dispose of assets used by the class.
     */
    public void dispose() {
        texture.dispose();
    }

}