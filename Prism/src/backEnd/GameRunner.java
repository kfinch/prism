package backEnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;

import frontEnd.GamePanel;

public class GameRunner implements ActionListener, MouseListener, KeyListener {

	public static final double STARTING_COLOR = 200;
	public static final double STARTING_FLUX = 100;
	public static final double MAXIMUM_FLUX = 200;
	public static final double COLOR_GAIN_RATE = 0.1;
	public static final double FLUX_GAIN_RATE = 0.1;
	
	public static final int DEFAULT_BOARD_WIDTH = 70;
	public static final int DEFAULT_BOARD_HEIGHT = 30;
	
	public static final int STEP_DURATION = 25;
	
	public int boardWidth;
	public int boardHeight;
	
	public GameState gameState;
	public GamePanel display;
	
	public double redResources;
	public double greenResources;
	public double blueResources;
	public double fluxResources;
	
	private Timer gameClock;
	
	public GameRunner(){
		boardWidth = DEFAULT_BOARD_WIDTH;
		boardHeight = DEFAULT_BOARD_HEIGHT;
		gameState = new GameState(boardWidth, boardHeight);
		display = new GamePanel(this);
		
		gameClock = new Timer(STEP_DURATION, this);
	}

	public void newGame(){
		gameState = new GameState(boardWidth, boardHeight);
		redResources = STARTING_COLOR;
		greenResources = STARTING_COLOR;
		blueResources = STARTING_COLOR;
		fluxResources = STARTING_FLUX;
		gameClock.restart();
	}
	
	public void step(){
		redResources += COLOR_GAIN_RATE;
		greenResources += COLOR_GAIN_RATE;
		blueResources += COLOR_GAIN_RATE;
		fluxResources += FLUX_GAIN_RATE;
		if(fluxResources > MAXIMUM_FLUX)
			fluxResources = MAXIMUM_FLUX;
		
		gameState.step();
		
		display.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		step();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
