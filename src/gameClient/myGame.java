package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class myGame {

    public static void main(String[] args){
        test1();
//        game_service game = Game_Server_Ex2.getServer(1);
//        System.out.println(getArrayPokemons(game).get(0).getType());

    }

    public static void test1() {
        //Create new game
        game_service game = Game_Server_Ex2.getServer(1); // you have [0,23] games
        //Details of game
        String g = game.getGraph();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        String info = game.toString();
        System.out.println(info);
        System.out.println(g);

//        dw_graph_algorithms dgw = new DWGraph_Algo();
//        dgw.load("data/A0");

        int src_node = 0;  // arbitrary node, you should start at one of the fruits
        game.addAgent(src_node);
        game.startGame();


        int i=0;
        while(game.isRunning()) {
            long t = game.timeToEnd();
            int seconds = (int) ((t / 1000) % 60);

            String lg = game.move();
            List<CL_Agent> log = Arena.getAgents(lg, gg);

            for(int a=0; a < log.size(); a++) {
                CL_Agent r = log.get(a);
                int dest = r.getNextNode();
                int src = r.getSrcNode();
                int id = r.getID();
                //Came to node so the dest is -1
                if(dest==-1) {

                    int new_dest = nextNode(gg, src);
                    game.chooseNextEdge(id, new_dest);
                    System.out.println(i+") "+a+") "+r+"  move to node: "+new_dest);
                    System.out.println("Time left: "+seconds+" seconds");
                }
            }
            i++;
        }

        System.out.println("Score game is:"+scoreGame(game));
    }

    public static double scoreGame(game_service game){
        double value = 0;
        JSONObject line;
        try {
            line = new JSONObject(game.getAgents());
            JSONArray arr = line.getJSONArray("Agents");
            for(int i=0 ; i < arr.length() ; i++){
                value += arr.getJSONObject(i).getJSONObject("Agent").getDouble("value");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    //Need to rewrite
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;


        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }

    private static ArrayList<CL_Pokemon> getArrayPokemons(game_service game) {

        GsonBuilder builder = new GsonBuilder();
        JsonDeserializer<ArrayPokemons> PokemonObject = new JsonDeserializer<ArrayPokemons>() {
            @Override
            public ArrayPokemons deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                JsonArray pokArray = jsonObject.get("Pokemons").getAsJsonArray();

                ArrayPokemons list = new ArrayPokemons();

                for (JsonElement n : pokArray) {
                    double value = n.getAsJsonObject().get("Pokemon").getAsJsonObject().get("value").getAsDouble();
                    int t = n.getAsJsonObject().get("Pokemon").getAsJsonObject().get("type").getAsInt();
                    String pos = n.getAsJsonObject().get("Pokemon").getAsJsonObject().get("pos").getAsString();
                    String[] parts = pos.split(",");
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    double z = Double.parseDouble(parts[2]);
                    Point3D p = new Point3D(x, y, z);
                    CL_Pokemon pok = new CL_Pokemon(p, t, value, null);
                    list.add(pok);
                }
                return list;
            }
        };

        try{
            builder.registerTypeAdapter(ArrayPokemons.class, PokemonObject);
            Gson customGson = builder.create();

            ArrayPokemons ap = customGson.fromJson(game.getPokemons(),ArrayPokemons.class);
            return ap.ArrayPokemons();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static class ArrayPokemons{
        private ArrayList<CL_Pokemon> list = new ArrayList<>();

        public ArrayList<CL_Pokemon> ArrayPokemons(){
            return this.list;
        }

        public void add(CL_Pokemon p){
            list.add(p);
        }
    }

}
