package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.entities.Alien;
import com.sprites.MinigameSprite;
import com.Kroy;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import static com.misc.Constants.*;

/**
 * MinigameScreen, as suggested, a Mini game contained within
 * the game, which allows the player to play a different style
 * of game ("whack-a-mole"), but keeping the same theme (aliens
 * and fire trucks). Players can earn score which is transferred
 * into the main game after 30 seconds
 */
public class MinigameScreen implements Screen {

    // constants to store game and game screen
    private final Kroy game;
    private final GameScreen gameScreen;

    //Declare images
    private Texture waterImage;
    private Texture background;

    private Random random;

    //Declare score items
    private int score;

    //declare camera items
    private OrthographicCamera camera;

    private Rectangle water;

    private long timeSpawn;

    private int time;

    private ArrayList<Alien> onScreenETs;
    private ArrayList<Vector2> ETLocations;

    private TreeMap<Double, AlienType> map;

    private MiniGameInputHandler miniGameInputHandler;
    private boolean canSpray;

    /**
     * Constructor for minigame screen which is called when
     * the player drives over {@link MinigameSprite} in
     * {@link GameScreen}
     *
     * @param game          to change screen and access shared batch
     * @param gameScreen    to return back to after minigame completion
     */
    public MinigameScreen(Kroy game, GameScreen gameScreen) {

        this.game = game;
        this.gameScreen = gameScreen;

        //load images for sprites
        waterImage = new Texture(Gdx.files.internal("Minigame/splashcircle.png"));
        background = new Texture(Gdx.files.internal("Minigame/tempbackground.png"));

        //alien creation
        onScreenETs = new ArrayList<Alien>();
        ETLocations = new ArrayList<Vector2>();
        generateETLocations();

        ArrayList<AlienType> typeOfAliens = new ArrayList<>();
        typeOfAliens.add(AlienType.blue);
        typeOfAliens.add(AlienType.green);
        typeOfAliens.add(AlienType.red);

        ArrayList<Double> chanceOfSelectingAlien = new ArrayList<>();
        for (AlienType type : typeOfAliens){
            chanceOfSelectingAlien.add(type.getChance());
        }

        random = new Random();

        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                time--;
            }
        }, 1, 1);

        time = MINIGAME_DURATION;

        // Creates a map of types of aliens and their chance of being selected
        map = new TreeMap<>();
        double total = 0.0d;
        for (int i = 0; i < typeOfAliens.size(); i++) {
            map.put(total += chanceOfSelectingAlien.get(i), typeOfAliens.get(i));
        }

        //initialise score to 0
        score = 0;

        //create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 960);

        // set InputHandler
        miniGameInputHandler = new MiniGameInputHandler(this);

        //create water rectangle to allow collision detection
        water = new Rectangle(0, 0, 150, 150);

        canSpray = true;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(miniGameInputHandler);
    }

    @Override
    public void render(float delta) {
        //render screen
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        camera.update();

        //render sprites as batch inc. score
        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();
        game.spriteBatch.draw(background, 0, 0);

        //draw aliens on screen
        for (Alien alien : onScreenETs) {
            game.spriteBatch.draw(alien.getTexture(), alien.getX(), alien.getY());
        }

        drawWater();

        game.coolFont.draw(game.spriteBatch, "Minigame Score: " + score, 25, 100);
        game.coolFont.draw(game.spriteBatch, "Time Remaining: " + time, 1320, 100);

        game.spriteBatch.end();

        for (int i = 0; i < onScreenETs.size(); i++) {
            Alien alien = onScreenETs.get(i);

            if (TimeUtils.millis() > alien.getSpawnTime() + alien.type.getAliveTime()) {
                onScreenETs.remove(alien);
            }

            if (water.overlaps(alien.getBoundingRectangle())) {
                score += alien.getScore();
                onScreenETs.remove(alien);
            }
        }

        if (TimeUtils.millis() > timeSpawn + MINIGAME_SPAWN_RATE) spawnAlien();

        if (time <= 0) toGameScreen();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        waterImage.dispose();
    }

    /**
     * Draws the water image if the player has clicked
     */
    private void drawWater() {
        if (canSpray) game.spriteBatch.draw(waterImage, water.x, water.y);
    }

    /**
     * Spawns an alien and resets the spawn timer
     */
    private void spawnAlien() {
        onScreenETs.add(new Alien(generateType(), selectLocation()));
        timeSpawn = TimeUtils.millis();
    }

    /**
     * Generates a random number between 0.0 and 1.0 then rounds up
     * to the nearest value in the map - this allows a random patrol
     * type to be selected but has a higher probability of choosing
     * lower scoring patrols over higher scoring patrols
     *
     * @return  type of alien
     */
    private AlienType generateType() {
        double randomIndex = random.nextDouble();
        return map.ceilingEntry(randomIndex).getValue();
    }

    /**
     * Selects a random location on the screen to
     * spawn the ET, from a list of locations
     *
     * @return  vector of random location
     */
    private Vector2 selectLocation(){
        int randomIndex = random.nextInt(ETLocations.size() - 1);
        return ETLocations.get(randomIndex);
    }

    /**
     *  Generates a list of all locations where an
     *  ET can appear
     */
    private void generateETLocations() {
        ETLocations.add(new Vector2(225, 700));
        ETLocations.add(new Vector2(195, 390));
        ETLocations.add(new Vector2(650, 390));
        ETLocations.add(new Vector2(850, 550));
        ETLocations.add(new Vector2(1050, 365));
        ETLocations.add(new Vector2(1000, 100));
        ETLocations.add(new Vector2(590, 600));
        ETLocations.add(new Vector2(900, 650));
        ETLocations.add(new Vector2(850, 500));
        ETLocations.add(new Vector2(300, 200));
        ETLocations.add(new Vector2(750, 890));
        ETLocations.add(new Vector2(900, 550));
    }

    /**
     * Goes back to the game screen once the user presses escape
     * or when the time runs out
     */
    public void toGameScreen() {
        gameScreen.setScore(gameScreen.getScore() + score);
        this.game.setScreen(this.gameScreen);
        dispose();
    }

    /**
     * Sets the position of the water object
     *
     * @param x position of water
     * @param y position of water
     */
    public void setTouch(int x, int y) {
        water.setPosition(x - (waterImage.getWidth()/2f), y - (waterImage.getHeight()/2f));
    }

    /**
     * Means that the water can spray and destroy
     * aliens in that location
     * @param b <code>true</code> if water can spray
     *          <code>false</code> otherwise
     */
    public void setCanSpray(boolean b) {
        this.canSpray = b;
    }

    public OrthographicCamera getCamera() { return camera; }

}
