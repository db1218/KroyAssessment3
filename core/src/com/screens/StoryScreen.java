package com.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.config.Constants;
import com.kroy.Kroy;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class StoryScreen implements Screen {

    private final Kroy game;
    private OrthographicCamera camera;
    private Stage stage;
    protected Texture texture;
    private Skin skin;
    private Viewport viewport;

    private TypingLabel storyLabel;

    private String story;

    public StoryScreen(Kroy game) {
        this.game = game;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));

        // Create new sprite batch

        // Create an orthographic camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		/* tell the SpriteBatch to render in the
		   coordinate system specified by the camera. */
        game.spriteBatch.setProjectionMatrix(camera.combined);

        // Create a viewport
        viewport = new ScreenViewport(camera);
        viewport.apply(true);

        // Set camera to centre of viewport
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        populateText();

        // Create a stage for buttons
        stage = new Stage(viewport, game.spriteBatch);
        stage.setDebugAll(Constants.DEBUG_ENABLED);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Create table to arrange buttons.
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Create actors
        storyLabel = new TypingLabel(story, skin);
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
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        // Add table to stage
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

        // Draw the button stage
        stage.act(delta);
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

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }

    private void populateText() {
        story = "{FADE}The year is 2042...\n" +
                "York has been invaded by evil extraterrestrials (ETs) from planet Kroy,{WAIT} who have set up fortresses in key locations around the city. \n" +
                "While ETs are more technologically advanced and outgun humans,{WAIT} they have a major and very convenient - weakness:\n " +
                "they evaporate when they come in contact with water.\n" +
                "As the leader of the Resistance,{WAIT} you have taken over York's old Fire Station and you are now in control of its fire engines.\n" +
                "Your mission is to use the fire engines you control to flood the ET fortresses,{WAIT}{WAIT} and liberate York.";
    }
}
