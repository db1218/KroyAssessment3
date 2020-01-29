package com.PathFinding;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class GenerateGraph extends ApplicationAdapter {
    MapGraph mapGraph;
    GraphPath<Junction> cityPath;
    ArrayList<Junction> junctionsInMap;

    public GenerateGraph(){
        mapGraph = new MapGraph();
        junctionsInMap = new ArrayList<>();

        Junction one = new Junction(4987, 572, "bottom right corner");
        junctionsInMap.add(one);
        Junction two = new Junction(3743, 572, "Bottom 4 junction R.H.S");
        junctionsInMap.add(two);
        Junction three = new Junction(2728, 572, " Bottom turn left to dead end");
        junctionsInMap.add(three);
        Junction four = new Junction(2538, 572, "Bottom turn up to four junction");
        junctionsInMap.add(four);
        Junction five = new Junction(1069, 572, "bottom 5 left 4 junction");
        junctionsInMap.add(five);
        Junction six = new Junction(3745, 1199, "bottom left of fire station");
        junctionsInMap.add(six);
        Junction seven = new Junction(4123, 1199, "bottom right of fire station");
        junctionsInMap.add(seven);
        Junction eight = new Junction(4128, 1910, "Top right of fire station");
        junctionsInMap.add(eight);
        Junction nine = new Junction(3738, 1918, "Top left of fire station");
        junctionsInMap.add(nine);
        Junction ten = new Junction(3412, 1920, "Across bridge turn up to tower");
        junctionsInMap.add(ten);
        Junction eleven = new Junction(2544, 1920, "4 junction bottom right of tower");
        junctionsInMap.add(eleven);
        Junction twelve = new Junction(2160, 1959, "to left of 4 junction bottom right of tower");
        junctionsInMap.add(twelve);

        mapGraph.addJunction(one);
        mapGraph.addJunction(two);
        mapGraph.addJunction(three);
        mapGraph.addJunction(four);
        mapGraph.addJunction(five);
        mapGraph.addJunction(six);
        mapGraph.addJunction(seven);
        mapGraph.addJunction(eight);
        mapGraph.addJunction(nine);
        mapGraph.addJunction(ten);
        mapGraph.addJunction(eleven);
        mapGraph.addJunction(twelve);

        mapGraph.connectJunctions(one, two);
        mapGraph.connectJunctions(two, one);
        mapGraph.connectJunctions(two, six);
        mapGraph.connectJunctions(two, three);
        mapGraph.connectJunctions(three, two);
        mapGraph.connectJunctions(three, four);
        mapGraph.connectJunctions(four, three);
        mapGraph.connectJunctions(four, five);
        mapGraph.connectJunctions(four, eleven);
        mapGraph.connectJunctions(five, four);
        mapGraph.connectJunctions(eleven, four);
        mapGraph.connectJunctions(eleven, ten);
        mapGraph.connectJunctions(eleven, twelve);
        mapGraph.connectJunctions(twelve, eleven);
        mapGraph.connectJunctions(ten, eleven);
        mapGraph.connectJunctions(ten, nine);
        mapGraph.connectJunctions(nine, ten);
        mapGraph.connectJunctions(nine, eight);
        mapGraph.connectJunctions(nine, six);
        mapGraph.connectJunctions(eight, nine);
        mapGraph.connectJunctions(eight, seven);
        mapGraph.connectJunctions(seven, eight);
        mapGraph.connectJunctions(seven, six);
        mapGraph.connectJunctions(six, nine);
        mapGraph.connectJunctions(six, seven);
        mapGraph.connectJunctions(six, two);

      //  cityPath = cityGraph.findPath(one, eleven);
    }

    public GraphPath<Junction> generateRandomGraph(int nodeIndex){
//        Junction one = cityGraph.getJunction(nodeIndex);
        Junction one = mapGraph.getJunction(nodeIndex);
        Junction two = mapGraph.getJunction(2);
      //  Junction two = generateRandomDestination();
        GraphPath<Junction> randomPath = mapGraph.findPath(one, two);
        return randomPath;
    }

    private Junction generateRandomDestination(){
        Random rand = new Random();
        int n = rand.nextInt(mapGraph.getNodeCount());
        return mapGraph.getJunction(n);
    }


    public Junction getJunction(Vector2 position){
        for (Junction junction: junctionsInMap){
            if (junction.getX() == position.x && junction.getY() == position.y){
                return junction;

            }
        }
        return new Junction(position.x, position.y, "new");
    }

    public MapGraph getMapGraph() {
        return mapGraph;
    }
}
