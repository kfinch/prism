package frontEnd;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import backEnd.GameRunner;

public class PrismFrontEnd extends JFrame {

	private static final String GAME_CARDNAME = "game";
	private static final String PREFS_CARDNAME = "prefs";
	private static final String MENU_CARDNAME = "menu";
	private static final String SCORES_CARDNAME = "scores";
	
	private JPanel containerPanel; //A container for the various cards
	
	private GamePanel gamePanel;
	private PreferencesPanel prefsPanel;
	private MenuPanel menuPanel;
	private ScoresPanel scoresPanel;
	
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
		game = new GameRunner(this);
		
		//initialize various panels
		gamePanel = game.display;
		menuPanel = new MenuPanel(this);
		prefsPanel = new PreferencesPanel();
		scoresPanel = new ScoresPanel();
		
		//initialize and add the container panel and its cards
    	containerPanel = new JPanel(new CardLayout());
    	containerPanel.add(gamePanel, GAME_CARDNAME);
    	containerPanel.add(menuPanel, MENU_CARDNAME);
    	containerPanel.add(prefsPanel, PREFS_CARDNAME);
    	containerPanel.add(scoresPanel, SCORES_CARDNAME);
    	add(containerPanel, BorderLayout.CENTER);
    	
    	//menu should show first
    	swapToMenuPanel();
	}
	
	public void swapToGamePanel(){
		((CardLayout)containerPanel.getLayout()).show(containerPanel, GAME_CARDNAME);
		gamePanel.requestFocus();
	}
	
	public void swapToMenuPanel(){
		((CardLayout)containerPanel.getLayout()).show(containerPanel, MENU_CARDNAME);
		menuPanel.requestFocus();
	}
	
	public void swapToPrefsPanel(){
		((CardLayout)containerPanel.getLayout()).show(containerPanel, PREFS_CARDNAME);
		prefsPanel.requestFocus();
	}
	
	public void swapToScoresPanel(){
		((CardLayout)containerPanel.getLayout()).show(containerPanel, SCORES_CARDNAME);
		scoresPanel.requestFocus();
	}
	
	public void newGame(){
		game.newGame();
	}
	
	public void quitGame(){
		System.exit(0);
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
