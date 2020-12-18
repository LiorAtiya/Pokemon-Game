package Classes;

import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;

import java.io.*;
import java.lang.reflect.Type;
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
            node_data n = new NodeData(i.getKey());
            n.setTag(i.getTag());
            n.setWeight(i.getWeight());
            n.setInfo(i.getInfo());
            n.setLocation(i.getLocation());
            copy.addNode(n);

            //Copy of the neighbors
            for (edge_data v : this.graph.getE(i.getKey())) {
                copy.connect(i.getKey(), v.getDest(), v.getWeight());
            }
        }

        return copy;
    }

    @Override
    public boolean isConnected() {
        if (graph.getV().isEmpty()) return true;

        for (node_data i : graph.getV()) {
            BFS(graph.getNode(i.getKey()));
//            Dijkstra(this.graph, graph.getNode(i.getKey()));

            for (node_data x : graph.getV()) {
                if (x.getWeight() == Double.MAX_VALUE) return false;
            }
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        //If it does not exist src or dest.
        if (graph.getNode(dest) == null || graph.getNode(src) == null) return -1;
        Dijkstra(this.graph, graph.getNode(src));
        //If there is no path to the dest.
        if (graph.getNode(dest).getWeight() == Double.MAX_VALUE) return -1;

        ////*In the attribute of the node - "Weight", save the distance between the src and the dest.
        return graph.getNode(dest).getWeight();
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        //Scan the entire graph and return the number of edges of the shortest path
        double sizePath = shortestPathDist(src, dest);
        if (sizePath == -1) return null;

        //Contain all nodes of the path
        List<node_data> path = new ArrayList<>();

        if (src == dest) path.add(graph.getNode(dest));
        else {
            path.add(graph.getNode(dest));
            node_data d = this.graph.getNode(dest);
            //*In the attribute of the node - "Info", save the key of the parent node in the shortest path
            node_data parent = this.graph.getNode(Integer.parseInt(d.getInfo()));

            //Loop from dest to src
            while (parent.getInfo() != null) {
                path.add(0, parent);
                parent = this.graph.getNode(Integer.parseInt(parent.getInfo()));
            }
            path.add(0, graph.getNode(src));
        }
        return path;
    }

    @Override
    public boolean save(String file) {
        String json = "{\"Edges\":[";
        for (node_data x : this.graph.getV()) {
            for (edge_data y : this.graph.getE(x.getKey())) {
                json += y.toString() + ",";
            }
        }
        json = json.substring(0, json.length() - 1) + "],\"Nodes\":[";

        for (node_data x : this.graph.getV()) {
            json += x.toString() + ",";
        }
        json = json.substring(0, json.length() - 1) + "]}";

        System.out.println(json);

        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        GsonBuilder builder = new GsonBuilder();
        JsonDeserializer<DWGraph_DS> graphObject = new JsonDeserializer<DWGraph_DS>() {
            @Override
            public DWGraph_DS deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();
                DWGraph_DS graph = new DWGraph_DS();

                for (JsonElement n : nodes) {
                    int key = n.getAsJsonObject().get("id").getAsInt();

                    String location = n.getAsJsonObject().get("pos").getAsString();
                    String[] parts = location.split(",");
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    double z = Double.parseDouble(parts[2]);
                    Point3D p = new Point3D(x, y, z);

                    node_data newNode = new NodeData(key);
                    newNode.setLocation(p);
                    graph.addNode(newNode);
                }

                JsonArray edges = jsonObject.get("Edges").getAsJsonArray();
                for (JsonElement e : edges) {
                    int src = e.getAsJsonObject().get("src").getAsInt();
                    int dest = e.getAsJsonObject().get("dest").getAsInt();
                    double weight = e.getAsJsonObject().get("w").getAsDouble();
                    graph.connect(src, dest, weight);
                }
                return graph;
            }
        };

        try {
            builder.registerTypeAdapter(DWGraph_DS.class, graphObject);
            Gson customGson = builder.create();

            FileReader reader = new FileReader(file);
            DWGraph_DS graph = customGson.fromJson(reader, DWGraph_DS.class);
            this.graph = graph;
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void Dijkstra(directed_weighted_graph g, node_data src) {

        PriorityQueue<node_data> pQueue = new PriorityQueue<>(new NodeComparator());
        for (node_data x : g.getV()) {
            if (x != src) {
                x.setWeight(Double.MAX_VALUE);
                x.setInfo(null);
            } else {
                x.setWeight(0);
                x.setInfo(null);
            }
            pQueue.add(x);
        }

        while (!pQueue.isEmpty()) {
            node_data curr = pQueue.remove();

            for (edge_data v : g.getE(curr.getKey())) {
                node_data nei = g.getNode(v.getDest());
                double t = curr.getWeight() + v.getWeight();
                if (nei.getWeight() > t) {
                    nei.setWeight(t);
                    nei.setInfo("" + curr.getKey());
                    pQueue.add(nei);
                }
            }
        }
    }

//    private void Dijkstra(directed_weighted_graph g, node_data src) {
//        PriorityQueue<node_data> pQueue = new PriorityQueue<>(new NodeComparator());
//        Iterator<node_data> max = graph.getV().iterator();
//        while (max.hasNext()) {
//            node_data temp = max.next();
//            temp.setWeight(Double.MAX_VALUE);
//            temp.setInfo(null);
//        }
//        src.setWeight(0);
//        pQueue.add(graph.getNode(src.getKey()));
//
//        while (!pQueue.isEmpty()) {
//            node_data prev = pQueue.peek();
//            pQueue.poll();
//            Iterator<edge_data> neighbors = graph.getE(prev.getKey()).iterator();
//            while (neighbors.hasNext()) {
//                edge_data temp2 = neighbors.next();
//
//                node_data dest = this.graph.getNode(temp2.getDest());
//                edge_data edgeW = graph.getEdge(prev.getKey(), temp2.getDest());
//
//                if (dest.getWeight() > prev.getWeight() + edgeW.getWeight()) {
//                    dest.setWeight(prev.getWeight() + edgeW.getWeight());
//                    pQueue.add(dest);
//                    dest.setInfo("" + prev.getKey());
//                }
//
//            }
//        }
//    }

    public class NodeComparator implements Comparator<node_data> {
        @Override
        public int compare(node_data o1, node_data o2) {
            int ans = 0;
            if (o1.getWeight() - o2.getWeight() > 0) ans = 1;
            else if (o1.getWeight() - o2.getWeight() < 0) ans = -1;
            return ans;
        }
    }

    public void BFS(node_data n) {
        //Reset distances.
        for (node_data x : graph.getV()) {
            x.setTag(0);
        }
        n.setTag(1);
        //Contain the neighbors of the current node.
        Queue<node_data> q = new LinkedList<>();
        q.add(n);

        while (!q.isEmpty()) {
            node_data current = q.remove();

            for (edge_data v : graph.getE(current.getKey())) {
                //If you have not yet passed the node.
                node_data temp = graph.getNode(v.getDest());
                if (temp.getTag() == 0) {
                    //Marking the distance from the src
                    temp.setTag(1);
                    q.add(temp);
                }
            }
        }
    }

}
