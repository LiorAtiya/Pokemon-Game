package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {

    private class Edge implements edge_data {

        private int src;
        private int des;
        private double weight;
        private String info;
        private int tag;

        public Edge(int src, int des, double w){
            this.src = src;
            this.des = des;
            this.weight = w;
            this.info = null;
            this.tag = 0;
        }

        @Override
        public int getSrc() {
            return this.src;
        }

        @Override
        public int getDest() {
            return this.des;
        }

        @Override
        public double getWeight() {
            return this.weight;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return this.tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }
    }


    //------------------------

    private final HashMap<Integer, node_data> vertices;
    private final HashMap<Integer, HashMap<Integer,edge_data>> neighbors;
    private int countMC;
    private int edgeSize;

    public DWGraph_DS() {
        this.vertices = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.countMC = 0;
        this.edgeSize = 0;
    }

    @Override
    public void addNode(node_data n) {
        //If it already exists or n is null
        if (n == null || this.vertices.containsKey(n.getKey())) return;

        this.vertices.put(n.getKey(), new NodeData(n));
        this.neighbors.put(n.getKey(), new HashMap<>());
        this.countMC++;
    }

    @Override
    public node_data getNode(int key) {
        if (this.vertices.containsKey(key)) return this.vertices.get(key);
        return null;
    }

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

    @Override
    public edge_data getEdge(int src, int dest) {
        if(this.neighbors.get(src) == null) return null;
        return this.neighbors.get(src).get(dest);
    }

    @Override
    public Collection<node_data> getV() {
        return this.vertices.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        Collection<edge_data> neiOfNode = new ArrayList<>();
        for(Integer x : this.neighbors.get(node_id).keySet()) {
            neiOfNode.add(getEdge(node_id,x));
        }
        return neiOfNode;
    }

    @Override
    public node_data removeNode(int key) {
        node_data x;
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
            this.neighbors.remove(key);
            x = this.vertices.remove(key);
            this.countMC++;
        }
        return x;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(this.neighbors.get(src).remove(dest) == null) return null;
        this.edgeSize--;
        return this.neighbors.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return this.vertices.size();
    }

    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    @Override
    public int getMC() {
        return this.countMC;
    }
}
