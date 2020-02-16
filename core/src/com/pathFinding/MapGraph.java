package com.pathFinding;

/* =================================================================
                   New class added for assessment 3
   ===============================================================*/

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.sprites.PatrolMovementSprite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** This class creates a MapGraph which contains all junctions and connections
 * between them. It is also used to find a path between two junctions and keep
 * track of which roads are being travelled on by patrols
 */

public class MapGraph implements IndexedGraph<Junction> {

    // The heuristic used to determine which path to take from
    // start to end goal
    final MapHeuristic mapHeuristic = new MapHeuristic();

    // Array of junctions in mapGraph
    final Array<Junction> junctions = new Array<>();

    // Array of roads in mapGraph
    final Array<Road> roads = new Array<>();

    // A hashmap containing the roads which are currently being travelled on
    // the PatrolMovementSprite that is travelling on that road
    final HashMap<Road, PatrolMovementSprite> lockedRoads = new HashMap<>();

    // A map containing the
    final ObjectMap<Junction, Array<Connection<Junction>>> connectionsFromJunctionMap = new ObjectMap<>();

    private int lastNodeIndex = 0;

    /** This finds the path from the startJunction to the goalJunction
     *
     * @param startJunction starting junction
     * @param goalJunction  finishing junction
     * @return              path of junctions needed to get from the startJunction to
     *                      the goalJunction
    */
    public GraphPath<Junction> findPath(Junction startJunction, Junction goalJunction){
        GraphPath<Junction> junctionPath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startJunction, goalJunction, mapHeuristic, junctionPath);
        return junctionPath;
    }

    /**
     * Gets road between two junction
     * @param fromJunction  first junction
     * @param toJunction    second junction
     * @return              returns a road going from fromJunction to toJunction
     */
    public Road getRoad(Junction fromJunction, Junction toJunction) {
        for (Road road : roads) {
            if (road.fromJunction == fromJunction && road.toJunction == toJunction) {
                return road;
            }
        }
        return new Road(fromJunction, toJunction);
    }

    /**
     * Adds a junction to junctions
     * @param junction  junction to add
     */
    public void addJunction(Junction junction){
        junction.index = lastNodeIndex;
        lastNodeIndex++;
        junctions.add(junction);
    }

    /** This creates a road from fromJunction to toJunction and adds it to roads.
     * It also adds this road to connectionsFromJunctionMap which keeps track of
     * which connections you can take from each junction
     * @param fromJunction  one end of the road - the end the patrol starts at
     * @param toJunction    the other end of the road - the end the patrol ends at
     */
    public void connectJunctions(Junction fromJunction, Junction toJunction){
        Road road = new Road(fromJunction, toJunction);
        if (!connectionsFromJunctionMap.containsKey(fromJunction)){
            connectionsFromJunctionMap.put(fromJunction, new Array<>());
        }
        connectionsFromJunctionMap.get(fromJunction).add(road);
        roads.add(road);
    }

    /** This returns the connections you can take from the fromNode
     *
     * @param fromNode   The node you want to find possible connections from
     * @return Array     An array of the connections you could take from fromNode
     *                   - returns an empty array if there are no connections
     */
    @Override
    public Array<Connection<Junction>> getConnections(Junction fromNode) {
        if(connectionsFromJunctionMap.containsKey(fromNode)){
            return connectionsFromJunctionMap.get(fromNode);
        }
        return new Array<>(0);
    }

    /** Checks if a patrol is travelling on the road between fromJunction to
     * toJunction. Have to check the road in both directions otherwise the patrols
     * can collide head on
     * @param from   The junction at one end of the road
     * @param to     The junction at the other end of the road
     * @return <code> true </code> if another Patrol is travelling on that road
     *                              (knows this because the road is present in lockedRoads)
     *         <code> false </code> if no Patrol is travelling on that road
     */
    public boolean isRoadLocked(Junction from, Junction to) {
        Road roadToCheck = getRoad(from, to);
        Road roadToCheckOtherDirection = getRoad(to, from);

        return lockedRoads.containsKey(roadToCheck) || lockedRoads.containsKey(roadToCheckOtherDirection);

    }

    /** Locks a road by a patrol if that road is not already locked
     * @param road    The road to lock
     * @param patrol  The patrol who locked that road - the only patrol who can unlock it */
    public void lockRoad(Road road, PatrolMovementSprite patrol) {
        if (!lockedRoads.containsKey(road)) {
            lockedRoads.put(road, patrol);
        }
    }

    /** Unlocks a road if there is a road that the patrol had previously locked
     *
     * @param road    The road to unlock
     * @param patrol  The patrol who wants to unlock the road. Will only unlock
     *                if this is the patrol who locked the road.
     */
    public void unlockRoad(Road road, PatrolMovementSprite patrol) {
        if (lockedRoads.containsKey(road)){
            if (lockedRoads.get(road) == patrol) {
                lockedRoads.remove(road);
            }
        }
    }

    /** Removes the road that a patrol was travelling on from locked roads when it
     * died. Otherwise this road would still be locked after the patrol died and no
     * other patrol would be able to travel on that road
     *
     * @param patrol  The patrol who died
     */
    public void removeDead(PatrolMovementSprite patrol){
        Iterator iter = lockedRoads.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            if (patrol.equals(entry.getValue())){
                iter.remove();
            }
        }
    }

    public Array<Junction> getJunctions(){ return this.junctions; }

    @Override
    public int getIndex(Junction node) { return node.index; }

    @Override
    public int getNodeCount() { return lastNodeIndex; }

}
