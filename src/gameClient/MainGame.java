package gameClient;

import Classes.DWGraph_Algo;
import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * This class presents an entire course of the game.
 * 1. Call the server for game information using Json files.
 * 2. Use functions to advance the game automatically using the information from the server.
 * 3. Use functions for victory strategies automatically.
 * 4. Create a user-friendly interface using a GUI.
 */
public class MainGame implements Runnable {
    private static GameFrame _win;
    private static Arena _ar;
    private int scenario_num;
    private long id;
    private static long dt = 0;

    public MainGame(long id, int level) {
        this.id = id;
        this.scenario_num = level;
    }

    /**
     * Run the game
     */
    @Override
    public void run() {

        //Create game
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        game.login(this.id);

        //Load graph
        dw_graph_algorithms algoGraph = new DWGraph_Algo();
        try {
            PrintWriter pw = new PrintWriter(new File("Graph"));
            pw.write(game.getGraph());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        algoGraph.load("Graph");

        directed_weighted_graph gg = algoGraph.getGraph();

        init(game, gg);

        game.startGame();
        _win.setTitle("Pokemon Game : " + game.toString());
        long dt = 0;

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                _win.repaint();
                List<Agent> log = _ar.getAgents();
                Agent maxAg = log.get(0);
                for (int i = 0; i < log.size(); i++) {
                    if (log.get(i).getSpeed() > maxAg.getSpeed()) {
                        maxAg = log.get(i);
                    }
                }
                if (maxAg.getSpeed() == 1.0) dt = 120;
                else if (maxAg.getSpeed() == 2.0) dt = 110;
                else if (maxAg.getSpeed() == 5.0) dt = 90;
                Thread.sleep(dt);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String result = game.toString();

        System.out.println(result);
    }

    /**
     * Moves each of the agents along the edge,
     * The next node is selected according to the neighbors of the same
     * node a target of the edge on which the agent is located.
     * @param game
     * @param gg graph
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        long t = game.timeToEnd();
        int seconds = (int) ((t / 1000) % 60);
        _ar.setTimeToEnd(seconds);
        _ar.set_info(game.toString());

        String lg = game.move();
        List<Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);

        String fs = game.getPokemons();
        List<Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            double s = ag.getSpeed();
            if (dest == -1) {
                dest = nextNode(gg, src);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + " | Value: " + v + " | Speed : " + s + " | Move to node: " + dest);
                System.out.println("Time left: " + seconds + " seconds");
            }
        }
    }

    /**
     * Selecting the next node of the agent according to strategy.
     * @param g graph
     * @param src source
     * @return next node
     */
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = strategy2(g, src);
        return ans;
    }

    /**
     * Victory Strategy Number 2 - The agent goes through a loop on the entire Pokemon list
     * in the game and calculates the path from the node it is to each source node of each
     * Pokemon in the list, and at the end the agent selects the minimum path from all
     * the paths he has calculated.
     * @param g graph
     * @param src source
     * @return
     */
    public static int strategy2(directed_weighted_graph g, int src) {
        int ans;
        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(g);

        List<Pokemon> listPokemom = _ar.getPokemons();
        Pokemon better = listPokemom.get(0);
        for (int i = 0; i < listPokemom.size(); i++) {
            Arena.updateEdge(listPokemom.get(i), g);
        }

        for (int i = 0; i < listPokemom.size(); i++) {
            double path2index = wga.shortestPathDist(src, listPokemom.get(i).get_edge().getSrc());
            double path2better = wga.shortestPathDist(src, better.get_edge().getSrc());
            geo_location pos = listPokemom.get(i).getLocation();
//            if ((path2index <= path2better) && !checkTarget(ag)){
            if ((path2index <= path2better)){
                better = listPokemom.get(i);
//                ag.setTarget(pos);
            }
        }

        int startEdge = better.get_edge().getSrc();
        List<node_data> listNodes = wga.shortestPath(src, startEdge);
        listNodes.add(g.getNode(better.get_edge().getDest()));

        if(listNodes.size() == 2){
            dt = 20;
        }
        if (listNodes.size() > 1) {
            ans = listNodes.get(1).getKey();
        } else {
            ans = listNodes.get(0).getKey();
        }
        return ans;
    }

    /**
     * Initialize the game - create the agents, store the game information in the Arena and create the GUI.
     * @param game
     * @param graph
     */
    private void init(game_service game, directed_weighted_graph graph) {

        ArrayList<Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());
        for (int a = 0; a < pokemonList.size(); a++) {
            Arena.updateEdge(pokemonList.get(a), graph);
        }

        initArena(graph, pokemonList);
        createGUI();

        //Init position of agents
        try {
            String info = game.toString();
            JSONObject line;
            line = new JSONObject(info);
            JSONObject gameObject = line.getJSONObject("GameServer");
            int numOfAgents = gameObject.getInt("agents");

            for (int i = 0; i < numOfAgents; i++) {
                Pokemon c = pokemonList.get(i);
                int nn = c.get_edge().getSrc();
                game.addAgent(nn);
            }

            //Print detail of game
            System.out.println(info);
            System.out.println(game.getPokemons());
            System.out.println(game.getAgents());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initArena(directed_weighted_graph g, List<Pokemon> p) {
        this._ar = new Arena();
        this._ar.setGraph(g);
        this._ar.setPokemons(p);
    }

    public void createGUI() {
        _win = new GameFrame("test Ex2", _ar);
        _win.setVisible(true);
    }

//    private static PriorityQueue<Pokemon> pQueue = new PriorityQueue<>();
//
//    public class PokemonComparator implements Comparator<Pokemon> {
//        @Override
//        public int compare(Pokemon o1, Pokemon o2) {
//            int ans = 0;
//            if (o1.getMin_dist() - o2.getMin_dist() > 0) ans = 1;
//            else if (o1.getMin_dist() - o2.getMin_dist() < 0) ans = -1;
//            return ans;
//        }
//    }
//
//    public static int strategy4(directed_weighted_graph g, int src) {
//        int ans = -1;
//        List<Pokemon> listPokemon = _ar.getPokemons();
//        dw_graph_algorithms dga = new DWGraph_Algo();
//        for (int i = 0; i < listPokemon.size(); i++) {
//            Arena.updateEdge(listPokemon.get(i), g);
//            double weight = dga.shortestPathDist(src, listPokemon.get(i).get_edge().getSrc());
//            listPokemon.get(i).setMin_dist(weight);
//            pQueue.add(listPokemon.get(i));
//        }
//
//        Pokemon better = pQueue.remove();
//        List<node_data> listNodes = dga.shortestPath(src, better.get_edge().getSrc());
//        listNodes.add(g.getNode(better.get_edge().getDest()));
//
//        if (listNodes.size() > 1) {
//            ans = listNodes.get(1).getKey();
//        } else {
//            ans = listNodes.get(0).getKey();
//        }
//        return ans;
//
//    }
//
//
//    //me
//    public static int strategy3(directed_weighted_graph g, int src) {
//        int ans = -1;
//        List<Pokemon> listPokemon = _ar.getPokemons();
//        Pokemon better = listPokemon.get(0);
//        for (int i = 0; i < listPokemon.size(); i++) {
//            if (listPokemon.get(i).getValue() > better.getValue()) {
//                better = listPokemon.get(i);
//            }
//        }
//        for (int i = 0; i < listPokemon.size(); i++) {
//            Arena.updateEdge(listPokemon.get(i), g);
//        }
//
//        dw_graph_algorithms wga = new DWGraph_Algo();
//        wga.init(g);
//
//        for (int i = 0; i < listPokemon.size(); i++) {
//            if (wga.shortestPathDist(src, listPokemon.get(i).get_edge().getSrc())
//                    <= wga.shortestPathDist(src, better.get_edge().getSrc())) {
//                better = listPokemon.get(i);
//            }
//        }
//
//        int startEdge = better.get_edge().getSrc();
//        List<node_data> listNodes = wga.shortestPath(src, startEdge);
//        listNodes.add(g.getNode(better.get_edge().getDest()));
//
//        if (listNodes.size() > 1) {
//            ans = listNodes.get(1).getKey();
//        } else {
//            ans = listNodes.get(0).getKey();
//        }
//        return ans;
//    }



//    private static boolean checkTarget(Agent ag){
//        List<Agent> listA = Arena.getAgents();
//        for(int i=0 ; i < listA.size() ; i++){
//            if(ag.getID() != i && (listA.get(i).getTarget() == ag.getTarget()) ){
//                return false;
//            }
//        }
//        return true;
//    }

//    //Eats pokemon by the best value(high score)
//    private static int strategy1(directed_weighted_graph g, int src) {
//        int ans = -1;
//        dw_graph_algorithms wga = new DWGraph_Algo();
//        wga.init(g);
//
//        List<Pokemon> listPokemom = _ar.getPokemons();
//        Pokemon better = listPokemom.get(0);
//        for (int i = 0; i < listPokemom.size(); i++) {
//            Arena.updateEdge(listPokemom.get(i), g);
//        }
//
//        for (int i = 0; i < listPokemom.size(); i++) {
//            if ((listPokemom.get(i).getValue() > better.getValue())) {
//                better = listPokemom.get(i);
//            }
//        }
//        int startEdge = better.get_edge().getSrc();
//        List<node_data> listNodes = wga.shortestPath(src, startEdge);
//        listNodes.add(g.getNode(better.get_edge().getDest()));
//
//
//        if (listNodes.size() > 1) {
//            ans = listNodes.get(1).getKey();
//        } else {
//            ans = listNodes.get(0).getKey();
//        }
//        return ans;
//    }

}

