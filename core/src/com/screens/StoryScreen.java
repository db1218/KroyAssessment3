package com.screens;

/* =================================================================
                    New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.Kroy;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import static com.misc.Constants.DEBUG_ENABLED;

/**
 * Screen to tell the user the back story to the game
 * This text is taken from the product brief and sets
 * the scene before the user enters the game
 */
public class StoryScreen implements Screen {

    // A constant variable to store the game
    private final Kroy game;
    private GameScreen gameScreen;

    // visuals and rendering
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Skin skin;
    private final Viewport viewport;

    /**
     * The constructor for the story screen
     *
     * @param game  game object for screen changes
     */
    public StoryScreen(Kroy game) {
        this.game = game;

        // imports common skin from game
        skin = game.getSkin();

        // Create an orthographic camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.spriteBatch.setProjectionMatrix(camera.combined);

        // Create a viewport
        viewport = new ScreenViewport(camera);
        viewport.apply(true);

        // Set camera to centre of viewport
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

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

        String story = "{FADE}The year is 2042...\n" +
                "York has been invaded by evil extraterrestrials (ETs) from planet Kroy,{WAIT} who have set up fortresses in key locations around the city. \n" +
                "While ETs are more technologically advanced and outgun humans,{WAIT} they have a major and very convenient - weakness:\n " +
                "they evaporate when they come in contact with water.\n" +
                "As the leader of the Resistance,{WAIT} you have taken over York's old Fire Station and you are now in control of its fire engines.\n" +
                "Your mission is to use the fire engines you control to flood the ET fortresses,{WAIT}{WAIT} and liberate York.";

        // Create table to arrange buttons.
        Table table = new Table();
        table.center();

        Image bcg = new Image(new Texture(Gdx.files.internal("story.png")));
        Stack bcgstack = new Stack();
        bcgstack.setFillParent(true);
        bcgstack.add(bcg);
        bcgstack.add(table);

        // Create actors
        TypingLabel storyLabel = new TypingLabel(story, skin);
        storyLabel.setAlignment(Align.center);
        TextButton continueButton = new TextButton("Continue", skin);

        // Add buttons to table and style them
        table.add(storyLabel).expand();
        table.row().colspan(2);
        table.add(continueButton).width(200).height(40).padBottom(40);

        // Add listeners
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
                dispose();
            }
        });

        // Add table to stage
        stage.addActor(bcgstack);

        // creates game screen here to allow for less load time later
        gameScreen = new GameScreen(game);
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

        // Draw the button stage
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
