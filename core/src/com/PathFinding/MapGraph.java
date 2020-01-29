package com.PathFinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class MapGraph implements IndexedGraph<Junction> {

    MapHeuristic mapHeuristic = new MapHeuristic();
    Array<Junction> junctions = new Array<>();
    Array<Road> roads = new Array<>();

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
            cityMap.put(fromJunction, new Array<Connection<Junction>>());
        }
        cityMap.get(fromJunction).add(road);
        roads.add(road);
    }

    public GraphPath<Junction> findPath(Junction startJunction, Junction goalJunction){
        GraphPath<Junction> junctionPath = new DefaultGraphPath<>();
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

    public Junction getJunction(int n) {
        return junctions.get(n);
    }

    public Array<Junction> getJunctions(){
        return this.junctions;
    }
}
