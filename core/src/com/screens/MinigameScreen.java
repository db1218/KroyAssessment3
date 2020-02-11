package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.classes.AlienType;
import com.classes.Aliens;
import com.kroy.Kroy;

import java.util.ArrayList;
import java.util.Random;

public class MinigameScreen implements Screen {

    //Declare images
    private Texture waterImage;
    private Texture background;
    private Texture GImage;
    private Texture RImage;
    private Texture BImage;

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

    private ArrayList<Aliens> ETs;
    private ArrayList<Aliens> activeETs;


    public MinigameScreen(Kroy game){
       //load images for sprites
       waterImage = new Texture(Gdx.files.internal("Minigame/splashcircle.png"));
       background = new Texture(Gdx.files.internal("Minigame/tempbackground.png"));

       GImage = new Texture(Gdx.files.internal("Minigame/aliensquare.png"));
       RImage = new Texture(Gdx.files.internal("Minigame/redalien.png"));
       BImage = new Texture(Gdx.files.internal("Minigame/bluealien.png"));

       //alien creation
       ETs = new ArrayList<Aliens>();
       activeETs = new ArrayList<Aliens>();

       //delay creation
       timeSleep = false;

       //create aliens
       ETs.add(new Aliens(GImage, AlienType.green, 225, 700, AlienType.green.getName()));
       ETs.add(new Aliens(GImage, AlienType.green, 195, 390, AlienType.green.getName()));
       ETs.add(new Aliens(GImage, AlienType.green, 650, 340, AlienType.green.getName()));
       ETs.add(new Aliens(GImage, AlienType.green, 850, 550, AlienType.green.getName()));
       ETs.add(new Aliens(GImage, AlienType.green, 1050, 365, AlienType.green.getName()));

       ETs.add(new Aliens(RImage, AlienType.red, 1000, 100, AlienType.red.getName()));
       ETs.add(new Aliens(RImage, AlienType.red, 590, 600, AlienType.red.getName()));
       ETs.add(new Aliens(RImage, AlienType.red, 900, 650, AlienType.red.getName()));

       ETs.add(new Aliens(BImage, AlienType.blue, 850, 500, AlienType.blue.getName()));
       ETs.add(new Aliens(BImage, AlienType.blue, 300, 200, AlienType.blue.getName()));
       ETs.add(new Aliens(BImage, AlienType.blue, 750, 890, AlienType.blue.getName()));
       ETs.add(new Aliens(BImage, AlienType.blue, 900, 550, AlienType.blue.getName()));

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

    private void spawnAlien(int x){
        System.out.println(x);
        activeETs.add(ETs.get(x));
        timing = TimeUtils.millis();
        }

    private boolean time(boolean times) {
        if (TimeUtils.millis() - timing > (random.nextInt(6000))) {
            times = true;
        }
        return times;
    }

    @Override
    public void show() {}

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
        for (Aliens alien : ETs) {
            batch.draw(alien.getTexture(), alien.getxPos(), alien.getyPos());
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
            spawnAlien(MathUtils.random (ETs.size()));
        }

        for (int i=0; i<ETs.size(); i++){
            Aliens alien = ETs.get(i);

            if (time(timeSleep)) {
                ETs.remove(alien);
                timeSleep = false;
            }
            if (water.overlaps(alien.getBoundingRectangle())) {
                score += alien.type.getScore();
                ETs.remove(alien);
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
        GImage.dispose();
        RImage.dispose();
        BImage.dispose();
        bitmapFontName.dispose();
        batch.dispose();

    }
}
