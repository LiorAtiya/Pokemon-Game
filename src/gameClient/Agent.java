package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

/**
 * This class is used for a player (Agent) in the Pokemon game,
 * The role of the player is to collect a quantity of Pokemon objects on a graph.
 * This class contains the player's attributes and actions.
 */
public class Agent {
	private int _id;
	private geo_location _pos;
	private double _speed;
	private edge_data _curr_edge;
	private node_data _curr_node;
	private directed_weighted_graph _gg;
	private double _value;
//	private Pokemon _curr_fruit;
//	private long _sg_dt;
// 	public static final double EPS = 0.0001;
//	private static int _count = 0;
//	private static int _seed = 3331;
//	private geo_location target;

	public Agent(directed_weighted_graph g, int start_node) {
		_gg = g;
		setMoney(0);
		this._curr_node = _gg.getNode(start_node);
		_pos = _curr_node.getLocation();
		_id = -1;
		setSpeed(0);
	}

	/**
	 * Gets information about the agent from the server in the Json file and
	 * converts it to this type of object
	 * @param json
	 */
	public void update(String json) {
		JSONObject line;
		try {
			// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {_id = id;}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				double value = ttt.getDouble("value");
				this._pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Source node that on which the agent is located.
	 * @return current source node
	 */
	//@Override
	public int getSrcNode() {return this._curr_node.getKey();}

	/**
	 * Convert this object information to a Json file
	 * @return json file
	 */
	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this._id+","
				+ "\"value\":"+this._value+","
				+ "\"src\":"+this._curr_node.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.getSpeed()+","
				+ "\"pos\":\""+_pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;
	}

	/**
	 * Setting the agent score after eating Pokemon.
	 * @param v grade agent
	 */
	private void setMoney(double v) {_value = v;}

	/**
	 * @return Grade of the agent
	 */
	public double getValue() {
		return this._value;
	}

	/**
	 * Checks if the next target agent can be set or is on the way to a node and still impossible.
	 * @param dest dest of the agent.
	 * @return true = on node, false = on edge.
	 */
	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this._curr_node.getKey();
		this._curr_edge = _gg.getEdge(src, dest);
		if(_curr_edge!=null) {
			ans=true;
		}
		else {_curr_edge = null;}
		return ans;
	}

	/**
	 * Defines the current node that the agent is currently on.
	 * @param src
	 */
	public void setCurrNode(int src) {
		this._curr_node = _gg.getNode(src);
	}

	public String toString() {
		return toJSON();
	}

	/**
	 * @return Agent's ID
	 */
	public int getID() {
		return this._id;
	}

	/**
	 * @return Agent position by x, y, z
	 */
	public geo_location getLocation() {
		return _pos;
	}

	/**
	 * Defines the next destination of the agent(next node).
	 * @return -1 if the agent still on a edge, else define the next node.
	 */
	public int getNextNode() {
		int ans;
		if(this._curr_edge==null) {
			ans = -1;}
		else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	/**
	 * @return Agent speed.
	 */
	public double getSpeed() {
		return this._speed;
	}

	/**
	 * Sets the agent's speed based on eating Pokemons.
	 * @param v speed.
	 */
	public void setSpeed(double v) {
		this._speed = v;
	}

//	public Pokemon get_curr_fruit() {
//		return _curr_fruit;
//	}
//	public void set_curr_fruit(Pokemon curr_fruit) {
//		this._curr_fruit = curr_fruit;
//	}
//	public void set_SDT(long ddtt) {
//		long ddt = ddtt;
//		if(this._curr_edge!=null) {
//			double w = get_curr_edge().getWeight();
//			geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
//			geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
//			double de = src.distance(dest);
//			double dist = _pos.distance(dest);
//			if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
//				dist = _curr_fruit.getLocation().distance(this._pos);
//			}
//			double norm = dist/de;
//			double dt = w*norm / this.getSpeed();
//			ddt = (long)(1000.0*dt);
//		}
//		this.set_sg_dt(ddt);
//	}
//
//	public edge_data get_curr_edge() {
//		return this._curr_edge;
//	}
//	public long get_sg_dt() {
//		return _sg_dt;
//	}
//	public void set_sg_dt(long _sg_dt) {
//		this._sg_dt = _sg_dt;
//	}

	//	public void setTarget(geo_location pos){
//		this.target = pos;
//	}
//
//	public geo_location getTarget(){
//		return this.target;
//	}

	//	public boolean isMoving() {
//		return this._curr_edge!=null;
//	}

//	public String toString1() {
//		String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();
//		return ans;
//	}

}