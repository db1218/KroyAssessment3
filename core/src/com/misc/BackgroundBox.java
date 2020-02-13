package com.misc;

/* =================================================================
   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Custom actor to allow for a shape to be
 * rendered on the stage. Used as backgrounds
 * to actors in the car park menu.
 */
public class BackgroundBox extends Actor {

    // the texture that the pixmap makes
    private final Texture texture;

    /**
     * Constructor to create a rectangle
     *
     * @param width     of rectangle
     * @param height    of rectangle
     * @param color     of the box
     */
    public BackgroundBox(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Constructor to create a rounded rectangle
     *
     * @param width     of rectangle
     * @param height    of rectangle
     * @param color     of the box
     * @param radius    of the corners
     */
    public BackgroundBox(int width, int height, Color color, int radius) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight()-2*radius);
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2*radius, pixmap.getHeight());
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(radius, pixmap.getHeight()-radius, radius);
        pixmap.fillCircle(pixmap.getWidth()-radius, radius, radius);
        pixmap.fillCircle(pixmap.getWidth()-radius, pixmap.getHeight()-radius, radius);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

}
