package gameClient;
import javax.swing.*;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class GameFrame extends JFrame{

	GameFrame(String a, Arena ar) {
		super(a);
		initFrame();
		initGamePanel(ar);
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
