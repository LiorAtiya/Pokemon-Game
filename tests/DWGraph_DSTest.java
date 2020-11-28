package tests;

import api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    @Test
    void addNode(){
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);

        directed_weighted_graph dwg = new DWGraph_DS();
        dwg.addNode(n1);
        dwg.addNode(n1);
        dwg.addNode(n2);
        dwg.addNode(n3);
        dwg.addNode(null);

        assertEquals(dwg.nodeSize(), 3);
    }

    @Test
    void getNode(){
        node_data n1 = new NodeData(1);
        directed_weighted_graph dwg = new DWGraph_DS();
        dwg.addNode(n1);
        assertEquals(dwg.getNode(1).getKey(),1);
        assertNull(dwg.getNode(2));
    }

    @Test
    void getEdge_connect(){
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        directed_weighted_graph dwg = new DWGraph_DS();
        dwg.addNode(n1);
        dwg.addNode(n2);
        dwg.addNode(n3);
        dwg.connect(1,2,10);
        dwg.connect(2,3,10);
        dwg.connect(3,1,10);

        edge_data actual = dwg.getEdge(1,2);
        assertEquals(10, actual.getWeight());
        assertEquals(1, actual.getSrc());
        assertEquals(2, actual.getDest());
        assertNull(dwg.getEdge(2,1));

    }

    @Test
    void getV_getE(){
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        directed_weighted_graph dwg = new DWGraph_DS();
        dwg.addNode(n1);
        dwg.addNode(n2);
        dwg.addNode(n3);
        dwg.connect(1,2,10);
        dwg.connect(1,2,6);
        dwg.connect(1,10,1000);
        dwg.connect(1,3,10);

        assertEquals(3, dwg.getV().size());
        assertEquals(2, dwg.getE(1).size());
        assertEquals(0, dwg.getE(2).size());
    }

    @Test
    void removeEdge(){
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        directed_weighted_graph dwg = new DWGraph_DS();
        dwg.addNode(n1);
        dwg.addNode(n2);
        dwg.addNode(n3);
        dwg.connect(1,2,10);
        dwg.connect(1,3,10);
        dwg.removeEdge(1,2);
        dwg.removeEdge(1,2);
        dwg.removeEdge(1,1);
        dwg.removeEdge(2,1);

        assertEquals(1,dwg.edgeSize());
        dwg.connect(1,2,11);
        assertEquals(2,dwg.edgeSize());
    }

    @Test
    void removeNode_getMC_edgeSize(){
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        node_data n4 = new NodeData(4);
        directed_weighted_graph dwg = new DWGraph_DS();
        dwg.addNode(n1);
        dwg.addNode(n2);
        dwg.addNode(n3);
        dwg.addNode(n4);
        assertEquals(4,dwg.getMC());
        dwg.connect(2,1,10);
        dwg.connect(3,1,10);
        dwg.connect(4,1,10);
//        dwg.connect(3,1,10);
//        dwg.connect(3,1,10);
//        dwg.connect(3,1,6);
//        assertEquals(8,dwg.getMC());

        assertEquals(3,dwg.edgeSize());
        dwg.removeNode(1);
//        dwg.removeNode(2);
//        dwg.removeNode(1);
        assertEquals(0,dwg.edgeSize());
//        assertEquals(10,dwg.getMC());
    }

    public static directed_weighted_graph graph_creator(int v_size, int e_size) {
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i < v_size; i++) {
            g.addNode(new NodeData(i));
        }

        for (int i = 0; i < v_size; i++) {
            for (int j = 1; j < v_size; j++) {
                if (g.edgeSize() < e_size) {
                    g.connect(i, j, 4);
                }
            }
            if (g.edgeSize() >= e_size) {
                break;
            }
        }

        return g;
    }

    /**
     Create a graph of a million vertices and 10 million edges.
     */
    @Test
    void generalTest1() {
        directed_weighted_graph wg = graph_creator(1000000,10000000);
        assertEquals(wg.nodeSize(), 1000000);
        assertEquals(wg.edgeSize(), 10000000);
    }


}