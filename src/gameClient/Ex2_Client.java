package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class Ex2_Client implements Runnable {
    private static GameFrame _win;
    private static Arena _ar;
    private static int scenario_num = -1;

    public Ex2_Client(int level){
        scenario_num = level;
    }

    public static void main(String[] args) {
        Frame login = new LoginGameFrame();
    }

    @Override
    public void run() {

        //Choose game level in the console
//        Scanner in = new Scanner(System.in);
//        int scenario_num;
//        do {
//            System.out.print("Enter scenario number: ");
//            scenario_num = in.nextInt();
//        } while (scenario_num > 23);

        //Create game
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 9999999;
        //	game.login(id);

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

        init(game,gg);

        game.startGame();
        _win.setTitle("Pokemon Game : " + game.toString());
        int ind = 0;
        long dt = 0;

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
//				if(ind%3 == 0) {_win.repaint();}
                _win.repaint();
                List<CL_Agent> log = _ar.getAgents();
                CL_Agent minAg = log.get(0);
                for (int i = 0; i < log.size(); i++) {
                    if (log.get(i).getSpeed() > minAg.getSpeed()) {
                        minAg = log.get(i);
                    }
                }
                if (minAg.getSpeed() == 1.0) dt = 100;
                else if (minAg.getSpeed() == 2.0) dt = 95;
                else if (minAg.getSpeed() == 5.0) dt = 90;
                Thread.sleep(dt);
//                    ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String result = game.toString();

        System.out.println(result);
//		System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        long t = game.timeToEnd();
        int seconds = (int) ((t / 1000) % 60);
        _ar.setTimeToEnd(seconds);
        _ar.set_info(game.toString());

        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);

        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
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
     * a very simple random walk implementation!
     *
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = strategy2(g, src);
        return ans;
    }

    private static PriorityQueue<CL_Pokemon> pQueue = new PriorityQueue<>();

    public class PokemonComparator implements Comparator<CL_Pokemon> {
        @Override
        public int compare(CL_Pokemon o1, CL_Pokemon o2) {
            int ans = 0;
            if (o1.getMin_dist() - o2.getMin_dist() > 0) ans = 1;
            else if (o1.getMin_dist() - o2.getMin_dist() < 0) ans = -1;
            return ans;
        }
    }

    public static int strategy4(directed_weighted_graph g, int src) {
        int ans = -1;
        List<CL_Pokemon> listPokemon = _ar.getPokemons();
        dw_graph_algorithms dga = new DWGraph_Algo();
        for (int i = 0; i < listPokemon.size(); i++) {
            Arena.updateEdge(listPokemon.get(i), g);
            double weight = dga.shortestPathDist(src, listPokemon.get(i).get_edge().getSrc());
            listPokemon.get(i).setMin_dist(weight);
            pQueue.add(listPokemon.get(i));
        }

        CL_Pokemon better = pQueue.remove();
        List<node_data> listNodes = dga.shortestPath(src, better.get_edge().getSrc());
        listNodes.add(g.getNode(better.get_edge().getDest()));

        if (listNodes.size() > 1) {
            ans = listNodes.get(1).getKey();
        } else {
            ans = listNodes.get(0).getKey();
        }
        return ans;

    }


    //me
    public static int strategy3(directed_weighted_graph g, int src) {
        int ans = -1;
        List<CL_Pokemon> listPokemon = _ar.getPokemons();
        CL_Pokemon better = listPokemon.get(0);
        for (int i = 0; i < listPokemon.size(); i++) {
            if (listPokemon.get(i).getValue() > better.getValue()) {
                better = listPokemon.get(i);
            }
        }
        for (int i = 0; i < listPokemon.size(); i++) {
            Arena.updateEdge(listPokemon.get(i), g);
        }

        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(g);

        for (int i = 0; i < listPokemon.size(); i++) {
            if (wga.shortestPathDist(src, listPokemon.get(i).get_edge().getSrc())
                    <= wga.shortestPathDist(src, better.get_edge().getSrc())) {
                better = listPokemon.get(i);
            }
        }

        int startEdge = better.get_edge().getSrc();
        List<node_data> listNodes = wga.shortestPath(src, startEdge);
        listNodes.add(g.getNode(better.get_edge().getDest()));


        if (listNodes.size() > 1) {
            ans = listNodes.get(1).getKey();
        } else {
            ans = listNodes.get(0).getKey();
        }
        return ans;
    }


    public static int strategy2(directed_weighted_graph g, int src) {
        int ans = -1;
        List<CL_Pokemon> listPokemom = _ar.getPokemons();
        CL_Pokemon better = listPokemom.get(0);
        for (int i = 0; i < listPokemom.size(); i++) {
            Arena.updateEdge(listPokemom.get(i), g);
        }

        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(g);

        for (int i = 0; i < listPokemom.size(); i++) {
            if (wga.shortestPathDist(src, listPokemom.get(i).get_edge().getSrc())
                    <= wga.shortestPathDist(src, better.get_edge().getSrc())) {
                better = listPokemom.get(i);
            }
        }

        int startEdge = better.get_edge().getSrc();
        List<node_data> listNodes = wga.shortestPath(src, startEdge);
        listNodes.add(g.getNode(better.get_edge().getDest()));


        if (listNodes.size() > 1) {
            ans = listNodes.get(1).getKey();
        } else {
            ans = listNodes.get(0).getKey();
        }
        return ans;
    }

    //Eats pokemon by the best value(high score)
    private static int strategy1(directed_weighted_graph g, int src) {
        int ans = -1;
        List<CL_Pokemon> listPokemom = _ar.getPokemons();
        CL_Pokemon better = listPokemom.get(0);

        dw_graph_algorithms wga = new DWGraph_Algo();
        wga.init(g);

        for (int i = 0; i < listPokemom.size(); i++) {
            Arena.updateEdge(listPokemom.get(i), g);
        }

        for (int i = 0; i < listPokemom.size(); i++) {
            if ((listPokemom.get(i).getValue() > better.getValue())) {
                better = listPokemom.get(i);
            }
        }
        int startEdge = better.get_edge().getSrc();
        List<node_data> listNodes = wga.shortestPath(src, startEdge);
        listNodes.add(g.getNode(better.get_edge().getDest()));


        if (listNodes.size() > 1) {
            ans = listNodes.get(1).getKey();
        } else {
            ans = listNodes.get(0).getKey();
        }
        return ans;
    }


    private void init(game_service game,directed_weighted_graph graph) {

        ArrayList<CL_Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());
        for (int a = 0; a < pokemonList.size(); a++) {
            Arena.updateEdge(pokemonList.get(a), graph);
        }

        initArena(graph,pokemonList);
        createGUI(game);

        //Init position of agents
        try {
            String info = game.toString();
            JSONObject line;
            line = new JSONObject(info);
            JSONObject gameObject = line.getJSONObject("GameServer");
            int numOfAgents = gameObject.getInt("agents");

            for (int i = 0; i < numOfAgents ; i++) {
                CL_Pokemon c = pokemonList.get(i);
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

    public void initArena(directed_weighted_graph g, List<CL_Pokemon> p) {
        this._ar = new Arena();
        this._ar.setGraph(g);
        this._ar.setPokemons(p);
    }

    public void createGUI(game_service game) {
        _win = new GameFrame("test Ex2", _ar);
        _win.setVisible(true);
    }
}

