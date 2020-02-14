package com.misc;

/*
 *  =======================================================================
 *                    New class Added for Assessment 3
 *  =======================================================================
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SFX {

    public static Boolean sfx_on = true;

    public static final Music sfx_soundtrack_1 = Gdx.audio.newMusic(Gdx.files.internal("sfx/soundtrack_1.mp3"));
    public static final Music sfx_soundtrack_2 = Gdx.audio.newMusic(Gdx.files.internal("sfx/soundtrack_2.mp3"));

    public static final Sound sfx_button_click = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_button.wav"));
    public static final Sound sfx_truck_damage = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_damage.wav"));
    public static final Sound sfx_fortress_destroyed = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_fortress_destroyed.wav"));
    public static final Sound sfx_projectile = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_projectile.wav"));
    public static final Sound sfx_patrol_dies = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_patrol_dies.wav"));
    public static final Sound sfx_garage = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_garage.wav"));


    public static void playMenuMusic() {
        if (sfx_on) {
            if (sfx_soundtrack_2.isPlaying()) {
                sfx_soundtrack_2.pause();
                sfx_soundtrack_1.play();
            } else {
                sfx_soundtrack_1.play();
            }
        }
    }

    public static void playGameMusic() {
        if (sfx_on) {
            if (sfx_soundtrack_1.isPlaying()) {
                sfx_soundtrack_1.pause();
                sfx_soundtrack_2.play();
            } else {
                sfx_soundtrack_2.play();
            }
        }
    }

    public static void disableSFX(String currentScreen) {
        sfx_on = false;

        sfx_soundtrack_1.pause();
        sfx_soundtrack_2.pause();
    }

    public static void enableSFX(short currentScreen) {
        sfx_on = true;

        if (currentScreen == 0) {sfx_soundtrack_1.play();}
        else if (currentScreen == 1) {sfx_soundtrack_2.play();}
    }


}
