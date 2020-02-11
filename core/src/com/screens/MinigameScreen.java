package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.classes.AlienType;
import com.classes.Aliens;

import java.util.ArrayList;
import java.util.Random;

public class MinigameScreen implements Screen {

    //Declare images
    private Texture alienImage;
    private Texture waterImage;
    private Texture background;
    private Texture GImage;
    private Texture RImage;
    private Texture BImage;

    //declare blue aliens
    private Aliens Blue1;
    private Aliens Blue2;
    private Aliens Blue3;
    private Aliens Blue4;

    //declare green aliens
    private Aliens Green1;
    private Aliens Green2;
    private Aliens Green3;
    private Aliens Green4;
    private Aliens Green5;

    //declare red aliens
    private Aliens Red1;
    private Aliens Red2;
    private Aliens Red3;

    private Random random;

    //Declare score items
    private int score;
    private String scoreName;
    private BitmapFont bitmapFontName;

    //declare camera items
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //declare green rectangles
    private Rectangle square1;
    private Rectangle square2;
    private Rectangle square3;
    private Rectangle square4;
    private Rectangle square5;

    //declare red rectangles
    private Rectangle circle1;
    private Rectangle circle2;
    private Rectangle circle3;

    //declare blue rectangles
    private Rectangle star1;
    private Rectangle star2;
    private Rectangle star3;
    private Rectangle star4;


    private Rectangle water;

    private long timing;
    private boolean timeSleep;
    private ArrayList<Rectangle> ET;

    public MinigameScreen(){
       //load images for sprites
       waterImage = new Texture(Gdx.files.internal("Minigame/splashcircle.png"));
       background = new Texture(Gdx.files.internal("Minigame/tempbackground.png"));

       GImage = new Texture(Gdx.files.internal("Minigame/aliensquare.png"));
       RImage = new Texture(Gdx.files.internal("Minigame/redalien.png"));
       BImage = new Texture(Gdx.files.internal("Minigame/bluealien.png"));

       //alien creation
       ET = new ArrayList<Rectangle>();

       //delay creation
       timeSleep = false;

       //create aliens
       Green1 = new Aliens(AlienType.green, 225, 700, AlienType.green.getName());
       Green2 = new Aliens(AlienType.green, 195, 390, AlienType.green.getName());
       Green3 = new Aliens(AlienType.green, 650, 340, AlienType.green.getName());
       Green4 = new Aliens(AlienType.green, 850, 550, AlienType.green.getName());
       Green5 = new Aliens(AlienType.green, 1050, 365, AlienType.green.getName());

       Red1 = new Aliens(AlienType.red, 1000, 100, AlienType.red.getName());
       Red2 = new Aliens(AlienType.red, 590, 600, AlienType.red.getName());
       Red3 = new Aliens(AlienType.red, 900, 650, AlienType.red.getName());

       Blue1 = new Aliens(AlienType.blue, 850, 500, AlienType.blue.getName());
       Blue2 = new Aliens(AlienType.blue, 300, 200, AlienType.blue.getName());
       Blue3 = new Aliens(AlienType.blue, 750, 890, AlienType.blue.getName());
       Blue4 = new Aliens(AlienType.blue, 900, 550, AlienType.blue.getName());

       //create alien rectangles
       square1 = new Rectangle();
       square1.x= Green1.getxPos();
       square1.y= Green1.getyPos();
       square1.height= Green1.getHeight();
       square1.width= Green1.getWidth();

       square2 = new Rectangle();
       square2.x= Green2.getxPos();
       square2.y= Green2.getyPos();
       square2.height= Green2.getHeight();
       square2.width= Green2.getWidth();

       square3 = new Rectangle();
       square3.x= Green3.getxPos();
       square3.y= Green3.getyPos();
       square3.height= Green3.getHeight();
       square3.width= Green3.getWidth();

       square4 = new Rectangle();
       square4.x= Green4.getxPos();
       square4.y= Green4.getyPos();
       square4.height= Green4.getHeight();
       square4.width= Green4.getWidth();

       square5 = new Rectangle();
       square5.x= Green5.getxPos();
       square5.y= Green5.getyPos();
       square5.height= Green5.getHeight();
       square5.width= Green5.getWidth();

       circle1 = new Rectangle();
       circle1.x= Red1.getxPos();
       circle1.y= Red1.getyPos();
       circle1.height= Red1.getHeight();
       circle1.width = Red1.getWidth();

       circle2 = new Rectangle();
       circle2.x= Red2.getxPos();
       circle2.y= Red2.getyPos();
       circle2.height= Red2.getHeight();
       circle2.width = Red2.getWidth();

       circle3 = new Rectangle();
       circle3.x= Red3.getxPos();
       circle3.y= Red3.getyPos();
       circle3.height= Red3.getHeight();
       circle3.width = Red3.getWidth();

       star1=new Rectangle();
       star1.x= Blue1.getxPos();
       star1.y= Blue1.getyPos();
       star1.height= Blue1.getHeight();
       star1.width= Blue1.getWidth();

       star2=new Rectangle();
       star2.x= Blue2.getxPos();
       star2.y= Blue2.getyPos();
       star2.height= Blue2.getHeight();
       star2.width= Blue2.getWidth();

       star3=new Rectangle();
       star3.x= Blue3.getxPos();
       star3.y= Blue3.getyPos();
       star3.height= Blue3.getHeight();
       star3.width= Blue3.getWidth();

       star4=new Rectangle();
       star4.x= Blue4.getxPos();
       star4.y= Blue4.getyPos();
       star4.height= Blue4.getHeight();
       star4.width= Blue4.getWidth();

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

    }
}
