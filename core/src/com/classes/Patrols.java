package com.classes;

import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.sprites.PatrolMovementSprite;

import java.util.ArrayList;

public class Patrols extends PatrolMovementSprite {

    ArrayList<Texture> textureSlices;
    private Circle detectionRange;
    private  boolean isDead;
    String name;

    public Patrols(ArrayList<Texture> textureSlices, Junction start, MapGraph mapGraph, String name){
        super(textureSlices.get(textureSlices.size() - 1), start, mapGraph, name);
        this.getHealthBar().setMaxResource(25);
        this.textureSlices = textureSlices;
        this.isDead = false;
        this.name = name;
        this.detectionRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth() * 3);
    }

    public void update(Batch batch) {
        checkDead();
        super.update(batch);
        updateDetectionRange();
        drawVoxelImage(batch);
    }

    private void checkDead() {
        if (this.getHealthBar().getCurrentAmount() == 0){
            this.isDead = true;
        }
    }

    private void updateDetectionRange() {
        this.detectionRange.setX(this.getX());
        this.detectionRange.setY(this.getY());
    }

    // Place holder until we get a sprite //
    private void drawsVoxelImage(Batch batch) {
        int slicesLength = textureSlices.size() - 1;
        float width = 100, height = 100;
        for (int i = 0; i < slicesLength; i++) {
            Texture texture = animateLights(i);
            batch.draw(new TextureRegion(texture), this.getX(), (this.getY() - slicesLength / 3) + i, width / 2, height / 2, width, height, 1, 1, this.getRotation(), true);
        }
    }

    private void drawVoxelImage(Batch batch) {
        // Length of array containing image slices
        int slicesLength = this.textureSlices.size() - 1;
        float x = getX(), y = getY(), angle = this.getRotation();
        float width = this.getWidth(), height = this.getHeight();
        for (int i = 0; i < slicesLength; i++) {
            Texture texture = animateLights(i);
            batch.draw(new TextureRegion(texture), x, (y - slicesLength / 3) + i, width / 2, height / 2, width, height, 1, 1, angle, true);
        }
    }

    // Place holder until we get a sprite //
    private Texture animateLights(int index) {
        if (index == 14) { // The index of the texture containing the first light colour
            Texture texture = this.getInternalTime() / 5 > 15 ? textureSlices.get(index + 1) : textureSlices.get(index);
            return texture;
        } else if (index > 14) { // Offset remaining in order to not repeat a texture
            return textureSlices.get(index + 1);
        }
        return this.textureSlices.get(index);
    }



    public boolean canShootProjectile() {
        if (this.getHealthBar().getCurrentAmount() > 0 && this.getInternalTime() < 120 && this.getInternalTime() % 30 == 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a polygon is within the range of the ETFortress.
     * Usually used to see if a firetruck is close enough to be attacked.
     *
     * @param polygon  The polygon that needs to be checked.
     * @return         Whether the given polygon is in the radius of the ETFortress
     */
    public boolean isInRadius(Polygon polygon) {
        float []vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(this.detectionRange.x, this.detectionRange.y);
        float squareRadius = this.detectionRange.radius * this.detectionRange.radius;
        for (int i = 0; i < vertices.length; i+=2){
            if (i == 0){
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                    return true;
            }
        }
        return polygon.contains(this.detectionRange.x, this.detectionRange.y);
    }

    public Circle getDetectionRange(){
        return this.detectionRange;
    }

    public boolean isDead(){
        return this.isDead;
    }

    public void removeDead(MapGraph mapGraph){
        mapGraph.removeDead(super.getThis());
    }

    public String getName(){
        return this.name;
    }

}





