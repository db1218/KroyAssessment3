package com.PathFinding;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;

public class GeneratePathGame extends ApplicationAdapter {
    MapGraph cityGraph;
    GraphPath<Junction> cityPath;

    public GeneratePathGame(){
        cityGraph = new MapGraph();

        Junction startJunction = new Junction(300,25, "S");
        Junction bCity = new Junction(300, 350, "B");
        Junction aCity = new Junction(200, 350, "A");
        Junction cCity = new Junction(400, 350, "C");
        Junction dCity = new Junction(200, 250, "D");
        Junction fCity = new Junction(100, 250, "F");
        Junction eCity = new Junction(400, 250, "E");
        Junction hCity = new Junction(300, 150, "H");
        Junction gCity = new Junction(200, 150, "G");
        Junction iCity = new Junction(200, 50, "I");
        Junction jCity = new Junction(300, 50, "J");
        Junction kCity = new Junction(400, 50, "K");
        Junction goalCity = new Junction(400, 150, "Z");

        cityGraph.addJunction(startJunction);
        cityGraph.addJunction(bCity);
        cityGraph.addJunction(aCity);
        cityGraph.addJunction(cCity);
        cityGraph.addJunction(dCity);
        cityGraph.addJunction(fCity);
        cityGraph.addJunction(eCity);
        cityGraph.addJunction(hCity);
        cityGraph.addJunction(gCity);
        cityGraph.addJunction(iCity);
        cityGraph.addJunction(jCity);
        cityGraph.addJunction(kCity);
        cityGraph.addJunction(goalCity);

        cityGraph.connectJunctions(startJunction, bCity);
        cityGraph.connectJunctions(bCity, aCity);
        cityGraph.connectJunctions(bCity, cCity);
        cityGraph.connectJunctions(startJunction, dCity);
        cityGraph.connectJunctions(dCity, fCity);
        cityGraph.connectJunctions(startJunction, hCity);
        cityGraph.connectJunctions(hCity, gCity);
        cityGraph.connectJunctions(gCity, iCity);
        cityGraph.connectJunctions(iCity, jCity);
        cityGraph.connectJunctions(jCity, kCity);
        cityGraph.connectJunctions(kCity, goalCity);
        cityGraph.connectJunctions(startJunction, eCity);
     //   cityGraph.connectJunctions(eCity, goalCity);

        cityPath = cityGraph.findPath(startJunction, goalCity);
    }

    public void output(){
        for(Road road: cityGraph.roads){
         //   Gdx.app.log("road", String.valueOf(road));
        }

        for (Junction junction : cityGraph.junctions){
            System.out.print(junction);
        }

        for (Junction junction : cityPath){
            Gdx.app.log("junction", String.valueOf(junction.name));
        }
    }



}
