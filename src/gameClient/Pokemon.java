package gameClient;
import api.edge_data;
import gameClient.util.Point3D;

/**
 * The Pokemon class is designed for the Pokemon game
 * Each Pokemon object is in a random position on the graph, and contains a value and type.
 */
public class Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private int id;
	//	private double min_dist;
//	private int min_ro;

	public Pokemon(Point3D p, int t, double v, edge_data e) {
		_type = t;
		_value = v;
		_edge = e;
		_pos = p;
//		min_dist = -1;
//		min_ro = -1;
	}

	/**
	 * Defines an ID for Pokemon
	 * @param id
	 */
	public void setID(int id){
		this.id = id;
	}

	/**
	 * Gets an ID for Pokemon
	 * @return id
	 */
	public int getID(){
		return this.id;
	}

	/**
	 * Gets a edge on which is the Pokemon.
	 * @return edge
	 */
	public edge_data get_edge() {
		return _edge;
	}

	/**
	 * Defines a edge on which is the Pokemon.
	 * @param _edge
	 */
	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	/**
	 * Gets pokemon position on the graph (x,y,z)
	 * @return position
	 */
	public Point3D getLocation() {
		return _pos;
	}

	/**
	 * Pokemon type - found on a descending edge -> -1, found on an ascending edge -> 1.
	 * @return type
	 */
	public int getType() {return _type;}

	/**
	 * Gets the Pokemon value.
	 * @return value
	 */
	public double getValue() {return _value;}

	public String toString() {return "F:{v="+_value+", t="+_type+"}";}

//	public double getMin_dist() {
//		return min_dist;
//	}
//
//	public void setMin_dist(double mid_dist) {
//		this.min_dist = mid_dist;
//	}
//
//	public int getMin_ro() {
//		return min_ro;
//	}
//
//	public void setMin_ro(int min_ro) {
//		this.min_ro = min_ro;
//	}

	//	public Pokemon init_from_json(String json) {
//		Pokemon ans = null;
//		try {
//			JSONObject p = new JSONObject(json);
//			int id = p.getInt("id");
//
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		return ans;
//	}
}