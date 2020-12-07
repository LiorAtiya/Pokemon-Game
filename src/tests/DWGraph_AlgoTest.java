package tests;

import api.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    @Test
    void isConnected(){
        directed_weighted_graph dwg = DWGraph_DSTest.graph_creator(5,0);
        dwg.connect(0,3,10);
        dwg.connect(3,0,10);
        dwg.connect(3,4,10);
        dwg.connect(4,3,10);
        dwg.connect(3,2,10);
        dwg.connect(2,3,10);
        dwg.connect(2,1,10);
        dwg.connect(1,2,10);
        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(dwg);
        assertTrue(wga.isConnected());
    }

    @Test
    void shortestPathDist(){
        directed_weighted_graph dwg = DWGraph_DSTest.graph_creator(4,0);
        dwg.connect(0,1,1);
        dwg.connect(1,2,1);
        dwg.connect(2,3,1);
        dwg.connect(0,2,5);
        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(dwg);
        double actual = wga.shortestPathDist(0,3);
        assertEquals(3, actual);
        actual = wga.shortestPathDist(3,0);
        assertEquals(-1, actual);
    }

    @Test
    void shortestPath(){
        directed_weighted_graph dwg = DWGraph_DSTest.graph_creator(4,0);
        dwg.connect(0,1,1);
        dwg.connect(1,2,1);
        dwg.connect(2,3,1);
        dwg.connect(0,2,5);

        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(dwg);
        List<node_data> actual = wga.shortestPath(0,3);
        List<node_data> expected = new ArrayList<>();
        expected.add(dwg.getNode(0));
        expected.add(dwg.getNode(1));
        expected.add(dwg.getNode(2));
        expected.add(dwg.getNode(3));
        assertEquals(expected, actual);

        dwg = new DWGraph_DS();
        for (int i = 0; i < 6; i++){
            dwg.addNode(new NodeData(i));
        }

        dwg.connect(0,1,2);
        dwg.connect(1,2,6);
        dwg.connect(1,4,6);
        dwg.connect(1,3, 9);
        dwg.connect(4,3,1);
        dwg.connect(2,3,2);
        dwg.connect(3,0,0);
        dwg.connect(3,5,100);
        wga.init(dwg);

        actual = wga.shortestPath(0, 3);
        expected = new ArrayList<>();
        expected.add(dwg.getNode(0));
        expected.add(dwg.getNode(1));
        expected.add(dwg.getNode(4));
        expected.add(dwg.getNode(3));
        assertEquals(expected, actual);
    }

    @Test
    void save_load(){
        directed_weighted_graph dwg = DWGraph_DSTest.graph_creator(4,0);
        dw_graph_algorithms wga = new DWGraph_Algo();

        wga.load("data/A3");
        for(node_data n : wga.getGraph().getV()){
            for(node_data n1 : wga.getGraph().getV()){
                System.out.println("path between "+n.getKey()+" to "+n1.getKey()+":"+wga.shortestPathDist(n.getKey(),n1.getKey()));
            }
        }
        double res = wga.shortestPathDist(22,27);
        System.out.println("path between 22 to 27: "+res);
    }
}