package com.sprites;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

// Class import
import com.sprites.ResourceBar;

/**
 * Simplify and add functionality to the sprite object provided by libGDX.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class SimpleSprite extends Sprite {

    // Private values to be used in this class only
    private Batch batch;
    private Texture texture;
    private float width = 0, height = 0;

    // Allows the health bar to be changed by subclasses
    public ResourceBar healthBar;
    public Polygon hitBox;

    /**
     * Constructor for this class. Gathers the required information so the
     * sprite can be drawn.
     * 
     * @param spriteBatch   The batch that the sprite should be drawn on.
     * @param spriteTexture The texture the sprite should use.
     */
    public SimpleSprite(Batch spriteBatch, Texture spriteTexture) {
        super(spriteTexture);
        batch = spriteBatch;
        texture = spriteTexture;
        healthBar = new ResourceBar(batch, this.getWidth(), this.getHeight());
        this.hitBox = new Polygon(new float[]{0,0,this.getWidth(),0,this.getWidth(),this.getHeight(),0,this.getHeight()});
        this.hitBox.setPosition(0, 0);
        this.setPosition(0, 0);
    }

    /**
     * Overload constructor for this class, taking a position to draw the sprite at.
     * 
     * @param spriteBatch    The batch that the sprite should be drawn on.
     * @param spriteTexture  The texture the sprite should use.
     * @param xPos           The x-coordinate for the sprite to be drawn.
     * @param yPos           The y-coordinate for the sprite to be drawn.
     */
    public SimpleSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos) {
        super(spriteTexture);
        batch = spriteBatch;
        texture = spriteTexture;
        healthBar = new ResourceBar(batch, this.getWidth(), this.getHeight());
        this.hitBox = new Polygon(new float[]{0,0,this.getWidth(),0,this.getWidth(),this.getHeight(),0,this.getHeight()});
        this.hitBox.setPosition(xPos, yPos);
        this.setPosition(xPos, yPos);
    }

    /**
     * Update the sprite position, hitbox and health bar.
     */
    public void update() {
        // Keep the healthbar and hitbox located on the sprite
        healthBar.setPosition(this.getX(), this.getY());
        healthBar.update();
        this.hitBox.setPosition(this.getX(), this.getY());
        // Draw the sprite
        batch.begin();
        batch.draw(texture, getX(), getY(), this.getWidth(), this.getHeight());
        batch.end();
    }

    /**
     * Enables drawing of the hitbox to it can be seen. 
     * @param renderer   The shape renderer to draw onto
     */
    public void drawDebug(ShapeRenderer renderer) {
        renderer.begin(ShapeType.Line);
        renderer.polygon(this.hitBox.getTransformedVertices());
        renderer.end();
    }

    /**
     * Overrides Sprite class method to keep seperate values for width and height.
     * Updates the sprites width and height.
     * @param width    The width the sprite should be set to.
     * @param height   The height the sprite should be set to.
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.hitBox = new Polygon(new float[]{0,0,width,0,width,height,0,height});
        this.hitBox.setOrigin(width/2, height/2);
        this.width = width;
        this.height = height;
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