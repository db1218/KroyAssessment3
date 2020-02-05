package com.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.CustomActors.BackgroundBox;
import com.classes.Firestation;
import com.classes.Firetruck;
import com.config.Constants;
import com.kroy.Kroy;
import java.util.ArrayList;

public class CarparkScreen implements Screen {

    private final Kroy game;
    private Skin skin;
    private GameScreen gameScreen;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Image background;

    private ShapeRenderer shapeRenderer;

    private Firestation firestation;
    private Firetruck activeFiretruck;
    private Stage stage;

    private Table mainTable;
    private Table previewTable;
    private TextButton quitButton;

    private ArrayList<Label> activeStatsLabel;
    private ArrayList<Label> activeStatsValue;

    public CarparkScreen(Firestation firestation, Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.firestation = firestation;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new ScreenViewport(camera);
        viewport.apply();

        background = new Image(new Texture(Gdx.files.internal("garage.jpg")));
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());

        TextureAtlas atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        quitButton = new TextButton("Close", skin);
        quitButton.setTransform(true);
        quitButton.scaleBy(0.25f);

        // Create table to arrange buttons.
        mainTable = new Table(skin);

        previewTable = new Table(skin);

        stage = new Stage(viewport);

        shapeRenderer = new ShapeRenderer();

        activeStatsLabel = new ArrayList<Label>();
        activeStatsValue = new ArrayList<Label>();
        generateStatLabels();
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        stage.setDebugAll(Constants.DEBUG_ENABLED);
        Gdx.input.setInputProcessor(stage);

        activeFiretruck = firestation.getActiveFireTruck();
        updateStatValues();

        ArrayList<Label> selectLocationLabel = new ArrayList<>();
        ArrayList<Button> selectImageButtons = new ArrayList<>();
        ArrayList<TextButton> selectTextButtons = new ArrayList<>();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Firetruck firetruck = firestation.getParkedFireTrucks().get(i);

            Label title = new Label("", skin);
            title.setAlignment(Align.center);

            Drawable drawable = new TextureRegionDrawable(new TextureRegion(firetruck.getFireTruckTexture()));
            drawable.setMinWidth(150);
            drawable.setMinHeight(75);

            Button imageButton = new Button(drawable);
            TextButton textButton = new TextButton(firetruck.getType().getColour() + " Fire Truck", skin);
            textButton.setSize(150,40);



            if (!firetruck.isAlive()) {
                textButton.setTouchable(Touchable.disabled);
                textButton.setColor(Color.DARK_GRAY);
                imageButton.setColor(Color.DARK_GRAY);
                title.setText("DEAD");
            } else {
                title.setText("Location: " + firetruck.getCarpark().getName());
            }

            selectLocationLabel.add(title);
            selectImageButtons.add(imageButton);
            selectTextButtons.add(textButton);
        }

        mainTable.clear();
        previewTable.clear();

        // selected truck

        Label heading = new Label(activeFiretruck.getCarpark().getName(), game.getLabelStyle());
        heading.setAlignment(Align.center);
        mainTable.add(heading).padTop(40);
        mainTable.row();
        mainTable.add(previewTable).expand().fill();

        previewTable.row().colspan(2).expand();

        // image preview
        previewTable.add(firestation.getActiveFireTruck().getFireTruckImage()).size(300, 150);
        Stack statsStack = new Stack();
        statsStack.add(new BackgroundBox(300, 300, Color.DARK_GRAY, 10));
        Table tableStats = new Table();
        tableStats.pad(50);
        statsStack.add(tableStats);

        Stack nameStack = new Stack();
        nameStack.add(new BackgroundBox(300, 25, Color.GRAY));
        Label name = activeStatsValue.get(0);
        name.setAlignment(Align.center);
        nameStack.add(name);
        tableStats.add(nameStack).colspan(2).padBottom(20).fillX();

        for (int i=1; i<activeStatsLabel.size(); i++) {
            Label label = activeStatsLabel.get(i);
            Label value = activeStatsValue.get(i);
            label.setAlignment(Align.left);
            value.setAlignment(Align.right);

            tableStats.row().padBottom(10).expandX().fillX();

            Stack labelStack = new Stack();
            labelStack.add(new BackgroundBox(100, 25, Color.GRAY));
            labelStack.add(label);
            tableStats.add(labelStack).left();

            Stack valueStack = new Stack();
            valueStack.add(new BackgroundBox(100, 25, Color.GRAY));
            valueStack.add(value);
            tableStats.add(valueStack).right();
        }
        previewTable.add(statsStack);

        // truck selector
        mainTable.row().colspan(3).expandX();
        Stack selectorStack = new Stack();
        selectorStack.add(new BackgroundBox(100, 100, Color.DARK_GRAY, 40));
        HorizontalGroup hg = new HorizontalGroup();
        hg.pad(50);
        hg.expand();
        hg.center();
        hg.space(30);
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Stack stack = new Stack();
            VerticalGroup vgTruck = new VerticalGroup();
            vgTruck.center();
            vgTruck.pad(30);
            vgTruck.addActor(selectLocationLabel.get(i));
            vgTruck.addActor(selectImageButtons.get(i));
            vgTruck.addActor(selectTextButtons.get(i));
            stack.addActor(new BackgroundBox(200, 100, Color.GRAY, 10));
            stack.addActor(vgTruck);
            hg.addActor(stack);
        }
        selectorStack.add(hg);
        mainTable.add(selectorStack);

        // close button
        mainTable.row().padBottom(80).expand();
        mainTable.add(quitButton).height(40).width(150).center();

        stage.addActor(background);
        stage.addActor(mainTable);
        mainTable.setFillParent(true);

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
                        show();
                    }
                });
            }
        }

    }

    public void render(float delta) {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        stage.act(delta);
        stage.draw();
        this.firestation.decreaseInternalTime();
        this.firestation.checkRepairRefill(gameScreen.getTime(), true);
        this.updateStatValues();
    }

    private void updateStatValues() {
        activeStatsValue.clear();
        activeStatsValue.add(new Label(activeFiretruck.getType().getColour() + " fire truck's Stats", game.getLabelStyle()));
        activeStatsValue.add(new Label(activeFiretruck.getHealthBar().getCurrentAmount() + " / " + activeFiretruck.getHealthBar().getMaxAmount(), game.getLabelStyle()));
        activeStatsValue.add(new Label(activeFiretruck.getWaterBar().getCurrentAmount() + " / " + activeFiretruck.getWaterBar().getMaxAmount(), game.getLabelStyle()));
        activeStatsValue.add(new Label(String.valueOf(activeFiretruck.getMaxSpeed()), game.getLabelStyle()));
        activeStatsValue.add(new Label(String.valueOf(activeFiretruck.getRange()), game.getLabelStyle()));
    }

    private void generateStatLabels() {
        activeStatsLabel.add(null);
        activeStatsLabel.add(new Label("Health: ", game.getLabelStyle()));
        activeStatsLabel.add(new Label("Water: ", game.getLabelStyle()));
        activeStatsLabel.add(new Label("Speed: ", game.getLabelStyle()));
        activeStatsLabel.add(new Label("Range: ", game.getLabelStyle()));
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
