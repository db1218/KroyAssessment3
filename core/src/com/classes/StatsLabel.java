package com.classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.awt.*;

public class StatsLabel extends Label {

    private String text;
    private Firetruck firetruck;
    private LabelStyle style;

    public StatsLabel(CharSequence text, Skin skin, Firetruck firetruck) {
        super(text, skin);
        this.firetruck = firetruck;
        this.setColor(Color.BLUE);
        this.setScale(100);
    }

    @Override
    public void act(final float delta) {
        this.setText(text);
        super.act(delta);
    }

    public void updateText() {
        this.text = "Stats\n" +
                "\nHealth: " + firetruck.getHealthBar().getCurrentAmount() + " / " + firetruck.getHealthBar().getMaxAmount() +
                "\nWater: " + firetruck.getWaterBar().getCurrentAmount() + " / " + firetruck.getWaterBar().getMaxAmount() +
                "\n" +
                "\nMaximum Speed: " + firetruck.getMaxSpeed() +
                "\nRange: " + firetruck.getRange();
    }

    public void updateTruck(Firetruck firetruck) {
        this.firetruck = firetruck;
    }
}
