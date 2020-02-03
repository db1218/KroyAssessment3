package com.PathFinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.classes.Patrols;
import com.sprites.PatrolMovementSprite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MapGraph implements IndexedGraph<Junction> {

    MapHeuristic mapHeuristic = new MapHeuristic();
    Array<Junction> junctions = new Array<>();
    Array<Road> roads = new Array<>();
    HashMap<Road, PatrolMovementSprite> lockedRoads = new HashMap<>();

    ObjectMap<Junction, Array<Connection<Junction>>> cityMap = new ObjectMap<>();

    private int lastNodeIndex = 0;

    public void addJunction(Junction junction){
        junction.index = lastNodeIndex;
        lastNodeIndex++;
        junctions.add(junction);
    }

    public void connectJunctions(Junction fromJunction, Junction toJunction){
        Road road = new Road(fromJunction, toJunction);
        if (!cityMap.containsKey(fromJunction)){
            cityMap.put(fromJunction, new Array<>());
        }
        cityMap.get(fromJunction).add(road);
        roads.add(road);
    }

    public GraphPath<Junction> findPath(Junction startJunction, Junction goalJunction){
        GraphPath<Junction> junctionPath = new DefaultGraphPath<Junction>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startJunction, goalJunction, mapHeuristic, junctionPath);
        return junctionPath;
    }

    @Override
    public int getIndex(Junction node) {
        return node.index;
    }

    @Override
    public int getNodeCount() {
        return lastNodeIndex;
    }

    @Override
    public Array<Connection<Junction>> getConnections(Junction fromNode) {
        if(cityMap.containsKey(fromNode)){
            return cityMap.get(fromNode);
        }
        return new Array<>(0);
    }

    public Junction getANewJunction(Junction fromNode){
        if (cityMap.containsKey(fromNode)){
            Random rand = new Random();
            int r = rand.nextInt(cityMap.get(fromNode).size);
            return (Junction) cityMap.get(fromNode).get(r);
        }
        return fromNode;
    }

    public void setTravelled(Junction fromJunction, Junction toJunction){
        Gdx.app.log("set travelled", "setting");
        for (Road road : roads){
            if (road.getFromNode() == fromJunction && road.getToNode() == toJunction){
            //    Gdx.app.log("road travelled", String.valueOf(road));
            //    Gdx.app.log("road from", String.valueOf(road.fromJunction.getName()));
            //    Gdx.app.log("road to", String.valueOf(road.toJunction.getName()));
                road.setTravelled(true);
              //  Gdx.app.log("road travelled check", String.valueOf(road.isTravelled()));
              //  Gdx.app.log("road travelled check2", String.valueOf(road.isTravelled));
        //    } else {
          //      road.setTravelled(false);
            }
        }

    }

    public Road hi(Junction fromJunction, Junction toJunction){
     //   Gdx.app.log("checking", "check");
        for (Road road : roads){
            if (road.fromJunction == fromJunction && road.toJunction == toJunction){
               // Gdx.app.log("match", "match");
                return road;
            }
        }
      //  Gdx.app.log("new road", "road new");
      //  Gdx.app.log("fromJunctio", String.valueOf(fromJunction.getName()));
      //  Gdx.app.log("toJunctio", String.valueOf(toJunction.getName()));
        return new Road(fromJunction, toJunction);
    }

    public boolean isTravelled(Junction fromJunction, Junction toJunction){
        Road t = hi(fromJunction, toJunction);
        return t.isTravelled();
    }

    public Array<Junction> getJunctions(){
        return this.junctions;
    }

    public Array<Road> getRoads() {return this.roads; }

    public void unsetTravelled(Junction fromJunction, Junction toJunction) {
        for (Road road : roads) {
            if (road.getFromNode() == fromJunction && road.getToNode() == toJunction) {
                road.setTravelled(false);
            }
        }
    }

    public void unsetTravelled(Road setRoad) {
        Gdx.app.log("unset road", String.valueOf(setRoad));
        setRoad.setTravelled(false);
    }

    public void removeDead(PatrolMovementSprite patrol){
        Iterator iter = lockedRoads.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            if (patrol.equals(entry.getValue())){
                iter.remove();
            }
        }
    }



    public void lockedRoads(Road road, PatrolMovementSprite patrol) {
        Gdx.app.log("locked", "lock");
        if (!lockedRoads.containsKey(road)) {
            Gdx.app.log("successful", "suc");
            lockedRoads.put(road, patrol);
        }
    }

    public boolean isRoadLocked(Junction from, Junction to){
        Road checkRoad = hi(from, to);
        if (lockedRoads.containsKey(checkRoad)){
            return true;
        }
        return false;
    }

    public void unlockRoad(Road road, PatrolMovementSprite patrol) {
        Gdx.app.log("unlock", "unlock");
        Gdx.app.log("Patrol", String.valueOf(patrol));
        Gdx.app.log("hascode",String.valueOf(patrol.hashCode()));
        if (lockedRoads.containsKey(road)){
            if (lockedRoads.get(road) == patrol) {
                Gdx.app.log("hascode rquired", String.valueOf(lockedRoads.get(road).hashCode()));
                Gdx.app.log("successful", "success");
                Gdx.app.log("required", String.valueOf(lockedRoads.get(road)));
                Gdx.app.log("successful by patrol", String.valueOf(patrol));
                lockedRoads.remove(road);
            }
        }
    }

}
