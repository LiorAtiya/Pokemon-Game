package test;

import Classes.DWGraph_Algo;
import Server.Game_Server_Ex2;
import api.*;
import gameClient.Agent;
import gameClient.Arena;
import gameClient.Pokemon;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class SimpleMainGameTest {

    private static Arena _ar;
    private static long dt = 0;
    private static String info = "";

    public static void main(String[] a) {
        CreateFile("TotalGrades.txt");
        runGame(0);
        runGame(1);
        runGame(2);
        runGame(3);
        runGame(4);
        runGame(5);
        runGame(6);
        runGame(7);
        runGame(8);
        runGame(9);
        runGame(10);
        runGame(11);
        runGame(12);
        runGame(13);
        runGame(14);
        runGame(15);
        runGame(16);
        runGame(17);
        runGame(18);
        runGame(19);
        runGame(20);
        runGame(21);
        runGame(22);
        runGame(23);
        Write2File("TotalGrades.txt", info);
    }

    public static void CreateFile(String file) {
        try {
            File myObj = new File(file);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void Write2File(String file, String info) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(info);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static void runGame(int num) {

        //Create game
        game_service game = Game_Server_Ex2.getServer(num); // you have [0,23] games
//        game.login(this.id);

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
        long dt = 0;

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                List<Agent> log = _ar.getAgents();
                Agent maxAg = log.get(0);
                for (int i = 0; i < log.size(); i++) {
                    if (log.get(i).getSpeed() > maxAg.getSpeed()) {
                        maxAg = log.get(i);
                    }
                }
                if (maxAg.getSpeed() == 1.0) dt = 120;
                else if (maxAg.getSpeed() == 2.0) dt = 110;
                else if (maxAg.getSpeed() == 5.0) dt = 100;
                Thread.sleep(dt);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String result = game.toString();
        System.out.println(result);
        info += result;
    }

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
                dest = nextNode(gg, src, ag);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + " | Value: " + v + " | Speed : " + s + " | Move to node: " + dest);
                System.out.println("Time left: " + seconds + " seconds");
            }
        }
    }

    private static int nextNode(directed_weighted_graph g, int src, Agent ag) {
        int ans = strategy2(g, src, ag);
        return ans;
    }

    public static int strategy2(directed_weighted_graph g, int src, Agent ag) {
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
            if ((path2index <= path2better)) {
                better = listPokemom.get(i);
//                ag.setTarget(pos);
            }
        }

        int startEdge = better.get_edge().getSrc();
        List<node_data> listNodes = wga.shortestPath(src, startEdge);
        listNodes.add(g.getNode(better.get_edge().getDest()));

        if (listNodes.size() == 2) {
            dt = 50;
        }
        if (listNodes.size() > 1) {
            ans = listNodes.get(1).getKey();
        } else {
            ans = listNodes.get(0).getKey();
        }
        return ans;
    }

    private static void init(game_service game, directed_weighted_graph graph) {

        ArrayList<Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());
        for (int a = 0; a < pokemonList.size(); a++) {
            Arena.updateEdge(pokemonList.get(a), graph);
        }

        initArena(graph, pokemonList);

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

    public static void initArena(directed_weighted_graph g, List<Pokemon> p) {
        _ar = new Arena();
        _ar.setGraph(g);
        _ar.setPokemons(p);
    }

}

