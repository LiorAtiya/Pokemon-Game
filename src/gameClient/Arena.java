package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * The Arena class is used to store the update information coming from the server
 *  regarding the game in real time.
 *  Which contains the game graph, a list of Pokemon and a list of agents and their location and more.
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private static List<Agent> _agents;
	private List<Pokemon> _pokemons;
	private Map<String,Integer> _info;
	private long timer;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);

	/**
	 * Defines a list of Object Pokemon
	 * @param f list pokemons
	 */
	public void setPokemons(List<Pokemon> f) {
		this._pokemons = f;
	}
	/**
	 * Gets a list of Object Pokemon
	 * @return list pokemons
	 */
	public List<Pokemon> getPokemons() {return _pokemons;}

	/**
	 * Defines a list of Object Agent
	 * @param f list agent
	 */
	public void setAgents(List<Agent> f) {
		this._agents = f;
	}

	/**
	 * Gets a list of Object Agent
	 * @return list agent
	 */
	public static List<Agent> getAgents() {return _agents;}

	/**
	 * Defines the time to end of the game
	 * @param time in seconds
	 */
	public void setTimeToEnd(long time){
		this.timer = time;
	}

	/**
	 * Gets the time to end of the game
	 * @return time in seconds
	 */
	public long getTimeToEnd(){
		return this.timer;
	}

	/**
	 * Defines the graph of the game
	 * @param g graph
	 */
	public void setGraph(directed_weighted_graph g) {this._gg =g;}

	/**
	 * Gets the the graph of the game
	 * @return graph
	 */
	public directed_weighted_graph getGraph() {
		return _gg;
	}

	/**
	 * Defines the the info of the game: moves, grade and game level.
	 * @param _info info string from file json
	 */
	public void set_info(String _info) {
		Map<String,Integer> info = new HashMap<>();
		try {
			JSONObject infoObj = new JSONObject(_info);
			int moves = infoObj.getJSONObject("GameServer").getInt("moves");
			int grade = infoObj.getJSONObject("GameServer").getInt("grade");
			int gameLevel = infoObj.getJSONObject("GameServer").getInt("game_level");

			info.put("moves",moves);
			info.put("grade", grade);
			info.put("gameLevel", gameLevel);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		this._info = info;

	}

	/**
	 * Gets the the info of the game: moves, grade and game level.
	 * @return Map of the info
	 */
	public Map<String,Integer> get_info() {
		return _info;
	}

	/**
	 * Gets information about the agents from the json file and converts them to
	 * a list of Agent objects
	 * @param aa json file
	 * @param gg graph
	 * @return list of Agent
	 */
	public static List<Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<Agent> ans = new ArrayList<Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				Agent c = new Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	/**
	 *Gets Pokemon information from the Json file and converts it to a list of Pokemon objects.
	 * @param fs json file
	 * @return list of pokemon
	 */
	public static ArrayList<Pokemon> json2Pokemons(String fs) {
		ArrayList<Pokemon> ans = new ArrayList<Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				String p = pk.getString("pos");
				Pokemon f = new Pokemon(new Point3D(p), t, v, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}

	/**
	 * Updates the position of the Pokemon on a edge of the graph.
	 * @param fr Pokemon
	 * @param g graph
	 */
	public static void updateEdge(Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {fr.set_edge(e);}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}

	/**
	 * Calculate the range of the graph by x, y.
	 * @param g graph
	 * @return range of graph
	 */
	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}

	/**
	 * Converts the graph according to the position of the nodes and edges to the frame window (Used for GUI).
	 * @param g graph
	 * @param frame size of window
	 * @return position suitable for frame
	 */
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

	//	private void init( ) {
//		MIN=null; MAX=null;
//		double x0=0,x1=0,y0=0,y1=0;
//		Iterator<node_data> iter = _gg.getV().iterator();
//		while(iter.hasNext()) {
//			geo_location c = iter.next().getLocation();
//			if(MIN==null) {x0 = c.x(); y0=c.y(); x1=x0;y1=y0;MIN = new Point3D(x0,y0);}
//			if(c.x() < x0) {x0=c.x();}
//			if(c.y() < y0) {y0=c.y();}
//			if(c.x() > x1) {x1=c.x();}
//			if(c.y() > y1) {y1=c.y();}
//		}
//		double dx = x1-x0, dy = y1-y0;
//		MIN = new Point3D(x0-dx/10,y0-dy/10);
//		MAX = new Point3D(x1+dx/10,y1+dy/10);
//
//	}

}