package Classes;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This implement directed_weighted_graph that represents a directional weighted graph.
 * The interface has a routing system or communication network at the top -
 * And must support a large number of nodes (over 100,000) ..
 * Which is really different methods of operation in the chart itself and at the vertices in the chart
 *
 * Vertices - contains all the nodes in the graph using HashMap.
 * Neighbors - Contains a hashap of nodes that have a path from a given node using HashMap within HashMap.
 * countMC- Represents the number of changes made to the graph (adding a vertex and more ..).
 * edgeSize- Contains the number of edges in the graph
 */

public class DWGraph_DS implements directed_weighted_graph {
    /**
     * This implement edge_data that represents the set of operations applicable on a
     * directional edge(src,dest) in a (directional) weighted graph.
     */
    private class Edge implements edge_data {

        private int src;
        private int des;
        private double weight;
        private String info;
        private int tag;

        /**
         *A constructor method that defines default values for edge.
         * @param src Edge source.
         * @param des Destination edge.
         * @param w Weight for edge.
         */
        public Edge(int src, int des, double w){
            this.src = src;
            this.des = des;
            this.weight = w;
            this.info = null;
            this.tag = 0;
        }

        /**
         * Return the id of the source node of this edge.
         * @return The id of the source.
         * @return
         */
        @Override
        public int getSrc() {
            return this.src;
        }

        /**
         * Return the id of the destination node of this edge.
         * @return The id of the destination.
         */
        @Override
        public int getDest() {
            return this.des;
        }

        /**
         * Return the weight of this edge (positive value).
         * @return weight
         */
        @Override
        public double getWeight() {
            return this.weight;
        }

        /**
         *Returns the remark (meta data) associated with this edge.
         * @return info.
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         *Defines a new value (meta data) associated with this edge.
         * @param s new  info.
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         *Returns the tag value associated with this edge (used for color definition).
         * @return tag
         */
        @Override
        public int getTag() {
            return this.tag;
        }

        /**
         * This method allows setting the "tag" value for temporal marking an edge - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            this.tag = t;
        }

        /**
         * The method returns a string that represents the values of a edge in the graph.
         * @return string that represents the values of a edge in the graph.
         */
        public String toString(){
            return "{\"src\":"+this.src+",\"w\":"+this.weight+",\"dest\":"+this.des+"}";
        }
    }


    //------------------------

    private final HashMap<Integer, node_data> vertices;
    private final HashMap<Integer, HashMap<Integer,edge_data>> neighbors;
    private int countMC;
    private int edgeSize;

    /**
     * A constructor that sets default values for a graph.
     */
    public DWGraph_DS() {
        this.vertices = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.countMC = 0;
        this.edgeSize = 0;
    }

    /**
     * this method add a new node to the graph with the given node_data.
     * If such a node exists, nothing will be done.
     * @param n node_data
     */
    @Override
    public void addNode(node_data n) {
        //If it already exists or n is null
        if (n == null || this.vertices.containsKey(n.getKey())) return;

        this.vertices.put(n.getKey(), n);
        this.neighbors.put(n.getKey(), new HashMap<>());
        this.countMC++;
    }

    /**
     * Return the node_data by the node_id.
     * @param key - the node_id
     * @return node_data or null.
     */
    @Override
    public node_data getNode(int key) {
        if (this.vertices.containsKey(key)) return this.vertices.get(key);
        return null;
    }

    /**
     * Connecting an edge from node 1 to node 2 by their key.
     * (if there is a edge between them nothing is done).
     * And the weight for the rib.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w >= 0) {
            //Same node.
            if (src == dest) return;

            node_data s = getNode(src);
            node_data d = getNode(dest);
            //Not exists
            if (s == null || d == null) return;
            //Edge exists with same weight.
            if (this.neighbors.get(src).containsKey(dest)) {
                if (getEdge(src, dest).getWeight() == w) {
                } else { ////Edge exists with different weight.
                    this.neighbors.get(src).replace(dest, new Edge(src,dest,w));
                    this.countMC++;
                }
                return;
            }

            this.neighbors.get(src).put(dest, new Edge(src,dest,w));
            this.countMC++;
            this.edgeSize++;
        }
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src node1
     * @param dest node2
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(this.neighbors.get(src) == null) return null;
        return this.neighbors.get(src).get(dest);
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return this.vertices.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        //FIX****
        Collection<edge_data> neiOfNode = new ArrayList<>();
        for(Integer x : this.neighbors.get(node_id).keySet()) {
            neiOfNode.add(getEdge(node_id,x));
        }
        return neiOfNode;
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
//        node_data x;
        //If it does not exist
        if (getNode(key) == null) return null;
        else {
            for(edge_data i : getE(key)){
                removeEdge(key, i.getDest());
            }
            for(node_data j : this.getV()){
                if(this.neighbors.get(j.getKey()).containsKey(key)){
                    removeEdge(j.getKey(),key);
                }
            }
//            this.neighbors.remove(key);
//            x = this.vertices.remove(key);
            this.countMC++;
        }
        return this.vertices.remove(key);
    }

    /**
     *Deletes the edge from the graph that connects the two given nodes.
     * @param src node1
     * @param dest node 2
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(this.neighbors.get(src) == null || this.neighbors.get(src).get(dest) == null) return null;
//        if(this.neighbors.get(src).get(dest) == null) return null;
        this.edgeSize--;
        return this.neighbors.get(src).remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * @return number of vertices in the graph.
     */
    @Override
    public int nodeSize() {
        return this.vertices.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * @return edgesize
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return countMC.
     */
    @Override
    public int getMC() {
        return this.countMC;
    }
}
