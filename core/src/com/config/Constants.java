package com.config;

// Allows access to constants throughout project
public final class Constants {

    private Constants() {
        // Any constants that need instantiation go here
    }

    // Game settings
    public static final String GAME_NAME = "Kroy";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int MAP_WIDTH = 10000;
    public static final int MAP_HEIGHT = 10000;

    // Class sizing
    public static final double SCREEN_CENTRE_X = SCREEN_WIDTH * 0.5;
    public static final double SCREEN_CENTRE_Y = SCREEN_HEIGHT * 0.5;
    public static final double SCORE_Y = SCREEN_HEIGHT * 0.95;
    public static final double SCORE_X = SCREEN_WIDTH * 0.05;

    // Sprite sizing
    public static final int SPRITE_WIDTH = 200;
    public static final int SPRITE_HEIGHT = 200;

}