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

    // Class sizing
    public static final double SCORE_Y = SCREEN_HEIGHT * 0.65;
    public static final double SCORE_X = SCREEN_WIDTH * 0.05;
}