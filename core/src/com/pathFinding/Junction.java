package com.pathFinding;

/* =================================================================
 *                  New class added for assessment 3
 *  ===============================================================*/

public class Junction {
    final float x;
    final float y;
    final String name;

    int index;

    /** Constructor for Junction
     *
     * @param x     The x position of the junction in pixels
     * @param y     The y position of the junction in pixels
     * @param name  Descriptor of junction location on the map
     *              - used only for help debugging
     */
    public Junction (float x, float y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }


    public void setIndex(int index){
        this.index = index;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public String getName(){
        return this.name;
    }

    public int getIndex(){
        return this.index;
    }


}
