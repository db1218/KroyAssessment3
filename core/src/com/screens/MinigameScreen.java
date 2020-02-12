package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.classes.AlienType;
import com.classes.Alien;
import com.kroy.Kroy;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class MinigameScreen implements Screen {

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

    private long timing;
    private boolean timeSleep;

    private ArrayList<Alien> onScreenETs;
    private ArrayList<Vector2> ETLocations;

    private TreeMap<Double, AlienType> map;

    private MiniGameInputHandler miniGameInputHandler;
    private boolean playerHasClicked;

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

        game.coolFont.draw(game.spriteBatch, "Score: " + score, 25, 100);

        game.spriteBatch.end();

        if (TimeUtils.millis() - timing > 700) {
            spawnAlien();
        }

        for (int i = 0; i < onScreenETs.size(); i++) {
            Alien alien = onScreenETs.get(i);

            if (time(timeSleep)) {
                onScreenETs.remove(alien);
                timeSleep = false;
            }
            if (playerHasClicked && water.overlaps(alien.getBoundingRectangle())) {
                score += alien.getScore();
                onScreenETs.remove(alien);
            }
        }

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

    private boolean time(boolean times) {
        if (TimeUtils.millis() - timing > (random.nextInt(6000))) {
            times = true;
        }
        return times;
    }

    private void drawWater() {
        if (this.playerHasClicked) {
            game.spriteBatch.draw(waterImage, water.x - (waterImage.getWidth()/2f), water.y - (waterImage.getHeight()/2f));
        }
    }

    private void spawnAlien() {
        AlienType randomType = generateType();
        Vector2 randomLocation = generateLocation();
        onScreenETs.add(new Alien(randomType, randomLocation));
        timing = TimeUtils.millis();
    }

    // Generates a random number between 0.0 and 1.0 then rounds up
    // to the nearest value in the map - this allows a random patrol
    // type to be selected but has a higher probability of choosing
    // lower scoring patrols over higher scoring patrols
    private AlienType generateType() {
        double randomIndex = random.nextDouble();
        AlienType alien = map.ceilingEntry(randomIndex).getValue();
        return alien;
    }

    private Vector2 generateLocation(){
        int randomIndex = random.nextInt(ETLocations.size() - 1);
        return ETLocations.get(randomIndex);
    }

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

    public void setPlayerHasClicked(Boolean b){
        this.playerHasClicked = b;
    }

    public void setTouch(int x, int y) {
        water.setPosition(x, y);
    }

    public OrthographicCamera getCamera() { return camera; }

    public void toGameScreen() {
        this.game.setScreen(this.gameScreen);
    }
}
