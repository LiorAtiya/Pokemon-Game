package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class GameFrame extends JFrame{
	private int _ind;

	GameFrame(String a, Arena ar) {
		super(a);
		initFrame();
		initGamePanel(ar);
		int _ind = 0;
	}

	private void initFrame(){
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initGamePanel(Arena ar){
		GamePanel panel = new GamePanel(ar);
		this.add(panel);
	}

}
