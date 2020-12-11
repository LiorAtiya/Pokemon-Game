package gameClient;

import api.directed_weighted_graph;
import api.game_service;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;

	MyFrame(String a,Arena ar) {
		super(a);
		this._ar = ar;
		initFrame();
		updateFrame();
		initPanel();
	}

	private void initPanel() {
		MyPanel panel = new MyPanel(this._ar,this._w2f);
		this.add(panel);
	}

	private void initFrame() {
		this.setSize(1000, 600);
		centreWindow(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	private void updateFrame() {
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
	}

}
