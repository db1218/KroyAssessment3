package com.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.CustomActors.BackgroundBox;
import com.classes.Firestation;
import com.classes.Firetruck;
import com.kroy.Kroy;

import java.util.ArrayList;

public class CarparkScreen implements Screen {

    private final Kroy game;
    private Skin skin;
    private GameScreen gameScreen;

    private OrthographicCamera camera;
    private Viewport viewport;

    private ShapeRenderer shapeRenderer;

    private Firestation firestation;
    private Firetruck activeFiretruck;
    private Stage stage;

    private Table mainTable;
    private Table previewTable;
    private TextButton quitButton;
    private Label activeStats;

    public CarparkScreen(Firestation firestation, Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.firestation = firestation;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        TextureAtlas atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        quitButton = new TextButton("Close", skin);
        quitButton.setTransform(true);
        quitButton.scaleBy(0.25f);

        // Create table to arrange buttons.
        mainTable = new Table(skin);
        mainTable.setFillParent(true);

        previewTable = new Table(skin);

        stage = new Stage(viewport);

        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

        activeFiretruck = firestation.getActiveFireTruck();

        activeStats = new Label(activeFiretruck.getColour() + " fire truck's Stats\n" +
                "\nHealth: " + activeFiretruck.getHealthBar().getCurrentAmount() + " / " + activeFiretruck.getHealthBar().getMaxAmount() +
                "\nWater: " + activeFiretruck.getWaterBar().getCurrentAmount() + " / " + activeFiretruck.getWaterBar().getMaxAmount() +
                "\n" +
                "\nMaximum Speed: " + activeFiretruck.getMaxSpeed() +
                "\nRange: " + activeFiretruck.getRange(), skin);

        activeStats.setAlignment(Align.center);
        activeStats.setFontScale(2);

        ArrayList<Button> selectImageButtons = new ArrayList<>();
        ArrayList<TextButton> selectTextButtons = new ArrayList<>();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Firetruck firetruck = firestation.getParkedFireTrucks().get(i);

            Drawable drawable = new TextureRegionDrawable(new TextureRegion(firetruck.getFireTruckTexture()));
            drawable.setMinWidth(150);
            drawable.setMinHeight(75);

            Button imageButton = new Button(drawable);
            TextButton textButton = new TextButton(firetruck.getColour() + " Fire Truck", skin);
            textButton.setSize(150,40);

            if (!firetruck.isAlive()) {
                textButton.setTouchable(Touchable.disabled);
                textButton.setColor(Color.DARK_GRAY);
                imageButton.setColor(Color.DARK_GRAY);
            }

            selectImageButtons.add(imageButton);
            selectTextButtons.add(textButton);
        }

        mainTable.clear();
        previewTable.clear();

        // selected truck
        mainTable.row();
        mainTable.add(previewTable).expand().fill();

        previewTable.row().colspan(2).expand().pad(40);

        // image preview
        previewTable.add(firestation.getActiveFireTruck().getFireTruckImage()).size(300, 150);
        previewTable.add(activeStats);

        // truck selector
        mainTable.row().colspan(3).expand().padLeft(40).padRight(40);
        HorizontalGroup hg = new HorizontalGroup();
        hg.expand();
        hg.center();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Stack stack = new Stack();
            Button temp1 = selectImageButtons.get(i);
            temp1.setSize(200, 100);
            TextButton temp2 = selectTextButtons.get(i);
            VerticalGroup vg = new VerticalGroup();
            vg.center();
            vg.pad(40);
            vg.addActor(temp1);
            vg.addActor(selectTextButtons.get(i));
            stack.addActor(new BackgroundBox(200, 100, Color.DARK_GRAY));
            stack.addActor(vg);
            hg.addActor(stack);
        }
        mainTable.add(hg).expand().fill();

        // close button
        mainTable.row().padBottom(80).expand();
        mainTable.add(quitButton).height(40).width(150).center();

        stage.addActor(mainTable);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                firestation.openMenu(false);
                game.setScreen(gameScreen);
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                if (keycode == Input.Keys.ESCAPE) {
                    firestation.openMenu(false);
                    game.setScreen(gameScreen);
                }
                return true;
            }
        });

        for (int i = 0; i< firestation.getParkedFireTrucks().size(); i++) {
            int index = i;
            if (firestation.getParkedFireTrucks().get(i).isAlive()) {
                selectImageButtons.get(i).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        firestation.changeFiretruck(index);
                        show();
                    }
                });
                selectTextButtons.get(i).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        firestation.changeFiretruck(index);
                        System.out.println(firestation.getActiveFireTruck().isAlive());
                        show();
                    }
                });
            }
        }

    }

    public void render(float delta) {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.1f));
        shapeRenderer.rect(40, 40, Gdx.graphics.getWidth()-80, Gdx.graphics.getHeight()-80);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        stage.act(delta);
        stage.draw();
        this.firestation.decreaseInternalTime();
        this.firestation.checkRepairRefill(gameScreen.getTime(), true);
        this.updateStats();
    }

    private void updateStats() {
        activeStats.setText(activeFiretruck.getColour() + " fire truck's Stats\n" +
                "\nHealth: " + activeFiretruck.getHealthBar().getCurrentAmount() + " / " + activeFiretruck.getHealthBar().getMaxAmount() +
                "\nWater: " + activeFiretruck.getWaterBar().getCurrentAmount() + " / " + activeFiretruck.getWaterBar().getMaxAmount() +
                "\n" +
                "\nMaximum Speed: " + activeFiretruck.getMaxSpeed() +
                "\nRange: " + activeFiretruck.getRange());
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();

    }

}
