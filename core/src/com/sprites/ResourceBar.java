package com.sprites;

// LibGDX imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Color;

// Class to add ResourceBars to sprites
public class ResourceBar {

    // Private values to be used in this class only
    private Batch batch;
    private ProgressBar bar;
    private int currentResourceAmount;
    private int maxResourceAmount;
    private int barWidth;
    private int barHeight;
    private float x;
    private float y;

    // Constructor for this class, gathers required information so that it can be drawn
    // Batch spriteBatch -  the batch that the sprite should be drawn on
    public ResourceBar(Batch barBatch, int spriteWidth, int spriteHeight ) {
        this.batch = barBatch;
        // Adjust bar to fit sprite dimensions
        this.barWidth = (int) (0.75 * spriteWidth);
        this.barHeight = (int) (0.05 * spriteHeight);
        this.currentResourceAmount = 1 * this.barWidth;
        this.maxResourceAmount = this.barWidth;
        this.x = 0;
        this.y = 0;
        this.bar = new ProgressBar(0, 100, 0.5f, false, getResourceBarStyle());
    }

    // Draw the health bar
    public void update() {
        drawResourceBar();
    }

    // Create the rectangles for the bar and fill them with a given colour
    private Skin createBarFill(String pixmapName, Color colorColour, int barWidth, int barHeight) {
        Skin barFill = new Skin();
        Pixmap pixMap= new Pixmap(barWidth, barHeight, Format.RGBA8888);
        pixMap.setColor(colorColour);
        pixMap.fill();
        barFill.add(pixmapName, new Texture(pixMap));
        return barFill;
    }

    // Update the colour of the bar and then return the new style
    private ProgressBarStyle getResourceBarStyle() {
        Color color = this.currentResourceAmount <= this.maxResourceAmount * 0.5 ? this.currentResourceAmount <= this.maxResourceAmount * 0.25 ? Color.RED : Color.ORANGE : Color.GREEN;
        Skin healthColour = createBarFill("healthPixmap", color, this.currentResourceAmount, this.barHeight);
        Skin background = createBarFill("backgroundPixmap", Color.GRAY, (this.maxResourceAmount - this.currentResourceAmount), this.barHeight);
        return new ProgressBarStyle(background.newDrawable("backgroundPixmap"), healthColour.newDrawable("healthPixmap"));
    }

    // Draw the bar onto the batch at the correct position
    private void drawResourceBar() {
        this.batch.begin();
        this.bar.setPosition(this.x, this.y);
        this.bar.setSize(this.barWidth, this.barHeight);
        this.bar.draw(batch, 1);
        this.batch.end();
    }

    // Set the position of the bar, takes into account the sprite's dimensions
    public void setPosition(float spriteXPos, float spriteYPos) {
        // Get sprite height and width by reversing previous calculations
        float spriteHeight = barHeight / 0.05f, spriteWidth = barWidth / 0.75f;
        this.x = spriteXPos + spriteWidth * 0.125f;
        this.y = spriteYPos + spriteHeight * 1.1f;
    }

    // Set the percentage the bar shows
    public void setResourcePercentage(int percent) {
        this.currentResourceAmount = (percent * this.maxResourceAmount) / 100;
        this.bar.setStyle(getResourceBarStyle());
    }

    // Update the bar by a percentage to be subtracted from
    public void subtractResourcePercentage(int percent) {
        this.currentResourceAmount = (percent * this.maxResourceAmount) / 100;
        this.bar.setStyle(getResourceBarStyle());
    }
}