package com.screens;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
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
import com.misc.BackgroundBox;
import com.entities.Firestation;
import com.entities.Firetruck;
import com.misc.Constants;
import com.Kroy;
import java.util.ArrayList;

/**
 * This screen shows the player the "car park"
 * which is an interface allowing the user
 * to manage bought fire trucks as well as
 * buy new ones to add to the user's arsenal
 */
public class CarparkScreen implements Screen {

    // objects from other screen
    private final Kroy game;
    private final Skin skin;
    private final Firestation firestation;
    private final GameScreen gameScreen;

    // camera and visual objects
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Stage stage;

    // truck current active
    private Firetruck activeFiretruck;

    // scene 2d actors
    private final HorizontalGroup selectorGroup;
    private final TextButton closeButton;

    private final Label timeLabel;
    private final Label scoreLabel;

    private final Label activeLocation;
    private final Image activeTruckImage;
    private final Table tableStats;

    private final ArrayList<TextButton> selectTextButtons;
    private final ArrayList<Button> selectImageButtons;
    private final ArrayList<Label> selectLocationLabels;

    private final ArrayList<Label> activeStatsLabel;
    private final ArrayList<Label> activeStatsValue;

    /**
     * Constructor for car park screen
     *
     * @param game          game object
     * @param gameScreen    go back to the game once the user
     *                      has finished in this screen
     * @param firestation   where the fire trucks live
     */
    public CarparkScreen(Kroy game, GameScreen gameScreen, Firestation firestation) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.firestation = firestation;

        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new ScreenViewport(camera);
        viewport.apply();

        // create stage
        stage = new Stage(viewport, game.spriteBatch);
        stage.setDebugAll(Constants.DEBUG_ENABLED);

        // create skin used by buttons
        skin = game.getSkin();

        // create background image
        Image background = new Image(new Texture(Gdx.files.internal("garage.jpg")));
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());

        // create tables
        Table mainTable = new Table(); // stores everything in
        Table previewGroup = new Table(); // stores the

        HorizontalGroup header = new HorizontalGroup();
        header.space(200);
        header.padTop(40);

        // create location label
        activeLocation = new Label("", new Label.LabelStyle(game.coolFont, Color.WHITE));
        activeLocation.setFontScale(2);
        activeLocation.setAlignment(Align.center);

        timeLabel = new Label("Time: " + gameScreen.getFireStationTime(), new Label.LabelStyle(game.coolFont, Color.WHITE));
        timeLabel.setAlignment(Align.left);

        scoreLabel = new Label("Score: " + gameScreen.getScore(), new Label.LabelStyle(game.coolFont, Color.WHITE));
        scoreLabel.setAlignment(Align.right);

        header.addActor(scoreLabel);
        header.addActor(activeLocation);
        header.addActor(timeLabel);

        // add header to table
        mainTable.add(header);

        // preview row
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
        mainTable.row().expand();

        Stack selectorStack = new Stack();
        selectorStack.add(new BackgroundBox(100, 100, Color.DARK_GRAY, 40));
        selectorGroup = new HorizontalGroup();
        selectorStack.add(selectorGroup);
        mainTable.add(selectorStack);

        mainTable.row().expandX();

        // create close button
        closeButton = new TextButton("Close", skin);
        mainTable.add(closeButton).center().width(150).height(40).padBottom(40).padTop(40);

        mainTable.setFillParent(true);

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
                firestation.toggleMenu(false);
                game.setScreen(gameScreen);
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    firestation.toggleMenu(false);
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
                        if (boughtTruck(selectedTruck)){
                            firestation.changeFiretruck(index);
                        }
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

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        firestation.decreaseInternalTime();
        firestation.checkRepairRefill(gameScreen.getFireStationTime(), true);

        gameScreen.updatePatrolMovements();

        updateTimeScore();
        updateStatValues();
        generateStatsTable();
    }

    /**
     * @param width     of window
     * @param height    of window
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

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

    /**
     * Builds the stats table for the active fire truck
     * re-built every frame to show the health/water increasing
     */
    private void generateStatsTable() {
        tableStats.clear();

        Stack nameStack = new Stack();

        Label name = activeStatsValue.get(0);
        name.setAlignment(Align.center);

        nameStack.add(new BackgroundBox(300, 25, Color.GRAY));
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

    /**
     * Builds each fire truck item which contains:
     * - location label
     * - image button
     * - text button (select or buy)
     */
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

            if (!firetruck.isBought()) {
                title.setText(firetruck.getType().getColourString() + " Fire Truck");
                textButton.setText("Buy " + firetruck.getPrice());
                if (gameScreen.getScore() < firetruck.getPrice()) {
                    textButton.setTouchable(Touchable.disabled);
                    imageButton.setTouchable(Touchable.disabled);
                    textButton.setColor(Color.DARK_GRAY);
                    imageButton.setColor(Color.DARK_GRAY);
                }
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

    /**
     * Builds the truck selector section of the screen
     * it is called once the screen is opened and each
     * time the user selects a fire truck
     */
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

    /**
     * Buys the truck if user has enough score to buy it
     *
     * @param truck to buy
     * @return      <code>true</code> if truck is bought
     *              <code>false</code> otherwise
     */
    public boolean boughtTruck(Firetruck truck) {
        if (gameScreen.getScore() >= truck.getPrice()) {
            truck.buy();
            gameScreen.setScore((int) (gameScreen.getScore() - truck.getPrice()));
            return true;
        }
        return false;
    }

    /**
     * Generates labels and add them to the label list
     */
    private void generateStatLabels() {
        activeStatsLabel.clear();
        activeStatsLabel.add(null);
        activeStatsLabel.add(new Label(" Health         ", game.getFont10()));
        activeStatsLabel.add(new Label(" Water          ", game.getFont10()));
        activeStatsLabel.add(new Label(" Speed          ", game.getFont10()));
        activeStatsLabel.add(new Label(" Range          ", game.getFont10()));
        activeStatsLabel.add(new Label(" Damage         ", game.getFont10()));
    }

    /**
     * Generate updated stats labels and add them to list
     * read to be added to the stage
     */
    private void updateStatValues() {
        activeStatsValue.clear();
        activeStatsValue.add(new Label(activeFiretruck.getType().getColourString() + " fire truck's Stats", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getHealthBar().getCurrentAmount() + " / " + activeFiretruck.getHealthBar().getMaxAmount() + " ", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getWaterBar().getCurrentAmount() + " / " + activeFiretruck.getWaterBar().getMaxAmount() + " ", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getMaxSpeed() + " ", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getRange() + " ", game.getFont10()));
        activeStatsValue.add(new Label(activeFiretruck.getDamage() + " ", game.getFont10()));
    }

    /**
     * Update the time and score labels
     */
    private void updateTimeScore() {
        timeLabel.setText("Time: " + gameScreen.getFireStationTime());
        scoreLabel.setText("Score: " + gameScreen.getScore());
    }

}
