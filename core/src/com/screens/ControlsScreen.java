package com.screens;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kroy.Kroy;

import static com.config.Constants.DEBUG_ENABLED;

/**
 * Screen that appears when the user enters the controls screen.
 * Whilst the game is in the control screen, it is paused, the timer will stop
 * counting down. From this screen, the user can view
 * controls and information about the game objectives
 */
public class ControlsScreen implements Screen {

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
    public ControlsScreen(Kroy game, Screen returnScreen) {
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
        String movement = "Move the firetruck using either WASD or the arrow keys to move up, left, down and right respectively. \n" +
                " The firetruck will rotate as you change direction. \n" +
                "Zoom in and out by scrolling up and down respectively";
        String destroying = "Toggle the firehose on and off by left clicking, and use the mouse to aim at fortresses and aliens. \n" +
                "Fortresses will appear flooded when destroyed. \n" +
                "Press the SPACEBAR to see the direction of the nearest fortress";
        String repairandpurchase = "Repair and refill your firetruck by returning to the firestation. When the time reaches 0, this is no longer possible. \n" +
                "Purchase new firetrucks using points earned by destroying fortresses";
        String minigame = "Play the minigame to reduce the number of patrols. Enter by driving over the minigame symbol";

        // Create main table to arrange actors
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Create second table to arrange score and time
        Table labels = new Table();
        labels.center();

        // Create actors
        // Heading
        Label heading = new Label("Controls", new Label.LabelStyle(game.coolFont, Color.WHITE));
        heading.setFontScale(2);

        // Movement header
        Label movementheading = new Label("Controlling your firetruck and the camera", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label movementstring = new Label(movement, skin);

        // Destroying and finding fortresses header
        Label destroyingheading = new Label("Destroying fortresses and aliens", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label destroyingstring = new Label(destroying, skin);

        // Buying and refilling new firetrucks header
        Label repairandpurchaseheading = new Label("Repairing and purchasing firetrucks", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label repairandpurchasestring = new Label(repairandpurchase, skin);

        // Minigame header
        Label minigameheading = new Label("Minigame", new Label.LabelStyle(game.coolFont, Color.WHITE));
        Label minigamestring = new Label(minigame, skin);

        // Resume button
        TextButton resumeButton = new TextButton("Return", skin);

        // Adding controls to main table
        table.add(heading).colspan(2).padBottom(40);
        table.row();
        table.add(movementheading).padRight(40).padBottom(10);
        table.add(destroyingheading).padBottom(10);
        table.row();
        table.add(movementstring).padRight(40).padBottom(40);
        table.add(destroyingstring).padBottom(40);
        table.row();
        table.add(repairandpurchaseheading).padRight(40).padBottom(10);
        table.add(minigameheading).padBottom(10);
        table.row();
        table.add(repairandpurchasestring).padRight(40).padBottom(40);
        table.add(minigamestring).padBottom(40);
        table.row();
        table.add(resumeButton).width(200).height(40).padBottom(20).colspan(2);

        // button listeners
        resumeButton.addListener(new ClickListener() {
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