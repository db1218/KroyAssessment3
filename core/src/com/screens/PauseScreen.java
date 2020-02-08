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

/**
 * Screen that appears when the user pauses the game.
 * Whilst the game is paused, the timer will stop
 * counting down. From this screen, the user can exit
 * to the main menu or resume to game
 */
public class PauseScreen implements Screen {

    private final Kroy game;
    private final GameScreen gameScreen;

    private final Skin skin;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;

    /**
     * The constructor for the pause screen
     *
     * @param game          the game object to change between screens
     * @param gameScreen    game screen to go back to
     */
    public PauseScreen(Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

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
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Table labels = new Table();
        labels.center();
        Label label = new Label("Game Paused", new Label.LabelStyle(game.coolFont, Color.WHITE));
        label.setFontScale(2);
        TextButton resumeButton = new TextButton("Resume game", skin);
        TextButton quitButton = new TextButton("Return to Main Menu", skin);
        Label scoreLabel = new Label("Score: " + gameScreen.getScore(), new Label.LabelStyle(game.coolFont, Color.WHITE));
        scoreLabel.setAlignment(Align.right);
        Label timeLabel = new Label("Time: " + gameScreen.getTime(), new Label.LabelStyle(game.coolFont, Color.WHITE));
        timeLabel.setAlignment(Align.left);

        table.add(label).padBottom(20);
        table.row();
        table.add(resumeButton).width(200).height(40).padBottom(20);
        table.row();
        table.add(quitButton).width(200).height(40).padBottom(20);
        table.row();
        labels.add(scoreLabel).padRight(20);
        labels.add(timeLabel).padLeft(20);
        table.add(labels);


        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
                gameScreen.resume();
                dispose();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                gameScreen.dispose();
                dispose();
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(gameScreen);
                    gameScreen.resume();
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
