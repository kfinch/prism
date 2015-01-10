package backEnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Set;

import javax.swing.Timer;

import frontEnd.GamePanel;
import frontEnd.PrismFrontEnd;

public class GameRunner implements ActionListener {

	public static final String BUILD_ERROR_BLOCKED = "Can't build here, there's something in the way.";
	public static final String BUILD_ERROR_INSUFFICIENT_RESOURCES = "You have insufficient resources";
	
	public static final double STARTING_COLOR = 4000; //400
	public static final double STARTING_FLUX = 100;
	public static final double MAXIMUM_FLUX = 200;
	public static final double COLOR_GAIN_RATE = 0.1;
	public static final double FLUX_GAIN_RATE = 0.1;
	
	public static final double TOWER_COST_MULTIPLIER = 10; //100
	public static final double CONDUIT_TOWER_COST = 10; //100
	
	public static final int WAVE_DURATION = 1200;
	public static final int WAVE_DOWNTIME = 400;
	
	public static final int DEFAULT_BOARD_WIDTH = 45;
	public static final int DEFAULT_BOARD_HEIGHT = 15;
	
	public static final int NO_ACTION = 0;
	public static final int ADD_RED_ACTION = 1;
	public static final int ADD_GREEN_ACTION = 2;
	public static final int ADD_BLUE_ACTION = 3;
	public static final int ADD_CONDUIT_ACTION = 4;
	public static final int SELL_TOWER_ACTION = 5;
	
	public static final int STEP_DURATION = 25;
	
	public int actionSelected;
	
	public int boardWidth;
	public int boardHeight;
	
	public GameState gameState;
	public GamePanel display;
	public PrismFrontEnd frontEnd;
	
	public int currentTier;
	public Enemy waveEnemies;
	public int spawnDelay;
	
	private Timer gameClock;
	
	public boolean isPaused;
	
	public Node nodeMouseOver;
	public Tower towerMouseOver;
	
	public GameRunner(PrismFrontEnd frontEnd){
		this.boardWidth = DEFAULT_BOARD_WIDTH;
		this.boardHeight = DEFAULT_BOARD_HEIGHT;
		this.gameState = new GameState(boardWidth, boardHeight);
		this.display = new GamePanel(this);
		this.frontEnd = frontEnd;
		
		this.actionSelected = NO_ACTION;
		
		this.gameClock = new Timer(STEP_DURATION, this);
	}
	
	public void step(){
		if(gameState.frameNumber % (WAVE_DURATION + WAVE_DOWNTIME) == 0){
			currentTier++;
			waveEnemies = new EnemyNibbler(currentTier, null, 0, 0, 0);
			spawnDelay = WAVE_DURATION / waveEnemies.getWaveSize();
		}
		
		if(gameState.frameNumber % (WAVE_DURATION + WAVE_DOWNTIME) <= WAVE_DURATION &&
		   gameState.frameNumber % spawnDelay == 0){ //TODO: do something about this, this is super awkward
			gameState.spawnEnemy(waveEnemies);
		}
		
		gameState.step();
		display.repaint();
	}

	public void newGame(){
		gameState = new GameState(boardWidth, boardHeight);
		currentTier = 1; //this will need change as I implement different difficulties
		pause();
	}
	
	public void pause(){
		gameClock.stop();
		isPaused = true;
	}
	
	public void unpause(){
		gameClock.restart();
		isPaused = false;
	}
	
	public void quitToMenu(){
		gameClock.stop();
		isPaused = true;
		frontEnd.swapToMenuPanel();
	}
	
	public void boardMouseOver(double xLoc, double yLoc){
		if(xLoc < 0 || xLoc > gameState.xNodes || yLoc < 0 || yLoc > gameState.yNodes){
			towerMouseOver = null;
			nodeMouseOver = null;
			return;
		}
		
		int x = (int) Math.round(xLoc);
		int y = (int) Math.round(yLoc);
		towerMouseOver = null;
		nodeMouseOver = gameState.nodeAt(x, y);
		
		Set<Tower> towersMouseOver = gameState.getTowersInEdgeRange(xLoc, yLoc, 0);
		if(!towersMouseOver.isEmpty()){
			towerMouseOver = towersMouseOver.iterator().next();
			nodeMouseOver = towerMouseOver.currNode;
		}
	}
	
	public void boardClicked(double xLoc, double yLoc){
		boardMouseOver(xLoc, yLoc);
		
		switch(actionSelected){
		case NO_ACTION: break;
		case ADD_RED_ACTION:
			if(towerMouseOver != null)
				upgradeRed(towerMouseOver);
			else if(nodeMouseOver != null)
				newRedTower(nodeMouseOver);
			break;
		case ADD_GREEN_ACTION:
			if(towerMouseOver != null)
				upgradeGreen(towerMouseOver);
			else if(nodeMouseOver != null)
				newGreenTower(nodeMouseOver);
			break;
		case ADD_BLUE_ACTION:
			if(towerMouseOver != null)
				upgradeBlue(towerMouseOver);
			else if(nodeMouseOver != null)
				newBlueTower(nodeMouseOver);
			break;
		case ADD_CONDUIT_ACTION:
			newConduitTower(nodeMouseOver);
			break;
		case SELL_TOWER_ACTION:
			if(towerMouseOver != null){
				towerMouseOver.sell(gameState);
			}
			break;
		default: throw new IllegalArgumentException("Invalid action type: " + actionSelected);
		}
		display.repaint(); //reflect any UI changes made here, even if paused
	}
	
	private String newRedTower(Node n){
		if(gameState.redResources < TOWER_COST_MULTIPLIER)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		if(!gameState.isValidTowerLocation(n.xLoc, n.yLoc))
			return BUILD_ERROR_BLOCKED;
		
		gameState.redResources -= TOWER_COST_MULTIPLIER;
		Tower tower = new TowerR(n, n.xLoc, n.yLoc, gameState.frameNumber);
		gameState.addTower(n.xLoc, n.yLoc, tower);
		return null;
	}
	
	private String newGreenTower(Node n){
		if(gameState.greenResources < TOWER_COST_MULTIPLIER)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		if(!gameState.isValidTowerLocation(n.xLoc, n.yLoc))
			return BUILD_ERROR_BLOCKED;
		
		gameState.greenResources -= TOWER_COST_MULTIPLIER;
		Tower tower = new TowerG(n, n.xLoc, n.yLoc, gameState.frameNumber);
		gameState.addTower(n.xLoc, n.yLoc, tower);
		return null;
	}
	
	private String newBlueTower(Node n){
		if(gameState.blueResources < TOWER_COST_MULTIPLIER)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		if(!gameState.isValidTowerLocation(n.xLoc, n.yLoc))
			return BUILD_ERROR_BLOCKED;
		
		gameState.blueResources -= TOWER_COST_MULTIPLIER;
		Tower tower = new TowerB(n, n.xLoc, n.yLoc, gameState.frameNumber);
		gameState.addTower(n.xLoc, n.yLoc, tower);
		return null;
	}
	
	private String newConduitTower(Node n){
		if(gameState.fluxResources < CONDUIT_TOWER_COST)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		if(!gameState.isValidTowerLocation(n.xLoc, n.yLoc))
			return BUILD_ERROR_BLOCKED;
		
		gameState.fluxResources -= CONDUIT_TOWER_COST;
		Tower tower = new TowerConduit(n, n.xLoc, n.yLoc, gameState.frameNumber);
		gameState.addTower(n.xLoc, n.yLoc, tower);
		return null;
	}
	
	private String upgradeRed(Tower t){
		double cost = TOWER_COST_MULTIPLIER * Math.pow(2, t.tier);
		if(gameState.redResources < cost)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		String message = t.addRed(gameState);
		if(message == null)
			gameState.redResources -= cost;
		return message;
	}
	
	private String upgradeGreen(Tower t){
		double cost = TOWER_COST_MULTIPLIER * Math.pow(2, t.tier);
		if(gameState.greenResources < cost)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		String message = t.addGreen(gameState);
		if(message == null)
			gameState.greenResources -= cost;
		return message;
	}
	
	private String upgradeBlue(Tower t){
		double cost = TOWER_COST_MULTIPLIER * Math.pow(2, t.tier);
		if(gameState.blueResources < cost)
			return BUILD_ERROR_INSUFFICIENT_RESOURCES;
		String message = t.addBlue(gameState);
		if(message == null)
			gameState.blueResources -= cost;
		return message;
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
	
	public void sellSelected(){
		actionSelected = SELL_TOWER_ACTION;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		step();
	}
}
