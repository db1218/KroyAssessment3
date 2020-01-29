package com.config;

/**
 * Game constants for use by Kroy.
 */
public final class Constants {

    private Constants() {
        // Any constants that need instantiation go here
    }

    // Firetruck One properties.
    public static final float[] FiretruckOneProperties = {
        100,  // HEALTH
        10f,  // ACCELERATION
        300f, // MAX_SPEED
        0.8f, // RESTITUTION
        1.2f, // RANGE
        400,  // WATER MAX
    };

    // Firetruck Two properties
    public static final float[] FiretruckTwoProperties = {
        100,  // HEALTH
        15f,  // ACCELERATION
        400f, // MAX_SPEED
        0.6f, // RESTITUTION
        1.01f,// RANGE
        300,  // WATER MAX
    };

     // Enums
     public static enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
         STATIC
    }

    public enum TruckColours{
        RED ("RED", "Red"),
        BLUE ("BLUE", "Blue"),
        YELLOW ("YELLOW", "Yellow");

        private final String colourUpper;
        private  final String colourLower;

        TruckColours(String upper, String lower) {
            this.colourUpper = upper;
            this.colourLower = lower;
        }

        public String getColourUpper() {
             return this.colourUpper;
        }
        public String getColourLower(){
             return this.colourLower;
        }
    }

    // Debug mode
    public static final boolean DEBUG_ENABLED = true;

    // Game settings
    public static final String GAME_NAME = "Kroy";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float MAP_SCALE = 6f;
    public static final float MAP_WIDTH = 117 * (8 * MAP_SCALE);
    public static final float MAP_HEIGHT = 10000 * (8 * MAP_SCALE);
    public static final int TILE_DIMS = (int) (8 * MAP_SCALE);
    public static final String COLLISION_TILE = "BLOCKED";

    // Time durations
    public static final float BAR_FADE_DURATION = 3;
    public static final int FIRETRUCK_REPAIR_SPEED = 75;

    // Camera settings
    public static final float LERP = 3.0f;
    public static final float MIN_ZOOM = 1f;
    public static final float MAX_ZOOM = 5f;

    // Screen elements sizing
    public static final float FONT_Y = 0.45f;
    public static final float SCORE_X = 0.47f;
    public static final float TIME_X = 0.4f;

    // Sprite properties
    // Damage
    public static final int FIRETRUCK_DAMAGE = 2;
    public static final int PROJECTILE_DAMAGE = 10;
    // Health
    public static final int ETFORTRESS_HEALTH = 2000;
    public static final int ETFORTRESS_HEALING = 1;
    public static final int FIRESTATION_HEALTH = 1000;
    // Speed
    public static final float PROJECTILE_SPEED = 400;
    // Size
    public static final float FIRETRUCK_WIDTH =  2*TILE_DIMS;
    public static final float FIRETRUCK_HEIGHT = 1*TILE_DIMS;
    public static final float FIRESTATION_WIDTH = 8*TILE_DIMS;
    public static final float FIRESTATION_HEIGHT = 5*TILE_DIMS;
    public static final float ETFORTRESS_WIDTH = 5*TILE_DIMS;
    public static final float ETFORTRESS_HEIGHT =5*TILE_DIMS;
    public static final float PROJECTILE_WIDTH = 1*TILE_DIMS;
    public static final float PROJECTILE_HEIGHT =0.5f*TILE_DIMS;

    public static final float FIRETRUCK_BOUNCEBACK = 5f;

}