package api;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph graph;

    @Override
    public void init(directed_weighted_graph g) {
        this.graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public directed_weighted_graph copy() {
        if (this.graph == null) return null;
        directed_weighted_graph copy = new DWGraph_DS();

        //Go over the collection of all nodes and copy the attributes of the node
        for (node_data i : this.graph.getV()) {
            copy.addNode(i);

            //Copy of the neighbors
            for (edge_data v : this.graph.getE(i.getKey())) {
                copy.connect(i.getKey(), v.getDest(), v.getWeight());
            }
        }

        return copy;
    }

    @Override
    public boolean isConnected() {
        if(graph.getV().isEmpty()) return true;

        for(node_data i : graph.getV()){
            BFS(graph.getNode(i.getKey()));

            for(node_data x : graph.getV()){
                if(x.getTag() == -1) return false;
            }
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }

    private static void Dijkstra(directed_weighted_graph g, node_data src) {
        HashMap<Integer, Boolean> visited = new HashMap<>();

        Comparator<node_data> nameSorter = Comparator.comparing(node_data::getTag);
        PriorityQueue<node_data> pQueue = new PriorityQueue<>(nameSorter);
        for (node_data x : g.getV()) {
            if (x != src) {
                x.setTag(Integer.MAX_VALUE);
            } else {
                x.setTag(0);
            }
            x.setInfo(null);
            pQueue.add(x);
            visited.put(x.getKey(), false);
        }

        while (!pQueue.isEmpty()) {
            node_data curr = pQueue.remove();

            for (edge_data v : g.getE(curr.getKey())) {
                if (!visited.get(curr.getKey())) {
                    if(g.getEdge(curr.getKey(), v.getDest()) != null) {
                        double t = curr.getTag() + g.getEdge(curr.getKey(), v.getDest()).getWeight();
                        if (v.getTag() > t) {
                            v.setTag((int)t);
                            v.setInfo("" + curr.getKey());
                            pQueue.add(g.getNode(v.getDest()));
                        }
                    }
                }
            }
            visited.put(curr.getKey(), true);
        }
    }

    public void BFS(node_data n){
        //Reset distances.
        for(node_data x : graph.getV()){
            x.setTag(-1);
        }
        n.setTag(0);
        //Contain the neighbors of the current node.
        Queue<node_data> q = new LinkedList<>();
        q.add(n);

        while(!q.isEmpty()){
            node_data current = q.remove();

            for(edge_data v : graph.getE(current.getKey())){
                //If you have not yet passed the node.
                if(v.getTag() == -1){
                    node_data temp = graph.getNode(v.getDest());
                    //Marking the distance from the src
                    temp.setTag(current.getTag()+1);
                    q.add(temp);
                }
            }
        }
    }

}
