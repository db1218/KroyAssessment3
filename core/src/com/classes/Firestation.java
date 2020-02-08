package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
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

    private ArrayList<Firetruck> parkedFireTrucks;
    private Firetruck activeFireTruck;

    private Texture destroyed;

    private boolean isMenuOpen;
    private boolean isDestroyed;


    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     *
     * @param texture  The texture used to draw the Firestation with.
     * @param xPos     The x-coordinate for the Firestation.
     * @param yPos     The y-coordinate for the Firestation.
     */
    public Firestation(Texture texture, Texture destroyedTexture, float xPos, float yPos) {
        super(texture);
        this.destroyed = destroyedTexture;
        this.setPosition(xPos, yPos);
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
        this.repairRange = new Circle(this.getCentreX(), this.getCentreY(), this.getWidth());
        this.parkedFireTrucks = new ArrayList<>();
        this.isDestroyed = false;
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     * Also reduces the time before next repair can occur.
     *
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        if (this.isDestroyed) {
            this.removeSprite(this.destroyed);
        }
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

    /**
     * Updates the active fire truck, check if the
     * fire truck's health is 0 or below, if so
     * "destroy" it
     *
     * @param batch         to draw textures
     * @param shapeRenderer to draw health and debug
     * @param camera        eventually to control active firetruck hose
     */
    public void updateFiretruck(Batch batch, ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        this.activeFireTruck.update(batch, camera);
        if (DEBUG_ENABLED) this.activeFireTruck.drawDebug(shapeRenderer);
        if (this.activeFireTruck.getHealthBar().getCurrentAmount() <= 0) {
            this.activeFireTruck.destroy();
            if (getAliveFiretruckID() != -1) {
                changeFiretruck(getAliveFiretruckID());
                this.toggleMenu(true);
            }
        }

    }

    /**
     * Updates the arrow that points to the nearest fortress
     *
     * @param shapeRenderer to draw the arrow polygon
     * @param fortresses    to find nearest fortress
     */
    public void updateActiveArrow(ShapeRenderer shapeRenderer, ArrayList<ETFortress> fortresses) {
        this.activeFireTruck.updateArrow(shapeRenderer, fortresses);
    }

    /**
     * Respawn the active fire truck
     */
    private void respawnFiretruck() {
        this.activeFireTruck.respawn();
    }

    /**
     * Whether the fire station has any more active fire trucks
     *
     * @return  <code>true</code> if there is a parked fire truck
     *          <code>false</code> otherwise
     */
    // ==============================================================
    //					Added for assessment 3
    // ==============================================================
    public boolean hasParkedFiretrucks() {
        return getAliveFiretruckID() >= 0;
    }

    /**
     * Add fire truck to parked fire truck list
     *
     * @param firetruck to park
     */
    public void parkFireTruck(Firetruck firetruck) {
            this.parkedFireTrucks.add(firetruck);
    }

    /**
     * Repairs/Refills the fire truck if:
     * - it is within 3 minutes
     * - in the main carpark (fire station)
     * - less than 100 health or water
     *
     * @param time          used to check whether the station
     *                      has been destroyed yet
     * @param includeActive <code>true</code> if car park menu
     *                      if open, repair active fire truck
     *                      and can see health/water increasing
     *                      <code>fasle</code> menu is closed
     */
    public void checkRepairRefill(int time, boolean includeActive) {
        if (includeActive) {
            if (time > 0 && activeFireTruck.getCarpark().name().contains("Main") && (activeFireTruck.isDamaged() || activeFireTruck.isLowOnWater())) {
                this.repairRefill(activeFireTruck);
            }
        }
        for (Firetruck firetruck : parkedFireTrucks) {
            if (time > 0 && firetruck.getCarpark().name().contains("Main") && (firetruck.isDamaged() || firetruck.isLowOnWater())) {
                this.repairRefill(firetruck);
            }
        }
        if (time == 0) {
            this.isDestroyed = true;
        }
    }

    /**
     * Open or close the car park menu
     *
     * @param isOpen    <code>true</code> if menu is just being opened
     *                  <code>false</code> menu is being closed
     */
    public void toggleMenu(boolean isOpen) {
        this.isMenuOpen = isOpen;
        if (this.activeFireTruck.isSpraying() && isOpen) this.activeFireTruck.toggleHose();
        if (!isOpen) respawnFiretruck();
    }

    /**
     * Returns the id of a parked fire truck,
     * called when a fire truck dies and a new one is selected
     *
     * @return  <code>-1</code> no id
     */
    // ==============================================================
    //					Modified for assessment 3
    // ==============================================================
    private int getAliveFiretruckID() {
        for (int i=0; i < parkedFireTrucks.size(); i++) {
            if (parkedFireTrucks.get(i).isAlive() && parkedFireTrucks.get(i).isBought()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Swap active fire truck with indexed parked fire truck
     *
     * @param index of parked fire truck
     */
    // ==============================================================
    //					Added for assessment 3
    // ==============================================================
    public void changeFiretruck(int index) {
        Firetruck previous = activeFireTruck;
        activeFireTruck = parkedFireTrucks.get(index);
        parkedFireTrucks.remove(index);
        parkedFireTrucks.add(index, previous);
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

    public boolean isMenuOpen() {
        return this.isMenuOpen;
    }

}