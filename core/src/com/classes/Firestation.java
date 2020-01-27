package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
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

    // Private values for this class to use
    private Circle repairRange;

    private Firetruck activeFireTruck;
    private Vector2 spawnLocation;

    private ArrayList<Firetruck> parkedFireTrucks;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture  The texture used to draw the Firestation with.
     * @param xPos     The x-coordinate for the Firestation.
     * @param yPos     The y-coordinate for the Firestation.
     */
    public Firestation(Texture texture, float xPos, float yPos) {
        super(texture);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Sets the health of the Firestation and its size provided in CONSTANTS.
     * Also creates a circle to indicate the range firetrucks should be within
     * in order to be repaired by the firestation.
     */
    private void create() {
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
        this.repairRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth());
        this.parkedFireTrucks = new ArrayList<>();
        this.spawnLocation = new Vector2(80 * TILE_DIMS, 24.5f * TILE_DIMS);
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
        if (this.getInternalTime() % 10 == 0) {
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

    public void updateFiretrucks(Batch batch, ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        System.out.println(this.parkedFireTrucks.get(0).isAlive());
        this.activeFireTruck.update(batch, camera);
        if (DEBUG_ENABLED) this.activeFireTruck.drawDebug(shapeRenderer);
        if (this.activeFireTruck.getHealthBar().getCurrentAmount() <= 0) {
            this.activeFireTruck.destroyed();
            this.openCarparkMenu();
        }
    }

    public void openCarparkMenu() {
        if (this.hasParkedFiretrucks()) {
            Firetruck firetruckToSpawn = this.parkedFireTrucks.get(0);
            this.parkedFireTrucks.remove(0);
            this.parkedFireTrucks.add(this.activeFireTruck);
            this.activeFireTruck = firetruckToSpawn;
            respawnFiretruck();
        }
    }

    private void respawnFiretruck() {
        this.activeFireTruck.setPosition(spawnLocation.x, spawnLocation.y);
        this.activeFireTruck.resetRotation();
        this.activeFireTruck.setSpeed(new Vector2(0, 0));
    }

    public boolean hasParkedFiretrucks() {
        for (Firetruck firetruck : this.parkedFireTrucks) {
            if (firetruck.isAlive()) {
                return true;
            }
        }
        return false;
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

    public void checkRepairRefill(int time) {
        for (Firetruck firetruck : parkedFireTrucks) {
            if (time > 0 && (firetruck.isDamaged() || firetruck.isLowOnWater())) {
                this.repairRefill(firetruck);
            }
        }
    }
}