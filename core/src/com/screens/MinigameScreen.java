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

    private Kroy game;

    //Declare images
    private Texture waterImage;
    private Texture background;

    private Random random;

    //Declare score items
    private int score;
    private String scoreName;
    private BitmapFont bitmapFontName;

    //declare camera items
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Rectangle water;

    private long timing;
    private boolean timeSleep;

    private ArrayList<Alien> onScreenETs;
    private ArrayList<Vector2> ETLocations;

    private ArrayList<AlienType> typeOfAliens;
    private ArrayList<Double> chanceOfSelectingAlien;

    private TreeMap<Double, AlienType> map;

    public MinigameScreen(Kroy game) {

        this.game = game;

        //load images for sprites
        waterImage = new Texture(Gdx.files.internal("Minigame/splashcircle.png"));
        background = new Texture(Gdx.files.internal("Minigame/tempbackground.png"));

        //alien creation
        onScreenETs = new ArrayList<Alien>();
        ETLocations = new ArrayList<Vector2>();
        generateETLocations();

        typeOfAliens = new ArrayList<>();
        typeOfAliens.add(AlienType.blue);
        typeOfAliens.add(AlienType.green);
        typeOfAliens.add(AlienType.red);

        chanceOfSelectingAlien = new ArrayList<>();
        for (AlienType type : typeOfAliens){
            chanceOfSelectingAlien.add(type.getChance());
        }

        //delay creation
        timeSleep = false;


        // Creates a map of types of aliens and their chance of being selected
        map = new TreeMap<>();
        double total = 0.0d;
        for (int i = 0; i < typeOfAliens.size(); i++) {
            map.put(total += chanceOfSelectingAlien.get(i), typeOfAliens.get(i));
        }

        batch = new SpriteBatch();

        //initialise score to 0
        score = 0;
        scoreName = "Score: 0";
        bitmapFontName = new BitmapFont();

        //create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 960);

        //create rectangles
        batch = new SpriteBatch();

        //create water rectangle to allow collision detection
        water = new Rectangle();
        water.x = 0;
        water.y = 0;
        water.width = 150;
        water.height = 150;

    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        //render screen
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        camera.update();

        //render sprites as batch inc. score
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);

        //draw aliens on screen
        for (Alien alien : onScreenETs) {
            batch.draw(alien.getTexture(), alien.getX(), alien.getY());
        }

        //renders water sprite when mouse is clicked
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX() - 50, Gdx.input.getY() + 50, 0);
            camera.unproject(touchPos);
            water.x = touchPos.x;
            water.y = touchPos.y;
            batch.draw(waterImage, water.x, water.y);
        }

        bitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFontName.draw(batch, scoreName, 25, 100);

        batch.end();

        random = new Random();
        if (TimeUtils.millis() - timing > 700) {
            spawnAlien();
        }

        for (int i = 0; i < onScreenETs.size(); i++) {
            Alien alien = onScreenETs.get(i);

            if (time(timeSleep)) {
                onScreenETs.remove(alien);
                timeSleep = false;
            }
            if (water.overlaps(alien.getBoundingRectangle())) {
                score += alien.getScore();
                onScreenETs.remove(alien);
                scoreName = "Score: " + score;
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
        bitmapFontName.dispose();
        batch.dispose();
    }

    private boolean time(boolean times) {
        if (TimeUtils.millis() - timing > (random.nextInt(6000))) {
            times = true;
        }
        return times;
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

}
