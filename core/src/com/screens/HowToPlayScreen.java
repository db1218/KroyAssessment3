package com.screens;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.Kroy;

import static com.config.Constants.DEBUG_ENABLED;
import static com.config.Constants.MINIGAME_DURATION;

/**
 * Screen that appears when the user enters the controls screen.
 * Whilst the game is in the control screen, it is paused, the timer will stop
 * counting down. From this screen, the user can view
 * controls and information about the game objectives
 */
public class HowToPlayScreen implements Screen {

    private final Kroy game;
    private final Screen returnScreen;

    private final Skin skin;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;

    /**
     * The constructor for the control screen
     *
     * @param game          the game object to change between screens
     * @param returnScreen  screen to go back to
     */
    public HowToPlayScreen(Kroy game, Screen returnScreen) {
        this.game = game;
        this.returnScreen = returnScreen;

        // skin for buttons
        skin = game.getSkin();

        // Create an orthographic camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set camera to centre of viewport
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // Create a viewport
        viewport = new ScreenViewport(camera);
        viewport.apply(true);

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.spriteBatch.setProjectionMatrix(camera.combined);

        // Create a stage for buttons
        stage = new Stage(viewport, game.spriteBatch);
        stage.setDebugAll(DEBUG_ENABLED);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Strings for controls
        String movement =           "Move the firetruck using either WASD or the arrow keys to move up, left, down and right respectively \n" +
                                    "The firetruck will rotate as you change direction \n" +
                                    "Zoom in and out by scrolling up and down";
        String destroying =         "Toggle the firehose on and off by left clicking, and use the mouse to aim at fortresses and aliens \n" +
                                    "Fortresses are destroyed when their health reaches 0, and will appear as flooded \n" +
                                    "Press the SPACEBAR to see the direction of the nearest fortress";
        String repairAndPurchase =  "Repair and refill your firetruck by returning to the firestation \n" +
                                    "Purchase new firetrucks using points earned by destroying fortresses, aliens and playing the minigame \n" +
                                    "When the time reaches 0, the firestation becomes vulnerable, and when destroyed can no longer be used";
        String minigame =           "Enter a minigame by driving over one of the five icons on the map \n" +
                                    "Click the red, blue, and green aliens that appear to earn as many points as you can in " + MINIGAME_DURATION + " seconds";


        // Create main table to arrange actors
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Create subtables to store images
        Table fortressImages = new Table();
        fortressImages.center();
        Table firetruckImages = new Table();
        firetruckImages.center();
        Table firestationImages = new Table();
        firestationImages.center();
        Table minigameImages = new Table();
        minigameImages.center();

        // Create actors
        // Heading
        Label heading = new Label("How to Play", new Label.LabelStyle(game.coolFont, Color.WHITE));
        heading.setFontScale(2);

        // Movement info
        Label movementHeading = new Label("Controlling your firetruck", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label movementBody = new Label(movement, skin);
        Image firetruck = new Image(new Texture(Gdx.files.internal("FireTrucks/Red/FiretruckFull.png")));
        Image WASD = new Image(new Texture(Gdx.files.internal("ControlScreen/WASD.png")));

        // Destroying and finding fortresses info
        Label destroyingHeading = new Label("Destroying fortresses and aliens", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label destroyingBody = new Label(destroying, skin);
        Image fortress = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/fortress_1.png")));
        Image fortressDestroyed = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/fortress_1_wet.png")));

        // Buying and refilling new firetrucks info
        Label repairAndPurchaseHeading = new Label("Repairing and purchasing firetrucks", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label repairAndPurchaseBody = new Label(repairAndPurchase, skin);
        Image firestation = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/firestation.png")));
        Image firestationDestroyed = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/firestation_destroyed.png")));

        // Minigame info
        Label minigameHeading = new Label("Minigame", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label minigameBody = new Label(minigame, skin);
        Image minigameSprite = new Image(new Texture(Gdx.files.internal("minigame.png")));
        Image redAlien = new Image(new Texture(Gdx.files.internal("Minigame/redAlien.png")));
        Image blueAlien = new Image(new Texture(Gdx.files.internal("Minigame/blueAlien.png")));
        Image greenAlien = new Image(new Texture(Gdx.files.internal("Minigame/aliensquare.png")));

        // Return button
        TextButton returnButton = new TextButton("Return", skin);

        //Adding images to subtables
        firetruckImages.add(firetruck).size(150, 75).padRight(20);
        firetruckImages.add(WASD).size(154,100);
        fortressImages.add(fortress).size(100, 100).padRight(20);
        fortressImages.add(fortressDestroyed).size(100,100);
        firestationImages.add(firestation).size(62,100).padRight(20);
        firestationImages.add(firestationDestroyed).size(62,100);
        minigameImages.add(minigameSprite).size(100,100).padRight(20);
        minigameImages.add(redAlien).size(100,100).padRight(20);
        minigameImages.add(blueAlien).size(100,100).padRight(20);
        minigameImages.add(greenAlien).size(152,80);

        // Adding information to main table
        table.add(heading).padBottom(40).colspan(2);
        table.row();
        table.add(movementHeading).padRight(80).padBottom(10);
        table.add(destroyingHeading).padRight(40).padBottom(10);
        table.row();
        table.add(movementBody).padRight(80).padBottom(10);
        table.add(destroyingBody).padRight(40).padBottom(10);
        table.row();
        table.add(firetruck).padRight(80).padBottom(40);
        table.add(fortressImages).padRight(80).padBottom(40);
        table.row();
        table.add(repairAndPurchaseHeading).padRight(80).padBottom(10);
        table.add(minigameHeading).padRight(40).padBottom(10);
        table.row();
        table.add(repairAndPurchaseBody).padRight(80).padBottom(10);
        table.add(minigameBody).padRight(40).padBottom(10);
        table.row();
        table.add(firestationImages).padRight(80).padBottom(40);
        table.add(minigameImages).padRight(40).padBottom(40);
        table.row();
        table.add(returnButton).width(200).height(40).padBottom(20).colspan(2);

        // button listeners
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(returnScreen);
                dispose();
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(returnScreen);
                    dispose();
                }
                return true;
            }
        });

        stage.addActor(table);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * @param width of window
     * @param height of window
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

    }
}