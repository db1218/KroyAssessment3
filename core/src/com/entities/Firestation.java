package com.entities;

// LibGDX imports
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.screens.GameScreen;
import com.sprites.SimpleSprite;

// Constants import
import java.util.ArrayList;

import static com.misc.Constants.*;

/**
 * The Firestation implementation, a static sprite in the game.
 *
 * @author Archie
 * @since 17/12/2019
 */
public class Firestation extends SimpleSprite {

    private final GameScreen gameScreen;

    // list of fire trucks that are not the active truck
    private final ArrayList<Firetruck> parkedFireTrucks;

    // fire truck that the user is currently controlling
    private Firetruck activeFireTruck;

    // destroyed texture
    private final Texture destroyed;

    // booleans
    private boolean isMenuOpen;
    private boolean isVulnerable;
    private boolean isDestroyed;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     *
     * @param texture           The texture used to draw the Firestation with.
     * @param destroyedTexture  The destoryed texture when Firestation is destroyed.
     * @param xPos              The x-coordinate for the Firestation.
     * @param yPos              The y-coordinate for the Firestation.
     * @param gameScreen        GameScreen to be able to send popup messages to
     */
    public Firestation(Texture texture, Texture destroyedTexture, float xPos, float yPos, GameScreen gameScreen) {
        super(texture);
        this.destroyed = destroyedTexture;
        this.gameScreen = gameScreen;
        this.setPosition(xPos, yPos);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.parkedFireTrucks = new ArrayList<>();
        this.isDestroyed = false;
        this.isVulnerable = false;
        super.resetRotation(90);
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     * Also reduces the time before next repair can occur.
     *
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        if (!isDestroyed && this.getHealthBar().getCurrentAmount() <= 0) {
            this.isDestroyed = true;
            this.removeSprite(this.destroyed);
            this.gameScreen.showPopupText("The Fire Station has been destroyed! " +
                    "You can no longer repair or refill your fire trucks", 1, 7);
        }
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
        if (this.activeFireTruck.isTankEmpty() && gameScreen.getFireStationTime() % 5 == 0) {
            if (gameScreen.getFireStationTime() > 0) gameScreen.showPopupText("{FAST}This truck has run out of water, go to the Fire Station to refill", 1, 5);
            else gameScreen.showPopupText("{FAST}This truck has run out of water, select another one", 1, 5);
        }
        if (DEBUG_ENABLED) this.activeFireTruck.drawDebug(shapeRenderer);
        if (this.activeFireTruck.checkDestroyed()) {
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

    /*
     *  =======================================================================
     *                          Added for Assessment 3
     *  =======================================================================
     */
    /**
     * Whether the fire station has any more active fire trucks
     *
     * @return  <code>true</code> if there is a parked fire truck
     *          <code>false</code> otherwise
     */
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
            if (!isDestroyed && activeFireTruck.getCarpark().name().contains("Main") && (activeFireTruck.isDamaged() || activeFireTruck.isLowOnWater())) {
                this.repairRefill(activeFireTruck);
            }
        }
        for (Firetruck firetruck : parkedFireTrucks) {
            if (!isDestroyed && firetruck.getCarpark().name().contains("Main") && (firetruck.isDamaged() || firetruck.isLowOnWater())) {
                this.repairRefill(firetruck);
            }
        }
        if (!this.isVulnerable && time == 0) {
            this.isVulnerable = true;
            this.gameScreen.showPopupText("WARNING: The Fire Station is now vulnerable to attack",3, 3);
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

    /*
     *  =======================================================================
     *                          Modified for Assessment 3
     *  =======================================================================
     */
    /**
     * Returns the id of a parked fire truck,
     * called when a fire truck dies and a new one is selected
     *
     * @return  <code>-1</code> no id
     */
    private int getAliveFiretruckID() {
        for (int i=0; i < parkedFireTrucks.size(); i++) {
            if (parkedFireTrucks.get(i).isAlive() && parkedFireTrucks.get(i).isBought()) {
                return i;
            }
        }
        return -1;
    }

    /*
     *  =======================================================================
     *                          Added for Assessment 3
     *  =======================================================================
     */
    /**
     * Swap active fire truck with indexed parked fire truck
     *
     * @param index of parked fire truck
     */
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

    public boolean isVulnerable() {
        return this.isVulnerable;
    }

    public boolean isDestroyed() {
        return this.isDestroyed;
    }

}