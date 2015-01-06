package backEnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;

import frontEnd.GamePanel;

public class GameRunner implements ActionListener {

	public static final double STARTING_COLOR = 200;
	public static final double STARTING_FLUX = 100;
	public static final double MAXIMUM_FLUX = 200;
	public static final double COLOR_GAIN_RATE = 0.1;
	public static final double FLUX_GAIN_RATE = 0.1;
	
	public static final int DEFAULT_BOARD_WIDTH = 45;
	public static final int DEFAULT_BOARD_HEIGHT = 15;
	
	public static final int NO_ACTION = 0;
	public static final int ADD_RED_ACTION = 1;
	public static final int ADD_GREEN_ACTION = 2;
	public static final int ADD_BLUE_ACTION = 3;
	public static final int ADD_CONDUIT_ACTION = 4;
	
	public static final int STEP_DURATION = 25;
	
	public int actionSelected;
	
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
		
		actionSelected = NO_ACTION;
		
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
	
	public void nodeClicked(Node n){
		switch(actionSelected){
		case NO_ACTION: break;
		case ADD_RED_ACTION:
			System.out.println("Attempting to add red at (" + n.xLoc + "," + n.yLoc + ") clicked.");
			if(n.tower != null){
				n.tower.addRed(gameState);
			}
			else if(gameState.isValidTowerLocation(n.xLoc, n.yLoc)){
				Tower tower = new TowerR(n, n.xLoc, n.yLoc, gameState.frameNumber);
				gameState.addTower(n.xLoc, n.yLoc, tower);
			}
			break;
		case ADD_GREEN_ACTION:
			System.out.println("Attempting to add green at (" + n.xLoc + "," + n.yLoc + ") clicked.");
			if(n.tower != null){
				n.tower.addGreen(gameState);
			}
			else if(gameState.isValidTowerLocation(n.xLoc, n.yLoc)){
				Tower tower = new TowerG(n, n.xLoc, n.yLoc, gameState.frameNumber);
				gameState.addTower(n.xLoc, n.yLoc, tower);
			}
			break;
		case ADD_BLUE_ACTION:
			System.out.println("Attempting to add blue at (" + n.xLoc + "," + n.yLoc + ") clicked.");
			if(n.tower != null){
				n.tower.addBlue(gameState);
			}
			else if(gameState.isValidTowerLocation(n.xLoc, n.yLoc)){
				Tower tower = new TowerB(n, n.xLoc, n.yLoc, gameState.frameNumber);
				gameState.addTower(n.xLoc, n.yLoc, tower);
			}
			break;
		case ADD_CONDUIT_ACTION:
			System.out.println("Attempting to add conduit at (" + n.xLoc + "," + n.yLoc + ") clicked.");
			if(gameState.isValidTowerLocation(n.xLoc, n.yLoc)){
				Tower tower = new TowerConduit(n, n.xLoc, n.yLoc, gameState.frameNumber);
				gameState.addTower(n.xLoc, n.yLoc, tower);
			}
			break;
		default: throw new IllegalArgumentException("Invalid action type: " + actionSelected);
		}
	}
	
	public void redSelected(){
		actionSelected = ADD_RED_ACTION;
	}
	
	public void greenSelected(){
		actionSelected = ADD_GREEN_ACTION;
	}
	
	public void blueSelected(){
		actionSelected = ADD_BLUE_ACTION;
	}
	
	public void conduitSelected(){
		actionSelected = ADD_CONDUIT_ACTION;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		step();
	}
}
