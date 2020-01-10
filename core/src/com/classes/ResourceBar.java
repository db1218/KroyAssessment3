package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Color;

/**
 * Resource bars used by sprites to indicate properties of sprites.
 * These are graphical and displayed to the user.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class ResourceBar {

    // Private values to be used in this class only
    private ProgressBar bar;
    private int currentResourceAmount, maxResourceAmount, barWidth, barHeight, isVisibileTime;

    /**
     * Constructor for this class, gathers required information so that it can
     * be drawn.
     * 
     * @param spriteWidth  The width of the sprite.
     * @param spriteHeight The height of the sprite.
     */
    public ResourceBar(float spriteWidth, float spriteHeight ) {
        // Adjust bar to fit sprite dimensions
        this.barWidth = (int) (0.7 * spriteWidth);
        this.barHeight = (int) (0.3 * spriteHeight);
        this.create();
    }

    /**
     * Creates a new progress bar and sets the inital values for all properties needed.
     */
    private void create() {
        this.isVisibileTime = 0;
        this.maxResourceAmount = 100;
        this.currentResourceAmount = 100;
        this.bar = new ProgressBar(0, 100, 0.5f, false, getResourceBarStyle());
        this.bar.setSize(this.barWidth, this.barHeight);
    }

    /**
     * Draw the resource bar. Needs to be called every frame.
     */
    public void update(Batch batch) {
        if (this.isVisibileTime > 0) {
            if (this.currentResourceAmount >= this.maxResourceAmount) this.isVisibileTime -= 1;
            this.bar.draw(batch, 1);
        }
    }

    /**
     * Create the rectangles for the resource bar, and fill them with the given
     * colour.
     * 
     * @param pixmapName The name of the pixmap to draw.
     * @param colour     The colour to fill the resource bar.
     * @param barWidth   The width of the resource bar.
     * @param barHeight  The height of the resource bar.
     * @return           The filled bar.
     */
    private Skin createBarFill(String pixmapName, Color colour, int barWidth, int barHeight) {
        Skin barFill = new Skin();
        Pixmap pixMap= new Pixmap(barWidth, barHeight, Format.RGBA8888);
        pixMap.setColor(colour);
        pixMap.fill();
        barFill.add(pixmapName, new Texture(pixMap));
        return barFill;
    }

    /**
     * Update the colour of the resource bar and then return the new style.
     * 
     * @return Updated resource bar style.
     */
    private ProgressBarStyle getResourceBarStyle() {
        // Colour to use for the bar, depending on health percentage
        Color color = this.currentResourceAmount <= this.maxResourceAmount * 0.5 ?
            this.currentResourceAmount <= this.maxResourceAmount * 0.25 ?
                Color.RED : Color.ORANGE : Color.GREEN;
        
        // Size of entire bar is shared between the reource bar and the empty bar
        int scaledResource = (int) (((float) this.currentResourceAmount / 100) * this.barWidth);
        int scaledEmpty = (int) (((float)(this.maxResourceAmount - this.currentResourceAmount) / 100) * this.barWidth);

        // Create the bars and return a combined bar
        Skin resource = createBarFill("resource", color, scaledResource, this.barHeight);
        Skin background = createBarFill("background", Color.GRAY, scaledEmpty, this.barHeight);
        return new ProgressBarStyle(background.newDrawable("background"), resource.newDrawable("resource"));
    }

    /** 
     * Get the current resource amount.
     * @return The current resource amount;
     */
    public float getCurrentAmount() {
        return this.currentResourceAmount;
    }

    /** 
     * Set the position of the bar, takes into account the sprite's dimensions
     * @param spriteXPos The x-coordinate of the sprite.
     * @param spriteYPos The y-coordinate of the sprite.
     */
    public void setPosition(float spriteXPos, float spriteYPos) {
        // Get sprite height and width by reversing previous calculations
        float longestSide = Math.max(barHeight / 0.3f, barWidth / 0.7f);
        float x = spriteXPos + (longestSide / 2) - (barWidth / 2);
        float y = spriteYPos + longestSide;
        this.bar.setPosition(x, y);
    }

    /**
     * Set the maximum limit for the resource bar.
     * 
     * @param maxAmount The maximum limit for the resource bar.
     */
    public void setMaxResource(int maxAmount) {
        this.currentResourceAmount = (this.currentResourceAmount / this.maxResourceAmount) * maxAmount;
        this.maxResourceAmount = maxAmount;
        this.bar.setStyle(getResourceBarStyle());
    }

    /**
     * The percentage for the bar to show.
     * 
     * @param percent The percentage the bar shows.
     */
    public void setResourcePercentage(int percent) {
        this.currentResourceAmount = (percent * this.maxResourceAmount) / 100;
        this.bar.setStyle(getResourceBarStyle());
    }

    /**
     * Add a value to the resource bar.
     * 
     * @param amount The value to add to the resource bar.
     */
    public void addResourceAmount(int amount) {
        if (amount < 0) {
            subtractResourceAmount(amount);
        } else {
            if ((this.currentResourceAmount + amount) > this.maxResourceAmount) {
                this.currentResourceAmount = this.maxResourceAmount;
            } else {
                this.currentResourceAmount += amount;
            }
        }
        this.bar.setStyle(getResourceBarStyle());
        // Bar values changed so show user
        this.isVisibileTime = 200;
    }

    /**
     * Subtract a value from the resource bar.
     * 
     * @param amount The value to subtract from the resource bar.
     */
    public void subtractResourceAmount(int amount) {
        if (amount < 0) {
            addResourceAmount(amount);
        } else {
            if ((this.currentResourceAmount - amount) < 0) {
                this.currentResourceAmount = 0;
            } else {
                this.currentResourceAmount -= amount;
            }
        }
        this.bar.setStyle(getResourceBarStyle());
        // Bar values changed so show user
        this.isVisibileTime = 200;
    }
}