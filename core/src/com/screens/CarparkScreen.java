package com.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.classes.Firestation;
import com.classes.Firetruck;
import com.kroy.Kroy;

import java.util.ArrayList;

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;

public class CarparkScreen implements Screen {

    private final Kroy game;
    private Skin skin;
    private GameScreen gameScreen;

    private OrthographicCamera camera;
    private Viewport viewport;

    private TextButton quitButton;
    private ArrayList<Button> selectButtons;

    private Firestation firestation;
    private Stage stage;

    private boolean open;

    private Table mainTable;
    private Table previewTable;
    private Table selectorTable;
    private Label stats;

    public CarparkScreen(Firestation firestation, Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.firestation = firestation;

        SpriteBatch batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        TextureAtlas atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        quitButton = new TextButton("Close", skin);
        quitButton.setTransform(true);
        quitButton.scaleBy(0.25f);

        Container<Table> tableContainer = new Container<Table>();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cw = sw * 0.7f;
        float ch = sh * 0.5f;
        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        // Create table to arrange buttons.
        mainTable = new Table(skin);
        mainTable.setFillParent(true);
        mainTable.setDebug(true);

        previewTable = new Table(skin);
        previewTable.setDebug(true);

        selectorTable = new Table(skin);
        selectorTable.setDebug(true);

        stage = new Stage(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera));
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.refresh();

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                firestation.openCarpark(false);
                game.setScreen(gameScreen);
            }
        });

        for (int i=0; i<selectButtons.size(); i++) {
            Button button = selectButtons.get(i);
            int index = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    firestation.changeFiretruck(index);
                    show();
                }
            });
        }

    }

    public void refresh() {
        mainTable.clear();
        previewTable.clear();
        selectorTable.clear();

        Firetruck activeFiretruck = firestation.getActiveFireTruck();

        selectButtons = new ArrayList<Button>();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(firestation.getParkedFireTrucks().get(i).getFireTruckTexture()));
            Button button = new Button(drawable);
            selectButtons.add(button);
        }

        stats = new Label("Stats \nThis truck is cool " +
                "\nHealth: " + activeFiretruck.getHealthBar().getCurrentAmount() + " / " + activeFiretruck.getHealthBar().getMaxAmount() +
                "\nWater: " + activeFiretruck.getWaterBar().getCurrentAmount() + " / " + activeFiretruck.getWaterBar().getMaxAmount(), skin);

        mainTable.row().colspan(3).expand().fill();
        mainTable.add(previewTable).expand().fill();
        previewTable.row().colspan(2).expand().fill().pad(40);
        previewTable.add(firestation.getActiveFireTruck().getFireTruckImage()).size(300, 150);
        previewTable.add(stats).expand();
        mainTable.row().colspan(3).expand().fill().padLeft(40).padRight(40);
        mainTable.add(selectorTable);
        selectorTable.row().colspan(6).expand();
        System.out.println(firestation.getParkedFireTrucks().size());
        for (Button button : selectButtons) {
            selectorTable.add(button).size(150, 75);
        }
        mainTable.row().colspan(1).pad(40).expandX();
        mainTable.add(quitButton).height(40).width(150).center();

        stage.addActor(mainTable);
    }

    public void render(float delta) {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    public void openCarpark(boolean bool) {
        this.open = bool;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void dispose() {
        stage.dispose();
    }

}
