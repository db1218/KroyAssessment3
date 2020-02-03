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


    public Road hi(Junction fromJunction, Junction toJunction){
        for (Road road : roads){
            if (road.fromJunction == fromJunction && road.toJunction == toJunction){
                return road;
            }
        }
        return new Road(fromJunction, toJunction);
    }

    public boolean isRoadLocked(Junction from, Junction to) {
        Road checkRoad = hi(from, to);
        Road otherRoad = hi(to, from);

        if (lockedRoads.containsKey(checkRoad) || lockedRoads.containsKey(otherRoad)) {
            return true;
        }
        return false;

    }

    public void lockRoads(Road road, PatrolMovementSprite patrol) {
        if (!lockedRoads.containsKey(road)) {
            lockedRoads.put(road, patrol);
        }
    }

    public void unlockRoad(Road road, PatrolMovementSprite patrol) {
        if (lockedRoads.containsKey(road)){
            if (lockedRoads.get(road) == patrol) {
                lockedRoads.remove(road);
            }
        }
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


    public Array<Junction> getJunctions(){
        return this.junctions;
    }

    public Array<Road> getRoads() {return this.roads; }

}
