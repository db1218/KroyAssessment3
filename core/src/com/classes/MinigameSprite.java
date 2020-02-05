package com.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.config.Constants;
import com.sprites.SimpleSprite;

public class MinigameSprite extends Sprite {

    private Rectangle hitBox;
    private Texture texture;

    public MinigameSprite(Texture texture) {
        super(texture);
        this.setPosition(42*Constants.TILE_DIMS, 42*Constants.TILE_DIMS);
        this.texture = texture;
        this.hitBox = new Rectangle(super.getX(), super.getY(), 1.5f * Constants.TILE_DIMS, 1.5f * Constants.TILE_DIMS);
    }

    public void update(Batch batch) {
        batch.draw(texture, super.getX(), super.getY(), 1.5f * Constants.TILE_DIMS, 1.5f * Constants.TILE_DIMS);
    }

    private void openMinigame() {

    }

}
