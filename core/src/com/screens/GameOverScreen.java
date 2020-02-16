package com.screens;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.misc.Constants.*;
import com.Kroy;

import static com.misc.Constants.DEBUG_ENABLED;

/**
 * Really basic screen that provides the user whether they have
 * won or lost the game, displays their score and allows them to
 * navigate back to the main menu
 */
public class GameOverScreen implements Screen {

    // values passed in from GameScreen
    private final Kroy game;
    private final Outcome outcome;
    private final int score;

    // camera, visual and appearance objects
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;
    private final Skin skin;

    /**
     * Constructor for Game over screen, with inputs from
     * {@link GameScreen} once the game has ended
     *
     * @param game      to access shared objects
     * @param outcome   either win or lose, depending on game outcome
     * @param score     how much score the player earned in the game
     */
    public GameOverScreen(Kroy game, Outcome outcome, int score) {
        this.game = game;
        this.outcome = outcome;
        this.score = score;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
        //skin.add("default", new Texture("button.png"));

        // Create an orthographic camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.spriteBatch.setProjectionMatrix(camera.combined);

        // Create a viewport
        viewport = new ScreenViewport(camera);
        viewport.apply();

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


        Table table = new Table();
        table.center();

        Image bcg;
        Stack bcgstack = new Stack();
        bcgstack.setFillParent(true);

        Label outcomeLabel = new Label("", new Label.LabelStyle(game.coolFont, Color.WHITE));
        outcomeLabel.setFontScale(2);
        if (outcome.equals(Outcome.WON)){
            bcg = new Image(new Texture(Gdx.files.internal("win.png")));
            outcomeLabel.setText("Well done, you saved York!");
        } else {
            bcg = new Image(new Texture(Gdx.files.internal("game_over.png")));
            outcomeLabel.setText("Well... you let York down...");
        }

        bcgstack.add(bcg);
        bcgstack.add(table);

        TextButton exitButton = new TextButton("Return to Main Menu", skin);

        Label scoreLabel = new Label("Final Score: " + score, new Label.LabelStyle(game.coolFont, Color.WHITE));
        scoreLabel.setAlignment(Align.center);

        table.add(outcomeLabel).padBottom(20);
        table.row();
        table.add(exitButton).width(200).height(40);
        table.row();
        table.add(scoreLabel).width(200).height(40);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(bcgstack);
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

    }
}
