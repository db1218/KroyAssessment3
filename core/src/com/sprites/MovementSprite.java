package com.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

// Constants import
import static com.config.Constants.MAP_HEIGHT;
import static com.config.Constants.MAP_WIDTH;
import static com.config.Constants.SPRITE_HEIGHT;
import static com.config.Constants.SPRITE_WIDTH;
import static com.config.Constants.COLLISION_TILE;

public class MovementSprite extends SimpleSprite {

    // For animations later
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    Direction direction;
    private TiledMapTileLayer collisionLayer;

    public MovementSprite(Batch spriteBatch, Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture);
        this.collisionLayer = collisionLayer;
    }

    public MovementSprite(Batch spriteBatch, Texture spriteTexture, float xPos, float yPos, TiledMapTileLayer collisionLayer) {
        super(spriteBatch, spriteTexture, xPos, yPos);
        this.collisionLayer = collisionLayer;
    }

    // Update the sprites position and direction
    public void update() {
        // look for key press input
        if (!collidesLeft() && Gdx.input.isKeyPressed(Keys.LEFT))
            setX(getX() - 200 * Gdx.graphics.getDeltaTime());
            direction = Direction.LEFT;

		if (!collidesRight() && Gdx.input.isKeyPressed(Keys.RIGHT))
            setX(getX() + 200 * Gdx.graphics.getDeltaTime());
            direction = Direction.RIGHT;
            
        if (!collidesBottom() && Gdx.input.isKeyPressed(Keys.DOWN))
            setY(getY() - 200 * Gdx.graphics.getDeltaTime());
            direction = Direction.DOWN;

		if (!collidesTop() && Gdx.input.isKeyPressed(Keys.UP))
            setY(getY() + 200 * Gdx.graphics.getDeltaTime());
            direction = Direction.UP;
        
        // Check boundaries then draw
        checkBoundaries();
        this.drawSprite();
    }

    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(COLLISION_TILE);
	}

	public boolean collidesRight() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesLeft() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesTop() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

	public boolean collidesBottom() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}
    
    // Make sure the sprite stays within the map bounds
    private void checkBoundaries() {
        if (getY() < 0)
            setY(0);
        if (getY() > MAP_HEIGHT - SPRITE_HEIGHT)
            setY(MAP_HEIGHT - SPRITE_HEIGHT);
        if (getX() < 0)
            setX(0);
        if (getX() > MAP_WIDTH - SPRITE_WIDTH)
            setX(MAP_WIDTH - SPRITE_WIDTH);
    }
}