package gameClient;
import javax.swing.*;
import java.awt.*;

/**
 * This class represents a GUI class to present game on a graph.
 * which contains features of the game window and the creation of
 * a panel for drawing components on the frame window.
 */
public class GameFrame extends JFrame{

	GameFrame(String a, Arena ar) {
		super(a);
		initFrame();
		initGamePanel(ar);
	}

	private void initFrame(){
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/pictures/agent.png"));
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initGamePanel(Arena ar){
		GamePanel panel = new GamePanel(ar);
		this.add(panel);
	}

}
