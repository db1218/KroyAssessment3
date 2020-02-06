package com.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private ShapeRenderer shapeRenderer;

    private Firestation firestation;
    private Firetruck activeFiretruck;
    private Stage stage;

    private HorizontalGroup selectorGroup;
    private TextButton closeButton;

    private Label activeLocation;
    private Image activeTruckImage;
    private Table tableStats;

    private ArrayList<Firetruck> trucksBought;

    private ArrayList<TextButton> selectTextButtons;
    private ArrayList<Button> selectImageButtons;
    private ArrayList<Label> selectLocationLabels;

    private ArrayList<Label> activeStatsLabel;
    private ArrayList<Label> activeStatsValue;

    public CarparkScreen(Firestation firestation, Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.firestation = firestation;

        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new ScreenViewport(camera);
        viewport.apply();

        // create stage
        stage = new Stage(viewport);
        stage.setDebugAll(Constants.DEBUG_ENABLED);

        // create skin used by buttons
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));

        // create background image
        Image background = new Image(new Texture(Gdx.files.internal("garage.jpg")));
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());

        // create tables
        Table mainTable = new Table(); // stores everything in
        Table previewGroup = new Table(); // stores the

        // create location label
        activeLocation = new Label("", game.getFont15());
        activeLocation.setAlignment(Align.center);
        mainTable.add(activeLocation).padTop(40);
        mainTable.row().expand();

        // background shape behind stats table
        Stack previewStack = new Stack();
        previewStack.add(new BackgroundBox(300, 300, Color.DARK_GRAY, 10));

        // create a placeholder image that can be replaced once the screen is called
        activeTruckImage = new Image(new Texture(Gdx.files.internal("waterSplash1.png")));
        previewGroup.add(activeTruckImage).size(300, 150).padRight(40);

        // create table for the active truck's stats, this can be updated in the method below
        tableStats = new Table();
        previewGroup.add(tableStats);

        previewStack.add(previewGroup);

        // create a padded border around the preview
        previewGroup.pad(20);

        mainTable.add(previewStack);

        // TRUCK SELECTOR

        // creates label lists
        activeStatsLabel = new ArrayList<Label>();
        activeStatsValue = new ArrayList<Label>();
        generateStatLabels();

        // create buttons for each parked truck
        selectLocationLabels = new ArrayList<>();
        selectImageButtons = new ArrayList<>();
        selectTextButtons = new ArrayList<>();
        generateTruckButtons();

        // preview row
        mainTable.row().expandX();

        Stack selectorStack = new Stack();
        selectorStack.add(new BackgroundBox(100, 100, Color.DARK_GRAY, 40));
        selectorGroup = new HorizontalGroup();
        selectorStack.add(selectorGroup);
        mainTable.add(selectorStack);

        mainTable.row().expandX();

        // create close button
        closeButton = new TextButton("Close", skin);
        mainTable.add(closeButton).center().width(150).height(40).padBottom(40);

        mainTable.setFillParent(true);

        this.trucksBought = new ArrayList<>();

        stage.addActor(background);
        stage.addActor(mainTable);

    }


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // set the active fire truck
        activeFiretruck = firestation.getActiveFireTruck();
        activeLocation.setText(activeFiretruck.getCarpark().getName());
        activeTruckImage.setDrawable(activeFiretruck.getFireTruckImage().getDrawable());

        updateStatValues();
        generateStatLabels();
        generateStatsTable();

        generateTruckButtons();
        generateTruckSelector();

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                firestation.openMenu(false);
                game.setScreen(gameScreen);
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    firestation.openMenu(false);
                    game.setScreen(gameScreen);
                }
                return true;
            }
        });

        for (int i = 0; i< firestation.getParkedFireTrucks().size(); i++) {
            int index = i;
            Firetruck selectedTruck = firestation.getParkedFireTrucks().get(i);
            if (!firestation.getParkedFireTrucks().get(i).isBought()){

                selectTextButtons.get(i).addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        buyTruck(selectedTruck);
                        show();
                    }
                });
            }

            else if (firestation.getParkedFireTrucks().get(i).isAlive()) {
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
        stage.act(delta);
        stage.draw();
        firestation.decreaseInternalTime();
        firestation.checkRepairRefill(gameScreen.getTime(), true);
        updateStatValues();
        generateStatsTable();
    }

    private void generateStatsTable() {
        tableStats.clear();
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
    }

    private void generateTruckSelector() {
        selectorGroup.clear();
        selectorGroup.pad(50);
        selectorGroup.expand();
        selectorGroup.center();
        selectorGroup.space(30);
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Stack stack = new Stack();
            VerticalGroup vgTruck = new VerticalGroup();
            vgTruck.center();
            vgTruck.pad(30);
            vgTruck.addActor(selectLocationLabels.get(i));
            vgTruck.addActor(selectImageButtons.get(i));
            vgTruck.addActor(selectTextButtons.get(i));
            stack.addActor(new BackgroundBox(200, 100, Color.GRAY, 10));
            stack.addActor(vgTruck);
            selectorGroup.addActor(stack);
        }
    }

    private void generateTruckButtons() {
        selectLocationLabels.clear();
        selectImageButtons.clear();
        selectTextButtons.clear();
        for (int i=0; i<firestation.getParkedFireTrucks().size(); i++) {
            Firetruck firetruck = firestation.getParkedFireTrucks().get(i);

            Label title = new Label("", skin);
            title.setAlignment(Align.center);

            Drawable drawable = new TextureRegionDrawable(new TextureRegion(firetruck.getFireTruckTexture()));
            drawable.setMinWidth(150);
            drawable.setMinHeight(75);

            Button imageButton = new Button(drawable);
            TextButton textButton = new TextButton("", skin);
            textButton.setSize(150,40);

            Drawable drawable1 = new TextureRegionDrawable(new TextureRegion(new Texture ("alienProjectile.png")));
            drawable1.setMinWidth(150);
            drawable1.setMinHeight(75);


            if (!firetruck.isBought()) {
                title.setText(firetruck.getType().getColourString() + " Fire Truck");
                textButton.setText("Buy " + firetruck.getPrice());
            } else if (!firetruck.isAlive()) {
                textButton.setText(firetruck.getType().getColourString() + " Fire Truck");
                textButton.setTouchable(Touchable.disabled);
                textButton.setColor(Color.DARK_GRAY);
                imageButton.setColor(Color.DARK_GRAY);
                title.setText("DEAD");
            } else {
                title.setText("Location: " + firetruck.getCarpark().getName());
                textButton.setText(firetruck.getType().getColourString() + " Fire Truck");
            }

            selectLocationLabels.add(title);
            selectImageButtons.add(imageButton);
            selectTextButtons.add(textButton);
        }
    }

    private void generateStatLabels() {
        activeStatsLabel.clear();
        activeStatsLabel.add(null);
        activeStatsLabel.add(new Label(" Health         ", game.getFont10()));
        activeStatsLabel.add(new Label(" Water          ", game.getFont10()));
        activeStatsLabel.add(new Label(" Speed          ", game.getFont10()));
        activeStatsLabel.add(new Label(" Range          ", game.getFont10()));
    }

    private void updateStatValues() {
        activeStatsValue.clear();
        activeStatsValue.add(new Label(activeFiretruck.getType().getColourString() + " fire truck's Stats", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getHealthBar().getCurrentAmount() + " / " + activeFiretruck.getHealthBar().getMaxAmount() + " ", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getWaterBar().getCurrentAmount() + " / " + activeFiretruck.getWaterBar().getMaxAmount() + " ", game.getFont10()));
        activeStatsValue.add(new Label(String.valueOf(activeFiretruck.getMaxSpeed()) + " ", game.getFont10()));
        activeStatsValue.add(new Label(String.valueOf(activeFiretruck.getRange()) + " ", game.getFont10()));
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

    public void buyTruck(Firetruck truck){
        if (canBuyTruck(truck)) {
            truck.buy();
            this.firestation.setTrucksBought(truck);
            gameScreen.setScore((int) (gameScreen.getScore() - truck.getPrice()));
        }
    }

    public boolean canBuyTruck(Firetruck truck){
        return gameScreen.getScore() >= truck.getPrice();
    }

    public ArrayList<Firetruck> getTrucksBought(){
        return this.trucksBought;
    }

}
