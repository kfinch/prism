package frontEnd;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import backEnd.GameRunner;

public class PrismFrontEnd extends JFrame {

	private GamePanel display;
	
	private GameRunner game;
	
	public PrismFrontEnd(){
		initUI();
	}
	
	private void initUI(){
		//initialize the frame
		setTitle("Prism");
		setSize(1100, 700); //TODO: tweak size
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
	
		//initialize game runner
		game = new GameRunner();
		display = game.display;
		
		//add panel
		add(display);
		
		//start game
		game.newGame();
	}
	
	public static void main(String[] args) {
        
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PrismFrontEnd game = new PrismFrontEnd();
					game.setVisible(true);
			}
		});
	}
}
