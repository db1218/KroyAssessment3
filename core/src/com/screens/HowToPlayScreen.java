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

import static com.misc.Constants.DEBUG_ENABLED;
import static com.misc.Constants.MINIGAME_DURATION;

/**
 * Screen that appears when the user enters the controls screen.
 * Whilst the game is in the control screen, it is paused, the timer will stop
 * counting down. From this screen, the user can view
 * controls and information about the game objectives
 */
public class HowToPlayScreen implements Screen {

    // objects to
    private final Kroy game;

    // either main menu or pause screen, depending where they came from
    private final Screen returnScreen;

    // objects used for visuals
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
        String firetruckText = "Move the firetruck using either WASD or the arrow keys to move up, left, down and right respectively \n" +
                "The firetruck will rotate as you change direction \n" +
                "Zoom in and out by scrolling up and down";
        String fortressText = "Toggle the firehose on and off by left clicking, and use the mouse to aim at fortresses and ETs \n" +
                "Fortresses are destroyed when their health bar is depleted, and will appear as flooded \n" +
                "Press the SPACEBAR to see the direction of the nearest fortress";
        String repairAndPurchaseText = "Repair and refill your firetruck by returning to the fire station \n" +
                "Purchase new firetrucks using points earned by destroying fortresses, ETs and playing the minigame \n" +
                "When the time reaches zero, the fire station becomes vulnerable, and when destroyed can no longer \n" +
                "be used to repair or refill trucks";
        String minigameText = "Enter a minigame by driving over one of the four icons on the map \n" +
                "Entrances can only be used once. Once you have used one, that icon will disappear \n" +
                "Click the red, blue, and green ETs that appear to earn as many points as you can in " + MINIGAME_DURATION + " seconds";


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
        Label firetruckHeading = new Label("Controlling your firetruck", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label firetruckBody = new Label(firetruckText, skin);
        Image firetruckImage = new Image(new Texture(Gdx.files.internal("FireTrucks/Red/FiretruckFull.png")));
        Image WASD = new Image(new Texture(Gdx.files.internal("ControlScreen/WASD.png")));

        // Destroying and finding fortresses info
        Label fortressHeading = new Label("Destroying fortresses and aliens", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label fortressBody = new Label(fortressText, skin);
        Image fortressImage = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/fortress_1.png")));
        Image fortressDestroyedImage = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/fortress_1_wet.png")));

        // Buying and refilling new firetrucks info
        Label repairAndPurchaseHeading = new Label("Repairing and purchasing firetrucks", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label repairAndPurchaseBody = new Label(repairAndPurchaseText, skin);
        Image firestationImage = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/firestation.png")));
        Image firestationDestroyedImage = new Image(new Texture(Gdx.files.internal("MapAssets/UniqueBuildings/firestation_destroyed.png")));

        // Minigame info
        Label minigameHeading = new Label("Minigame", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label minigameBody = new Label(minigameText, skin);
        Image minigameImage = new Image(new Texture(Gdx.files.internal("minigame.png")));
        Image redAlienImage = new Image(new Texture(Gdx.files.internal("Minigame/alien_3.png")));
        Image blueAlienImage = new Image(new Texture(Gdx.files.internal("Minigame/alien_1.png")));
        Image greenAlienImage = new Image(new Texture(Gdx.files.internal("Minigame/alien_2.png")));

        // Return button
        TextButton returnButton = new TextButton("Return", skin);

        //Adding images to subtables
        firetruckImages.add(firetruckImage).size(150, 75).padRight(20);
        firetruckImages.add(WASD).size(154,100);
        fortressImages.add(fortressImage).size(100, 100).padRight(20);
        fortressImages.add(fortressDestroyedImage).size(100,100);
        firestationImages.add(firestationImage).size(62,100).padRight(20);
        firestationImages.add(firestationDestroyedImage).size(62,100);
        minigameImages.add(minigameImage).size(100,100).padRight(20);
        minigameImages.add(redAlienImage).size(100,100).padRight(20);
        minigameImages.add(blueAlienImage).size(100,100).padRight(20);
        minigameImages.add(greenAlienImage).size(100,100);

        // Adding information to main table
        table.add(heading).padBottom(40).colspan(2);
        table.row();
        table.add(firetruckHeading).padRight(80).padBottom(10);
        table.add(fortressHeading).padRight(40).padBottom(10);
        table.row();
        table.add(firetruckBody).padRight(80).padBottom(10);
        table.add(fortressBody).padRight(40).padBottom(10);
        table.row();
        table.add(firetruckImages).padRight(80).padBottom(40);
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