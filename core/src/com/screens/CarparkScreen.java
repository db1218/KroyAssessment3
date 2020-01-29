package com.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
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
import com.classes.Firestation;
import com.classes.Firetruck;
import com.classes.StatsLabel;
import com.kroy.Kroy;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;

public class CarparkScreen implements Screen {

    private final Kroy game;
    private Skin skin;
    private GameScreen gameScreen;

    private OrthographicCamera camera;
    private Viewport viewport;

    private ShapeRenderer shapeRenderer;

    private Firestation firestation;
    private Stage stage;

    private Table mainTable;
    private Table previewTable;
    private TextButton quitButton;

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
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);

        StatsLabel stats = new StatsLabel("Stats", skin, firestation.getActiveFireTruck());
        stats.updateText();
        stats.setAlignment(Align.center);

        ArrayList<Button> selectImageButtons = new ArrayList<>();
        ArrayList<TextButton> selectTextButtons = new ArrayList<>();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Firetruck firetruck = firestation.getParkedFireTrucks().get(i);

            Drawable drawable = new TextureRegionDrawable(new TextureRegion(firetruck.getFireTruckTexture()));
            drawable.setMinWidth(150);
            drawable.setMinHeight(75);
            Button imageButton = new Button(drawable);
            selectImageButtons.add(imageButton);

            TextButton textButton = new TextButton(firetruck.getColour() + " Fire Truck", skin);
            textButton.setSize(150,40);
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
        previewTable.add(stats);

        // truck selector
        mainTable.row().colspan(3).expand().padLeft(40).padRight(40);
        HorizontalGroup hg = new HorizontalGroup();
        hg.expand();
        hg.center();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Button temp1 = selectImageButtons.get(i);
            temp1.setSize(200, 100);
            TextButton temp2 = selectTextButtons.get(i);
            VerticalGroup vg = new VerticalGroup();
            vg.center();
            vg.pad(40);
            vg.addActor(temp1);
            vg.addActor(temp2);
            vg.addActor(selectTextButtons.get(i));
            hg.addActor(vg);
        }
        mainTable.add(hg).expand().fill();

        // close button
        mainTable.row().padBottom(80).expandX();
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
//        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.1f));
        shapeRenderer.rect(40, 40, Gdx.graphics.getWidth()-80, Gdx.graphics.getHeight()-80);
        shapeRenderer.end();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
        stage.act(delta);
        stage.draw();
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
