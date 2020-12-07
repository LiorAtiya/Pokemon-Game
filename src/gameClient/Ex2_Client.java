package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}

	@Override
	public void run() {
		int scenario_num = 11;
		game_service game = Game_Server_Ex2.getServer(23); // you have [0,23] games
		//	int id = 9999999;
		//	game.login(id);
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);

		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=1;

		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String result = game.toString();

		System.out.println(result);
		System.exit(0);
	}
	/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				dest = nextNode(gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = strategy1(g,src);
		return ans;
	}

	public static int strategy2(directed_weighted_graph g, int src){
		int ans = -1;
		List<CL_Pokemon> listPokemom = _ar.getPokemons();
		CL_Pokemon better = listPokemom.get(0);
		for(int i=0 ; i < listPokemom.size() ; i++){
			Arena.updateEdge(listPokemom.get(i),g);
		}

		dw_graph_algorithms wga = new DWGraph_Algo();
		wga.init(g);

		for(int i=0 ; i < listPokemom.size() ; i++){
			if(wga.shortestPathDist(src,listPokemom.get(i).get_edge().getSrc())
					<= wga.shortestPathDist(src,better.get_edge().getSrc())){
				better = listPokemom.get(i);
			}
		}

		int startEdge = better.get_edge().getSrc();
		List<node_data> listNodes = new ArrayList<>();
		listNodes.add(g.getNode(better.get_edge().getSrc()));

		//NEED TO CHECK WHY ITS NULL
		if(wga.shortestPath(src,startEdge) != null){
			listNodes = wga.shortestPath(src,startEdge);
		}

//		System.out.println("Shortest path: "+listN);
		listNodes.add(g.getNode(better.get_edge().getDest()));


		if(listNodes.size() > 1){
			ans = listNodes.get(1).getKey();
		}else{
			ans = listNodes.get(0).getKey();
		}
		return ans;
	}

	//Eats pokemon by the best value(high score)
	private static int strategy1(directed_weighted_graph g, int src){
		int ans = -1;
		List<CL_Pokemon> listPokemom = _ar.getPokemons();
		CL_Pokemon better = listPokemom.get(0);

		dw_graph_algorithms wga = new DWGraph_Algo();
		wga.init(g);

		for(int i=0 ; i < listPokemom.size() ; i++){
			Arena.updateEdge(listPokemom.get(i),g);
		}

		for(int i=0 ; i < listPokemom.size() ; i++){
			if((listPokemom.get(i).getValue() >= better.getValue()) ||
					(wga.shortestPathDist(src,listPokemom.get(i).get_edge().getSrc())
							<= wga.shortestPathDist(src,better.get_edge().getSrc()))){
				better = listPokemom.get(i);
			}
		}
		int startEdge = better.get_edge().getSrc();
		List<node_data> listNodes = new ArrayList<>();
		listNodes.add(g.getNode(better.get_edge().getDest()));

		//NEED TO CHECK WHY ITS NULL
		if(wga.shortestPath(src,startEdge) != null){
			listNodes = wga.shortestPath(src,startEdge);
		}

//		System.out.println("Shortest path: "+listN);
		listNodes.add(g.getNode(better.get_edge().getDest()));

		ans = listNodes.get(1).getKey();
		return ans;
	}


	private void init(game_service game) {
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
//		//Option 2
//		dw_graph_algorithms myGraph = new DWGraph_Algo();
//		myGraph.load("data/A0");
		initArena(game,gg);
		createGUI();

		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");

			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

				game.addAgent(nn);
			}
			//Print detail of game
			System.out.println(info);
			System.out.println(game.getPokemons());
			System.out.println(game.getAgents());
		}
		catch (JSONException e) {e.printStackTrace();}
	}

	public void initArena(game_service game,directed_weighted_graph g){
		this._ar = new Arena();
		this._ar.setGraph(g);
		String fs = game.getPokemons();
		this._ar.setPokemons(Arena.json2Pokemons(fs));
	}

	public void createGUI(){
		_win = new MyFrame("test Ex2",this._ar);
		_win.setVisible(true);
	}
}

//package gameClient;
//
//		import Server.Game_Server_Ex2;
//		import api.directed_weighted_graph;
//		import api.edge_data;
//		import api.game_service;
//		import org.json.JSONException;
//		import org.json.JSONObject;
//
//		import java.util.ArrayList;
//		import java.util.Collection;
//		import java.util.Iterator;
//		import java.util.List;
//
//public class Ex2_Client implements Runnable{
//	private static MyFrame _win;
//	private static Arena _ar;
//	public static void main(String[] a) {
//		Thread client = new Thread(new Ex2_Client());
//		client.start();
//	}
//
//	@Override
//	public void run() {
//		int scenario_num = 11;
//		game_service game = Game_Server_Ex2.getServer(11); // you have [0,23] games
//		//	int id = 999;
//		//	game.login(id);
//		String g = game.getGraph();
//		String pks = game.getPokemons();
//		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
//		init(game);
//
//		game.startGame();
//		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
//		int ind=0;
//		long dt=100;
//
//		while(game.isRunning()) {
//			moveAgants(game, gg);
//			try {
//				if(ind%1==0) {_win.repaint();}
//				Thread.sleep(dt);
//				ind++;
//			}
//			catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//		String res = game.toString();
//
//		System.out.println(res);
//		System.exit(0);
//	}
//	/**
//	 * Moves each of the agents along the edge,
//	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
//	 * @param game
//	 * @param gg
//	 * @param
//	 */
//	private static void moveAgants(game_service game, directed_weighted_graph gg) {
//		String lg = game.move();
//		List<CL_Agent> log = Arena.getAgents(lg, gg);
//		_ar.setAgents(log);
//		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
//		String fs =  game.getPokemons();
//		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
//		_ar.setPokemons(ffs);
//		for(int i=0;i<log.size();i++) {
//			CL_Agent ag = log.get(i);
//			int id = ag.getID();
//			int dest = ag.getNextNode();
//			int src = ag.getSrcNode();
//			double v = ag.getValue();
//			if(dest==-1) {
//				dest = nextNode(gg, src);
//				game.chooseNextEdge(ag.getID(), dest);
//				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
//			}
//		}
//	}
//	/**
//	 * a very simple random walk implementation!
//	 * @param g
//	 * @param src
//	 * @return
//	 */
//	private static int nextNode(directed_weighted_graph g, int src) {
//		int ans = -1;
//		Collection<edge_data> ee = g.getE(src);
//		Iterator<edge_data> itr = ee.iterator();
//		int s = ee.size();
//		int r = (int)(Math.random()*s);
//		int i=0;
//		while(i<r) {itr.next();i++;}
//		ans = itr.next().getDest();
//		return ans;
//	}
//	private void init(game_service game) {
//		String g = game.getGraph();
//		String fs = game.getPokemons();
//		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
//		//gg.init(g);
//		_ar = new Arena();
//		_ar.setGraph(gg);
//		_ar.setPokemons(Arena.json2Pokemons(fs));
//		_win = new MyFrame("test Ex2");
//		_win.setSize(1000, 700);
//		_win.update(_ar);
//
//
//		_win.show();
//		String info = game.toString();
//		JSONObject line;
//		try {
//			line = new JSONObject(info);
//			JSONObject ttt = line.getJSONObject("GameServer");
//			int rs = ttt.getInt("agents");
//			System.out.println(info);
//			System.out.println(game.getPokemons());
//			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
//			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
//			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
//			for(int a = 0;a<rs;a++) {
//				int ind = a%cl_fs.size();
//				CL_Pokemon c = cl_fs.get(ind);
//				int nn = c.get_edge().getDest();
//				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
//
//				game.addAgent(nn);
//			}
//		}
//		catch (JSONException e) {e.printStackTrace();}
//	}
//}