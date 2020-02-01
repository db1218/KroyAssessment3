package com.CustomActors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundBox extends Actor {

    private Texture texture;

    public BackgroundBox(int width, int height, Color color) {
        createRectangleTexture(width, height, color);
    }

    public BackgroundBox(int width, int height, Color color, int radius) {
        createRoundRectangleTexture(width, height, color, radius);
    }

    /**
     * Rectangle
     * @param width
     * @param height
     * @param color
     */
    private void createRectangleTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Rounded Rectangle
     * @param width
     * @param height
     * @param color
     * @param radius
     */
    private void createRoundRectangleTexture(int width, int height, Color color, int radius) {
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
