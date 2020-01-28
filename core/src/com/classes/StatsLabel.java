package com.classes;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StatsLabel extends Label {

    private String text;
    private Firetruck firetruck;

    public StatsLabel(CharSequence text, Skin skin, Firetruck firetruck) {
        super(text, skin);
        this.firetruck = firetruck;
    }

    @Override
    public void act(final float delta) {
        this.setText(text);
        super.act(delta);
    }

    public void updateText() {
        this.text = "Stats:\n" +
                "\nHealth: " + firetruck.getHealthBar().getCurrentAmount() + " / " + firetruck.getHealthBar().getMaxAmount() +
                "\nWater: " + firetruck.getWaterBar().getCurrentAmount() + " / " + firetruck.getWaterBar().getMaxAmount();
    }

    public void updateTruck(Firetruck firetruck) {
        this.firetruck = firetruck;
    }
}
