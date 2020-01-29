package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.kroy.Kroy;
import com.screens.CarparkScreen;
import com.screens.GameScreen;
import com.screens.MainMenuScreen;
import com.sprites.SimpleSprite;

// Constants import
import java.util.ArrayList;

import static com.config.Constants.*;

/**
 * The Firestation implementation, a static sprite in the game.
 *
 * @author Archie
 * @since 17/12/2019
 */
public class Firestation extends SimpleSprite {

    private Kroy game;

    // Private values for this class to use
    private Circle repairRange;

    private Firetruck activeFireTruck;
    private Vector2 spawnLocation;

    private CarparkScreen carparkScreen;
    private boolean isMenuOpen;

    private ArrayList<Firetruck> parkedFireTrucks;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     *
     * @param texture  The texture used to draw the Firestation with.
     * @param xPos     The x-coordinate for the Firestation.
     * @param yPos     The y-coordinate for the Firestation.
     */
    public Firestation(Texture texture, float xPos, float yPos, Kroy game, GameScreen gameScreen) {
        super(texture);
        this.setPosition(xPos, yPos);
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
        this.repairRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth());
        this.parkedFireTrucks = new ArrayList<>();
        this.spawnLocation = new Vector2(80 * TILE_DIMS, 24.5f * TILE_DIMS);
        this.carparkScreen = new CarparkScreen(this, game, gameScreen);
        this.game = game;
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     * Also reduces the time before next repair can occur.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        this.repairRange.setPosition(this.getCentreX(), this.getCentreY());
    }

    /**
     * Repair a firetruck over time.
     *
     * @param firetruck  The firetruck that will be repaired.
     */
    public void repairRefill(Firetruck firetruck) {
        System.out.println(this.getInternalTime());
        if (this.getInternalTime() % 10 == 0) {
         //   System.out.println("refill! =================");
         //   System.out.println(this.getInternalTime());
            firetruck.getHealthBar().addResourceAmount((int) firetruck.getHealthBar().getMaxAmount() / FIRETRUCK_REPAIR_SPEED);
            firetruck.getWaterBar().addResourceAmount((int) firetruck.getWaterBar().getMaxAmount() / FIRETRUCK_REPAIR_SPEED);
        }
    }

    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the range circle.
     *
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
    }

    public void updateFiretruck(Batch batch, ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        this.activeFireTruck.update(batch, camera);
        if (DEBUG_ENABLED) this.activeFireTruck.drawDebug(shapeRenderer);
        if (this.activeFireTruck.getHealthBar().getCurrentAmount() <= 0) {
            this.activeFireTruck.destroyed();
            if (getAliveFiretruckID() == -1) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            } else {
                changeFiretruck(getAliveFiretruckID());
                this.openMenu(true);
            }
        }
    }

    public CarparkScreen getCarparkScreen() {
        return this.carparkScreen;
    }

    private void respawnFiretruck() {
        this.activeFireTruck.setPosition(spawnLocation.x, spawnLocation.y);
        this.activeFireTruck.resetRotation();
        this.activeFireTruck.setSpeed(new Vector2(0, 0));
    }

    public boolean hasParkedFiretrucks() {
        return getAliveFiretruckID() >= 0;
    }

    public void setActiveFireTruck(Firetruck fireTruck) {
        this.activeFireTruck = fireTruck;
    }

    public Firetruck getActiveFireTruck() {
        return this.activeFireTruck;
    }

    public ArrayList<Firetruck> getParkedFireTrucks() {
        return this.parkedFireTrucks;
    }

    public void parkFireTruck(Firetruck firetruck) {
        this.parkedFireTrucks.add(firetruck);
    }

    public Vector2 getSpawnLocation() {
        return this.spawnLocation;
    }

    public void checkRepairRefill(int time, boolean includeActive) {
        if (includeActive) {
            if (time > 0 && (activeFireTruck.isDamaged() || activeFireTruck.isLowOnWater())) {
                this.repairRefill(activeFireTruck);
            }
        }
        for (Firetruck firetruck : parkedFireTrucks) {
            if (time > 0 && (firetruck.isDamaged() || firetruck.isLowOnWater())) {
                this.repairRefill(firetruck);
            }
        }
    }

    public boolean isMenuOpen() {
        return this.isMenuOpen;
    }

    public void openMenu(boolean isOpen) {
        this.isMenuOpen = isOpen;
        if (!isOpen) respawnFiretruck();
    }

    private int getAliveFiretruckID() {
        for (int i=0; i < parkedFireTrucks.size(); i++) {
            if (parkedFireTrucks.get(i).isAlive()) {
                return i;
            }
        }
        return -1;
    }

    public void changeFiretruck(int index) {
        Firetruck previous = activeFireTruck;
        activeFireTruck = parkedFireTrucks.get(index);
        parkedFireTrucks.remove(index);
        parkedFireTrucks.add(index, previous);
    }
}