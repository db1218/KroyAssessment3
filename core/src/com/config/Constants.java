package com.config;

import com.badlogic.gdx.math.Vector2;

/**
 * Game constants for use by Kroy.
 */
public final class Constants {

    private Constants() {
        // Any constants that need instantiation go here
    }

     // Enums
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
    //					Modified for assessment 3
    // ==============================================================

    public enum FortressType {
        CLIFFORD(7, 2000, 1, 500),
        MINSTER(15, 3000,1, 500),
        RAIL(10, 1000, 1, 500),
        CASTLE1(13, 750, 2, 500),
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
                140,  // HEALTH
                10f,  // ACCELERATION
                300f, // MAX_SPEED
                0.8f, // RESTITUTION
                1.2f, // RANGE
                400,  // WATER MAX
                0,    // PRICE
                2,    // damage
        }),
        BLUE ("Blue", new float[]{
                100,   // HEALTH
                15f,   // ACCELERATION
                400f,  // MAX_SPEED
                0.6f,  // RESTITUTION
                1.01f, // RANGE
                300,   // WATER MAX
                100,   // PRICE
                2,     // damage
        }),
        YELLOW ("Yellow", new float[]{
                250,  // HEALTH
                15f,  // ACCELERATION
                500f, // MAX_SPEED
                1.6f, // RESTITUTION
                0.9f, // RANGE
                500,  // WATER MAX
                200,  // PRICE
                2,    // damage
        }),
        GREEN("Green", new float[]{
                250f, // HEALTH
                15f,  // ACCELERATION
                500f, // MAX_SPEED
                1.6f, // RESTITUTION
                0.9f, // RANGE
                500,  // WATER MAX
                300,  // PRICE
                3,    // damage
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

    // ==============================================================
    //					Added for assessment 3
    // ==============================================================
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

    // Time durations
    public static final float BAR_FADE_DURATION = 3;
    public static final int FIRETRUCK_REPAIR_SPEED = 75;
    public static final int TIME_STATION_VULNERABLE = 10;

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