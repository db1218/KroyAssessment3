package com.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SFX {

    public Boolean sfx_on = true;

    public static final Music sfx_soundtrack_1 = Gdx.audio.newMusic(Gdx.files.internal("sfx/soundtrack_1.mp3"));
    public static final Music sfx_soundtrack_2 = Gdx.audio.newMusic(Gdx.files.internal("sfx/soundtrack_2.mp3"));

    public static final Sound sfx_button_click = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_button.mp3"));
    public static final Sound sfx_truck_damage = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_damage.mp3"));
    public static final Sound sfx_fortress_destroyed = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_fortress_destroyed.mp3"));



    



}
