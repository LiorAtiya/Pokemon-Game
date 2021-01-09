package Classes;

import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This implement dw_graph_algorithms that represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone (); (copy)
 * 1. init (graph);
 * 2. isConnected (); // strongly (all ordered pais connected)
 * 3. double shortestPathDist (int src, int dest);
 * 4. List <node_data> shortestPath (int src, int dest);
 * 5. Save (file); // JSON file
 * 6. Load (file); // JSON file
 *
 * graph-Is an abstract representation of a set of nodes and edge,
 * each edge has a weight,
 * It is possible to have a route from node to another node.
 */

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph graph;

    /**
     *Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Compute a deep copy of this weighted graph,
     * By going through all the nodes in the graph using an iterator.
     * and creating a list of neighbors and sides corresponding to each node using iterator
     * .
     * @return Compute a deep copy of this weighted graph.
     */
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

    /**
     * Returns true if and only if (iff) there is a valid path from the EVREY node to each,
     * by checking if there is a node in the graph
     * that the method did not reach him from all the other nodes.
     *
     * The test is done by scan on all the nodes using an iterator
     * this method checks if there is a proper route from the src to the rest of the nodes by marking
     * all the nodes that have a suitable route from the src using the Dijkstra algorithm.
     * Then by switching with an iterator, check if there is a node in the graph whose weight value is Double.MAX_VALUE.
     * there is no path from all node to any nodes and the graph is not linked.
     *
     * @return true if the graph is linked, otherwise false.
     */
    @Override
    public boolean isConnected() {
        if (graph.getV().isEmpty()) return true;

        for (node_data i : graph.getV()) {
            BFS(this.graph, this.graph.getNode(i.getKey()));
//            Dijkstra(this.graph, graph.getNode(i.getKey()));

            for (node_data x : graph.getV()) {
                if (x.getTag() == 0) return false;
            }
        }
        return true;
    }

    public List<node_data> connectedComponent(int src){
        List<node_data> component = new ArrayList<>();
        List<node_data> componentReverse = new ArrayList<>();
        List<node_data> Union = new ArrayList<>();

        BFS(this.graph, this.graph.getNode(src));
        for (node_data x : graph.getV()) {
            if (x.getTag() == 1){
                component.add(x);
            }
        }
//        System.out.print("\nBefore reverse: ");
//        for(int i=0 ; i < component.size() ; i++){
//            System.out.print(component.get(i).getKey()+", ");
//        }

        BFS(this.edgeReverse(), this.graph.getNode(src));
        for (node_data x : graph.getV()) {
            if (x.getTag() == 1){
                componentReverse.add(x);
            }
        }
//        System.out.print("\nAfter reverse: ");
//        for(int i=0 ; i < componentReverse.size() ; i++){
//            System.out.print(componentReverse.get(i).getKey()+", ");
//        }

        for(int i=0 ; i < component.size() ; i++){
            if(componentReverse.contains(component.get(i))){
                Union.add(component.get(i));
            }
        }


        return Union;
    }

    public directed_weighted_graph edgeReverse(){
        directed_weighted_graph reverse = new DWGraph_DS();
        for(node_data x : graph.getV()){
            reverse.addNode(x);
        }

        for(node_data x : graph.getV()){
            for(edge_data e : graph.getE(x.getKey())){
                reverse.connect(e.getDest(),e.getSrc(),e.getWeight());
            }
        }
        return reverse;
    }


    public List<List<node_data>> connected_components(){
        HashMap<Integer,Integer> allNodes = new HashMap();
        List<List<node_data>> allComponents = new ArrayList<>();
        for(node_data n : this.graph.getV()){
            allNodes.put(n.getKey(),n.getKey());
        }

        while(allNodes.size() > 0){
            int first = allNodes.get(allNodes.keySet().toArray()[0]);
            List<node_data> comp = this.connectedComponent(first);
            allComponents.add(comp);
            for(node_data n : comp){
                allNodes.remove(n.getKey());
            }
        }

        return allComponents;
    }

    /**
     * Returns the shortest path length between src and dest
     * by implementing a dijkstra algorithm.
     * if there is no such path -> returns -1.
     *
     * The algorithm gets the start and destination value and first goes over all the nodes in the graph using an iterator
     * and updates the tag value to Double.Max_Value.
     * Then insert the source node into the PriorityQueue structure which allows to say priority (this priority was set
     * by the comperator in class) and thus arrange the node with the shortest route at the top of the queue.
     * The algorithm then passes over neighbors by using iterator of each vertex starting from src and removes
     * from the queue the neighbor with the shortest route.
     * Which ensures that we will not be able to reach every junction with a shorter route.
     * Will finally return the tag value of the destination node.
     * @param src - start node
     * @param dest - end (target) node
     * @return The length of the path.
     */
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

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest.
     * By pass the shortest path from the end to the beginning.
     * If no such path exists return null.
     * <p>
     * The method sends the given values of src and dest to the shortestPathDist method (),
     * and adds to the ArrayList the nodes of the corresponding path by value of info
     * add to the list the dest and then the parent vertex (defined in the info of each node) until you reach the src.
     * and the method will return this list.

     * @param src - start node.
     * @param dest - end (target) node.
     * @return List<node_data>.
     */
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

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved.
     */
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

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
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

    /**
     * Algorithm for finding the shortest path.
     * @param g
     * @param src
     */
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

    /**
     *A comparator class that compares two node_data, implemented for an algorithm Dijkstra.
     */
    public class NodeComparator implements Comparator<node_data> {
        @Override
        public int compare(node_data o1, node_data o2) {
            int ans = 0;
            if (o1.getWeight() - o2.getWeight() > 0) ans = 1;
            else if (o1.getWeight() - o2.getWeight() < 0) ans = -1;
            return ans;
        }
    }

    /**
     * Algorithm for finding the shortest path in an unweighted graph.
     * @param n
     */
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
