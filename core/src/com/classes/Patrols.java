package com.classes;

import com.PathFinding.Junction;
import com.PathFinding.MapGraph;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sprites.PatrolMovementSprite;

import java.util.ArrayList;


public class Patrols extends PatrolMovementSprite {

    ArrayList<Texture> textureSlices;

    public Patrols(ArrayList<Texture> textureSlices, Junction start, MapGraph mapGraph){
        super(textureSlices, start, mapGraph);
        this.getHealthBar().setMaxResource((int) 10);
        this.textureSlices = textureSlices;
    }

    public void update(Batch batch) {
        super.update(batch);
        drawVoxelImage(batch);
    }

    // Place holder until we get a sprite //
    private void drawVoxelImage(Batch batch) {
        int slicesLength = textureSlices.size() - 1;
        float width = 100, height = 50;
        for (int i = 0; i < slicesLength; i++) {
            Texture texture = animateLights(i);
            batch.draw(new TextureRegion(texture), this.getX(), (this.getY() - slicesLength / 3) + i, width / 2, height / 2, width, height, 1, 1, this.getRotation(), true);
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
}





