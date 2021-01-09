package test;

import Classes.DWGraph_Algo;
import Classes.DWGraph_DS;
import Classes.NodeData;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_Java_VS_Python {

    @Test
    public void test_connectedComponent(){
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.addNode(new NodeData(4));
        g.addNode(new NodeData(5));

        g.connect(1,2,1);
        g.connect(2,3,1);
        g.connect(3,1,1);
        g.connect(3,4,1);
        g.connect(4,5,1);

        DWGraph_Algo ga = new DWGraph_Algo();
        ga.init(g);

        List<node_data> component = ga.connectedComponent(4);
//        System.out.println(component);
        System.out.println(ga.connected_components());
//        System.out.print("\nUnion: ");
//        for(int i=0 ; i < component.size() ; i++){
//            System.out.print(component.get(i).getKey()+", ");
//        }
    }

    @Test
    public void test_100_vertices() {
        directed_weighted_graph g = graph_creator(100,100);
        assertEquals(g.nodeSize(), 100);
    }
//
//    def test_10000_vertices(self):
//        g = graph_creator(10000)
//        self.assertEqual(g.v_size(), 10000)
//
//    def test_1000000_vertices(self):
//    g = graph_creator(1000000)
//        self.assertEqual(g.v_size(), 1000000)
//
//    def test_10000000_vertices(self):
//    g = graph_creator(10000000)
//        self.assertEqual(g.v_size(), 10000000)

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
}
