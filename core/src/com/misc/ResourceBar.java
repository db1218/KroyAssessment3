package com.misc;

// LibGDX imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Color;

// Constants imports
import static com.misc.Constants.BAR_FADE_DURATION;

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
    private int currentResourceAmount;
    private int maxResourceAmount;
    private final int barWidth;
    private final int barHeight;
    private boolean fadeIn, fadeOut, beginFadeOut;
    private Color[] colourRange; 

    /**
     * Constructor for this class, gathers required information so that it can
     * be drawn.
     * 
     * @param spriteWidth  The width of the sprite.
     * @param spriteHeight The height of the sprite.
     */
    public ResourceBar(float spriteWidth, float spriteHeight) {
        // Adjust bar to fit sprite dimensions
        this.barWidth = (int) (0.7 * spriteWidth);
        this.barHeight = (int) (0.3 * spriteHeight);
        this.create();
    }

    /**
     * Creates a new progress bar and sets the inital values for all properties needed.
     */
    private void create() {
        this.maxResourceAmount = 100;
        this.currentResourceAmount = 100;
        this.colourRange = new Color[] { Color.RED, Color.ORANGE, Color.GREEN };
        this.bar = new ProgressBar(0, 100, 0.5f, false, getResourceBarStyle());
        this.bar.setSize(this.barWidth, this.barHeight);
        this.bar.getColor().a = 0;
        this.setFade(false, true);
    }

    /**
     * Draw the resource bar. Needs to be called every frame.
     * 
     * @param batch The batch to draw the resource bar onto.
     */
    public void update(Batch batch) {
        // Fade the bar in or out
        if (!this.beginFadeOut) {
            this.bar.getColor().a += this.fadeIn  && this.bar.getColor().a < BAR_FADE_DURATION ? 0.05 : 0;
            if (this.bar.getColor().a >= BAR_FADE_DURATION) this.beginFadeOut = true;
        } else {
            this.bar.getColor().a -= this.fadeOut && this.bar.getColor().a > 0 ? 0.05 : 0;
        }

        if (this.currentResourceAmount > 0) this.bar.draw(batch, 1);
        // MUST return batch to correct alpha value
        // otherwise it fades all layers out
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);
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
        // Values changed, show bar then fade out if at maximum
        this.setFade(true, this.currentResourceAmount >= this.maxResourceAmount);

        // Colour to use for the bar, depending on health percentage
        Color color = this.currentResourceAmount <= this.maxResourceAmount * 0.5 ?
            this.currentResourceAmount <= this.maxResourceAmount * 0.25 ?
                this.colourRange[0] : this.colourRange[1] : this.colourRange[2];
        
        // Size of entire bar is shared between the resource bar and the empty bar
        int scaledResource = (int) (((float) this.currentResourceAmount / this.maxResourceAmount) * this.barWidth);
        int scaledEmpty = (int) (((float)(this.maxResourceAmount - this.currentResourceAmount) / this.maxResourceAmount) * this.barWidth);

        // Create the bars and return a combined bar
        Skin resource = createBarFill("resource", color, scaledResource, this.barHeight);
        Skin background = createBarFill("background", Color.GRAY, scaledEmpty, this.barHeight);
        return new ProgressBarStyle(background.newDrawable("background"), resource.newDrawable("resource"));
    }

    /** 
     * Set the position of the bar, takes into account the sprite's dimensions
     * @param spriteXPos The x-coordinate of the sprite.
     * @param spriteYPos The y-coordinate of the sprite.
     */
    public void setPosition(float spriteXPos, float spriteYPos) {
        // Get sprite height and width by reversing previous calculations
        float longestSide = Math.max(barHeight / 0.3f, barWidth / 0.7f);
        float x = spriteXPos + (longestSide / 2) - (barWidth / 2f);
        float y = spriteYPos + longestSide;
        this.bar.setPosition(x, y);
    }

    /** 
     * Fade the bar either in or out using it's alpha value.
     * 
     * @param shouldFadeIn      Whether to fade in (true) or out (false)
     * @param shouldFadeOut     Whether to fade out (true) or out (false)
     */
    public void setFade(boolean shouldFadeIn, boolean shouldFadeOut) {
        this.fadeIn = shouldFadeIn;
        this.fadeOut = shouldFadeOut;
        this.beginFadeOut = !shouldFadeIn;
    }

    /** 
     * Get the bar's alpha value, manipulated to remain in the range 0-1.
     * @return The alpha value of the bar
     */
    public float getFade() {
        return (this.bar.getColor().a / 10) * BAR_FADE_DURATION;
    }

    /** 
     * Set the colour range of the bar. The bar is split into 3 sections
     * so 3 colours are required.
     * @param colours The colours the sprite will use;
     */
    public void setColourRange(Color[] colours) {
        if (colours.length >= 3) {
            this.colourRange = colours;
        } else {
            this.colourRange = new Color[] { colours[0], colours[0], colours[0] };
        }
        this.bar.setStyle(getResourceBarStyle());
    }

    /** 
     * Get the current resource amount.
     * @return The current resource amount;
     */
    public float getCurrentAmount() {
        return this.currentResourceAmount;
    }

    /** 
     * Get the max resource amount.
     * @return The max resource amount;
     */
    public float getMaxAmount() {
        return this.maxResourceAmount;
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
                this.bar.setStyle(getResourceBarStyle());
            }
        }
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
                this.bar.setStyle(getResourceBarStyle());
            }
        }
    }

    /*
     *  =======================================================================
     *                          Added for Assessment 3
     *  =======================================================================
     */
    /**
     * Reset the resource amount back to its
     * original full capacity, this is used for
     * replenishing the fire truck's water after
     * the tutorial has finished
     */
    public void resetResourceAmount() {
        this.currentResourceAmount = this.maxResourceAmount;
    }

}