package tests;

import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    @Test
    void isConnected(){
        directed_weighted_graph dwg = DWGraph_DSTest.graph_creator(3,0);
        dwg.connect(0,1,10);
        dwg.connect(1,2,10);
        dwg.connect(2,0,10);
        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(dwg);
        assertTrue(wga.isConnected());
    }
}