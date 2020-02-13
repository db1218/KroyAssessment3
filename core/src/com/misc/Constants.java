package com.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Game constants for use by Kroy.
 */
public final class Constants {

     // Simple enums
     public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public enum Outcome {
        WON,
        LOST
    }

    // ==============================================================
    //					Added for assessment 3
    // ==============================================================

    // Type enums for entities in the game
    public enum FortressType {
        CLIFFORD(7, 2000, 1, 400),
        MINSTER(15, 3000,1, 700),
        RAIL(10, 100000, 1, 900),
        CASTLE1(13, 750, 2, 600),
        CASTLE2(8, 1500, 2, 500);

        private final int damage;
        private final int health;
        private final int healing;
        private final int range;

        FortressType(int damage, int health, int healing, int range) {
            this.damage = damage;
            this.health = health;
            this.healing = healing;
            this.range = range;
        }

        public int getDamage() {
            return this.damage;
        }

        public int getHealth() {
            return this.health;
        }

        public int getHealing() {
            return this.healing;
        }

        public float getRange() {
            return this.range;
        }
    }

    public enum TruckType {
        RED ("Red", new float[]{
                170,  // HEALTH
                10f,  // ACCELERATION
                600f, // MAX_SPEED
                0.8f, // RESTITUTION
                1.2f, // RANGE
                600,  // WATER MAX
                0,    // PRICE
                1.9f,    // damage
        }),
        BLUE ("Blue", new float[]{
                220,   // HEALTH
                15f,   // ACCELERATION
                400f,  // MAX_SPEED
                0.6f,  // RESTITUTION
                1.2f, // RANGE
                600,   // WATER MAX
                300,   // PRICE
                1.9f,     // damage
        }),
        YELLOW ("Yellow", new float[]{
                270,  // HEALTH
                18f,  // ACCELERATION
                500f, // MAX_SPEED
                1.6f, // RESTITUTION
                1.25f, // RANGE
                600,  // WATER MAX
                900,  // PRICE
                1.9f,    // damage
        }),
        GREEN("Green", new float[]{
                270f, // HEALTH
                15f,  // ACCELERATION
                500f, // MAX_SPEED
                1.6f, // RESTITUTION
                1.2f, // RANGE
                750,  // WATER MAX
                1750,  // PRICE
                2.2f,    // damage
            }
        );

        private final String colourString;
        private final float[] properties;

        TruckType(String colourString, float[] properties) {
            this.colourString = colourString;
            this.properties = properties;
        }

        public String getColourString(){
             return this.colourString;
        }
        public float[] getProperties() {
            return this.properties;
        }
    }

    public enum AlienType {

        green(10, 0.5, new Texture(Gdx.files.internal("Minigame/aliensquare.png")), 2000),
        red(30, 0.3, new Texture(Gdx.files.internal("Minigame/redalien.png")), 1000),
        blue(50,0.2, new Texture(Gdx.files.internal("Minigame/bluealien.png")), 750);

        private int score;
        private double chance;
        private long aliveTime;

        private Texture texture;

        AlienType(int score, double chance, Texture texture, long aliveTime) {
            this.score = score;
            this.chance = chance;
            this.aliveTime = aliveTime;
            this.texture = texture;
        }

        public int getScore(){return this.score;}

        public double getChance() {return this.chance; }

        public Texture getTexture(){ return this.texture; }

        public long getAliveTime() {
            return aliveTime;
        }

    }

    public enum CarparkEntrances {
        Main1(new Vector2(80.5f * TILE_DIMS, 24.5f * TILE_DIMS), 0, "Fire Station"),
        Main2(new Vector2(85 * TILE_DIMS, 30.5f * TILE_DIMS), 90, "Fire Station"),
        Lower(new Vector2(52 * TILE_DIMS, 22.5f * TILE_DIMS), 90, "Lower Car Park"),
        Upper1(new Vector2(52 * TILE_DIMS, 52.5f * TILE_DIMS), 90, "Upper Car Park"),
        Upper2(new Vector2(44 * TILE_DIMS, 52 * TILE_DIMS), 90, "Upper Car Park"),
        TopLeft(new Vector2(27 * TILE_DIMS, 90 * TILE_DIMS), 0, "Top Left Car Park"),
        TopRight(new Vector2(87 * TILE_DIMS, 97 * TILE_DIMS), -90, "Top Right Car Park");

        private final Vector2 location;
        private final float rotation;
        private final String name;

        CarparkEntrances(Vector2 location, float rotation, String name) {
            this.location = location;
            this.rotation = rotation;
            this.name = name;
        }

        public Vector2 getLocation() {
            return this.location;
        }

        public float getRotation() {
            return this.rotation;
        }

        public String getName() {
            return this.name;
        }
    }

    // Debug mode
    public static final boolean DEBUG_ENABLED = false;

    // Game settings
    public static final String GAME_NAME = "Kroy";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float MAP_SCALE = 6f;
    public static final float MAP_WIDTH = 117 * (8 * MAP_SCALE);
    public static final float MAP_HEIGHT = 10000 * (8 * MAP_SCALE);
    public static final int TILE_DIMS = (int) (8 * MAP_SCALE);
    public static final int PATROL_NUMBER = 10;

    // Time durations
    public static final float BAR_FADE_DURATION = 3;
    public static final int FIRETRUCK_REPAIR_SPEED = 75;
    public static final int TIME_STATION_VULNERABLE = 180;
    public static final int MINIGAME_DURATION = 30;
    public static final int MINIGAME_SPAWN_RATE = 1000;

    // Camera settings
    public static final float LERP = 3.0f;
    public static final float ZOOM_SPEED = 0.003f;
    public static final float MIN_ZOOM = 1f;
    public static final float MAX_ZOOM = 2f;

    // Sprite properties
    // Health
    public static final int FIRESTATION_HEALTH = 300;
    // Speed
    public static final float PROJECTILE_SPEED = 400;
    // Size
    public static final float FIRETRUCK_WIDTH =  2*TILE_DIMS;
    public static final float FIRETRUCK_HEIGHT = TILE_DIMS;
    public static final float FIRESTATION_WIDTH = 8*TILE_DIMS;
    public static final float FIRESTATION_HEIGHT = 5*TILE_DIMS;
    public static final float ETFORTRESS_WIDTH = 5*TILE_DIMS;
    public static final float ETFORTRESS_HEIGHT =5*TILE_DIMS;
    public static final float PROJECTILE_WIDTH = TILE_DIMS;
    public static final float PROJECTILE_HEIGHT =0.5f*TILE_DIMS;
}