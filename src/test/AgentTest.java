package test;

import Classes.DWGraph_DS;
import Classes.NodeData;
import api.directed_weighted_graph;
import api.node_data;
import gameClient.Agent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    private static Agent createAgent(){
        directed_weighted_graph g = new DWGraph_DS();
        Agent ag = new Agent(g,0);
        return ag;
    }

    @Test
    void srcNodeTest() {
        Agent ag = createAgent();
    }


    @Test
    void getValue() {
    }

    @Test
    void setNextNode() {
    }

    @Test
    void setCurrNode() {
    }

    @Test
    void testToString() {
    }

    @Test
    void getID() {
    }

    @Test
    void getLocation() {
    }

    @Test
    void getNextNode() {
    }

    @Test
    void getSpeed() {
    }

    @Test
    void setSpeed() {
    }
}