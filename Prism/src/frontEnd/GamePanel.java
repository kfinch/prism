package frontEnd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import util.Animation;
import util.Point2d;
import backEnd.Buff;
import backEnd.Enemy;
import backEnd.EnemyWave;
import backEnd.EnemyWaveGenerator;
import backEnd.Entity;
import backEnd.GameRunner;
import backEnd.GameState;
import backEnd.LightSource;
import backEnd.WaveModifier;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	protected int panelWidth, panelHeight; //size of game panel in pixels
	protected int tileSize; //size of game tiles, in pixels
	protected int unitSize; //size in pixels of a 'unit' used to preserve aspect ratio. Panel UI fits in 20x12 units.
	protected int boardWidth, boardHeight; //size of game board in pixels
	protected int xBoardLeft, xBoardRight;
	protected int yLineTop, yLineBot1, yLineBot2;
	protected int xLineLeft1, xLineLeft2, xLineCenter1, xLineCenter2, xLineRight1, xLineRight2;
	
	protected int xNodes, yNodes; //size of game board, in tiles
	
	private GameRunner game; //reference to game runner (and thus the current game state to be displayed)
	
	private int mouseX, mouseY; //mouse's current location
	
	public GamePanel(GameRunner game){
		this.game = game;
		
		//adds listeners to receive input
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		
		mouseX = 0;
		mouseY = 0;
	}
	
	/*
	 * Updates dimensional variables used to draw the game panel.
	 * GamePanel allows for a resizable window, but its expected aspect ratio will always be 10:6
	 */
	private void updateDimensions(){
		panelWidth = getSize().width;
		panelHeight = getSize().height;
		
		xNodes = game.gameState.xNodes;
		yNodes = game.gameState.yNodes;
		
		//calculate tile size
		tileSize = (int) Math.min(panelWidth/(xNodes+6), (panelHeight*0.6)/(yNodes));
		
		//calculate unit size
		unitSize = Math.min(panelWidth/20, panelHeight/12);
		
		//generate board dimensions
		boardWidth = tileSize*xNodes;
		boardHeight = tileSize*yNodes;
		xBoardLeft = (panelWidth-boardWidth)/2;
		xBoardRight = xBoardLeft + boardWidth;
		
		//generate ui y-lines
		yLineTop = unitSize;
		yLineBot1 = (int) (unitSize*7.25);
		yLineBot2 = (int) (unitSize*8.5);
		
		//generate ui x-lines
		xLineLeft1 = (panelWidth/2) - 10*unitSize;
		xLineLeft2 = (panelWidth/2) - 5*unitSize;
		xLineCenter1 = (panelWidth/2) - 4*unitSize;
		xLineCenter2 = (panelWidth/2) + 1*unitSize;
		xLineRight1 = (panelWidth/2) + 2*unitSize;
		xLineRight2 = (panelWidth/2) + 10*unitSize;
		
		//TODO: remove debugging
		//System.out.println("DIMENSIONS: panelWidth=" + panelWidth + " panelHeight=" + panelHeight);
		//System.out.println("xNodes=" + xNodes + " yNodes=" + yNodes);
		//System.out.println("tileSize=" + tileSize + " unitSize=" + unitSize);
		//System.out.println("boardWidth=" + boardWidth + " boardHeight=" + boardHeight);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		updateDimensions();
		doPainting((Graphics2D)g);
	}
	
	private void doPainting(Graphics2D g2d){
		//draw light levels
		g2d.setColor(GameState.BACKGROUND_DARK);
		g2d.fillRect(xBoardLeft, yLineTop, boardWidth, boardHeight);
		g2d.setColor(GameState.BACKGROUND_LIGHT);
		for(LightSource ls : game.gameState.lightSources){
			double radiance = ls.lightRadius();
			Point2d loc = ls.getLocation();
			int x = xBoardLocToScreenLoc(loc.x - radiance);
			int y = yBoardLocToScreenLoc(loc.y - radiance);
			int diameter = (int) (radiance * 2 * tileSize);
			g2d.fillOval(x, y, diameter, diameter);
		}
		
		//dot individual nodes
		g2d.setColor(Color.black);
		int dotSize = 4;
		for(int i=0; i<xNodes; i++){
			for(int j=0; j<yNodes; j++){
				g2d.fillOval(xBoardLeft + i*tileSize + tileSize/2 - dotSize/2,
						     yLineTop + j*tileSize + tileSize/2 - dotSize/2,
						     dotSize, dotSize);
			}
		}
		
		//draw path lines
		//TODO: implement
		
		//draw entities
		for(Entity e : game.gameState.towers)
			e.paintEntity(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		
		for(Entity e : game.gameState.enemies)
			e.paintEntity(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		
		for(Entity e : game.gameState.projectiles)
			e.paintEntity(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		
		for(Entity e : game.gameState.miscEntities)
			e.paintEntity(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		
		//draw animations
		for(Animation a : game.gameState.animations)
			a.paintAnimation(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		
		//draw mouse-over box
		if(game.nodeMouseOver != null){
			switch(game.actionSelected){
			case GameRunner.ADD_RED_ACTION: 
				g2d.setColor(new Color(200, 40, 40, 100));
				break;
			case GameRunner.ADD_GREEN_ACTION: 
				g2d.setColor(new Color(40, 200, 40, 100));
				break;
			case GameRunner.ADD_BLUE_ACTION: 
				g2d.setColor(new Color(40, 40, 200, 100));
				break;
			case GameRunner.ADD_CONDUIT_ACTION: 
				g2d.setColor(new Color(200, 200, 200, 100));
				break;
			case GameRunner.TELEPORT_TOWER_SRC_ACTION:
				g2d.setColor(new Color(255, 240, 15, 50));
				break;
			case GameRunner.TELEPORT_TOWER_DST_ACTION:
				g2d.setColor(new Color(255, 240, 15, 150));
				break;
			case GameRunner.SELL_TOWER_ACTION:
				g2d.setColor(new Color(200, 200, 200, 150));
				break;
			default:
				g2d.setColor(new Color(200, 200, 200, 0));
				break;
			}
			g2d.fillRect(xBoardLocToScreenLoc(game.nodeMouseOver.xLoc) - tileSize,
					     yBoardLocToScreenLoc(game.nodeMouseOver.yLoc) - tileSize,
					     tileSize*2, tileSize*2);
		}
		
		//draw mouse over ranges
		if(game.towerMouseOver != null){
			float[] dash = {8.0f};
			g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
			g2d.setColor(GameState.UI_GOLD);
			double range = game.towerMouseOver.attackRange.modifiedValue;
			int x = xBoardLocToScreenLoc(game.towerMouseOver.loc.x - range);
			int y = yBoardLocToScreenLoc(game.towerMouseOver.loc.y - range);
			int diameter = (int) (range * 2 * tileSize);
			if(diameter != 0)
				g2d.drawOval(x, y, diameter, diameter);
		}
		
		//highlight tower to teleport
		if(game.actionSelected == GameRunner.TELEPORT_TOWER_DST_ACTION){
			g2d.setColor(GameState.UI_GOLD);
			g2d.drawRect(xBoardLocToScreenLoc(game.towerToTeleport.loc.x - tileSize),
					     yBoardLocToScreenLoc(game.towerToTeleport.loc.y - tileSize),
					     tileSize*2, tileSize*2);
		}
		
		//box out anything that leaks...
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, xBoardLeft, panelHeight);
		g2d.fillRect(0, 0, panelWidth, yLineTop);
		g2d.fillRect(xBoardLeft + tileSize*xNodes, 0, panelWidth, panelHeight);
		g2d.fillRect(0, yLineTop + tileSize*yNodes, panelWidth, panelHeight);
		
		//draw the prisms! (which leaks by design)
		game.gameState.prism.paintEntity(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		game.gameState.darkPrism.paintEntity(g2d, xBoardLeft + tileSize/2, yLineTop + tileSize/2, tileSize);
		
		//draw resource counts
		g2d.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16)); //TODO: make scaling font size
		
		//TODO: redo the placing of these
		g2d.setColor(Color.red);
		g2d.drawString("RED: " + ((int) game.gameState.redResources), (panelWidth/2)-unitSize*6, yLineTop - 5);
		
		g2d.setColor(Color.green);
		g2d.drawString("GREEN: " + ((int) game.gameState.greenResources), (panelWidth/2)-unitSize*3, yLineTop - 5);
		
		g2d.setColor(Color.blue);
		g2d.drawString("BLUE: " + ((int) game.gameState.blueResources), (panelWidth/2)+unitSize, yLineTop - 5);
		
		g2d.setColor(Color.gray);
		g2d.drawString("FLUX: " + ((int) game.gameState.fluxResources), (panelWidth/2)+unitSize*4, yLineTop - 5);
		
		//draw tool panel (and highlight the current selection) (5x4 units)
		int subUnitSize = unitSize/10;
		int fontSize = (int) (unitSize*0.7); //TODO: tweak this, obviously (maybe also make a global font size?)
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
		g2d.setStroke(new BasicStroke(3)); //TODO: should this be a static 3, or variable as well?
		
		g2d.setColor(Color.lightGray);
		g2d.fillRect(xLineLeft1, yLineBot2, unitSize*5, panelHeight); //draws all the way to bottom of screen
		
		g2d.setColor(Color.red);
		g2d.drawString("R", xLineLeft1 + 5*subUnitSize, yLineBot2 + fontSize + 4*subUnitSize);
		if(game.actionSelected == GameRunner.ADD_RED_ACTION)
			g2d.drawRect(xLineLeft1 + 3*subUnitSize, yLineBot2 + 3*subUnitSize, 10*subUnitSize, 10*subUnitSize);
		
		g2d.setColor(Color.green);
		g2d.drawString("G", xLineLeft1 + 20*subUnitSize, yLineBot2 + fontSize + 4*subUnitSize);
		if(game.actionSelected == GameRunner.ADD_GREEN_ACTION)
			g2d.drawRect(xLineLeft1 + 18*subUnitSize, yLineBot2 + 3*subUnitSize, 10*subUnitSize, 10*subUnitSize);
		
		g2d.setColor(Color.blue);
		g2d.drawString("B", xLineLeft1 + 35*subUnitSize, yLineBot2 + fontSize + 4*subUnitSize);
		if(game.actionSelected == GameRunner.ADD_BLUE_ACTION)
			g2d.drawRect(xLineLeft1 + 33*subUnitSize, yLineBot2 + 3*subUnitSize, 10*subUnitSize, 10*subUnitSize);
		
		g2d.setColor(Color.gray);
		g2d.drawString("C", xLineLeft1 + 5*subUnitSize, yLineBot2 + fontSize + 19*subUnitSize);
		if(game.actionSelected == GameRunner.ADD_CONDUIT_ACTION)
			g2d.drawRect(xLineLeft1 + 3*subUnitSize, yLineBot2 + 18*subUnitSize, 10*subUnitSize, 10*subUnitSize);
		
		g2d.setColor(Color.black);
		g2d.drawString("S", xLineLeft1 + 20*subUnitSize, yLineBot2 + fontSize + 19*subUnitSize);
		if(game.actionSelected == GameRunner.SELL_TOWER_ACTION)
			g2d.drawRect(xLineLeft1 + 18*subUnitSize, yLineBot2 + 18*subUnitSize, 10*subUnitSize, 10*subUnitSize);
		
		g2d.setColor(GameState.UI_GOLD);
		g2d.drawString("T", xLineLeft1 + 35*subUnitSize, yLineBot2 + fontSize + 19*subUnitSize);
		if(game.actionSelected == GameRunner.TELEPORT_TOWER_SRC_ACTION ||
		   game.actionSelected == GameRunner.TELEPORT_TOWER_DST_ACTION)
			g2d.drawRect(xLineLeft1 + 33*subUnitSize, yLineBot2 + 18*subUnitSize, 10*subUnitSize, 10*subUnitSize);

		g2d.setStroke(new BasicStroke(1));
		
		//set up info bars
		Font panelFont = new Font(Font.SANS_SERIF, Font.BOLD, subUnitSize*3);
		Color panelFontColor = Color.black;
		
		//draw incoming waves bar
		/*
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(xLineCenter1, yLineBot1, xLineRight2 - xLineCenter1, unitSize*1);
		
		int spacing = unitSize*2;
		int currentX = xLineRight2;
		int nameY = yLineBot1 + 5*subUnitSize;
		int modY = yLineBot1 + 9*subUnitSize;
		
		g2d.setFont(panelFont);
		g2d.setColor(panelFontColor);
		for(EnemyWave wave : game.waveGenerator.getIncomingWaves()){
			currentX -= spacing;
			g2d.drawString(wave.enemy.name, currentX, nameY);
			if(wave.modifier != WaveModifier.NONE)
				g2d.drawString("*" + wave.modifier + "*", currentX, modY);
		}
		*/
		
		int currentTimeX = xLineRight2 - 2*unitSize;
		int futurePixels = 14*unitSize;
		int futureFrames = (game.waveGenerator.waveDuration + game.waveGenerator.waveDowntime) *
				           (EnemyWaveGenerator.DEFAULT_GENERATED_WAVE_BUFFER + 1);
		double pixelsPerFrame = (double)futurePixels / (double)futureFrames;
		
		LinkedList<EnemyWave> incomingWaves = game.waveGenerator.getIncomingWaves();
		if(!incomingWaves.isEmpty()){
			g2d.setFont(panelFont);
			int waveStartX = (int) (currentTimeX + game.waveGenerator.timeSinceWaveStart() * pixelsPerFrame);
			int waveEndX;
			for(EnemyWave wave : incomingWaves){
				waveEndX = (int) (waveStartX - game.waveGenerator.getEffectiveWaveDuration(wave) * pixelsPerFrame);
				g2d.setColor(Color.lightGray);
				g2d.fillRect(waveEndX, yLineBot1, waveStartX-waveEndX, unitSize);
				g2d.setColor(Color.black);
				g2d.drawString(wave.enemy.name, waveEndX + subUnitSize, yLineBot1 + 4*subUnitSize);
				if(wave.modifier != WaveModifier.NONE)
					g2d.drawString("*" + wave.modifier + "*", waveEndX + subUnitSize, yLineBot1 + 9*subUnitSize);
				waveStartX -= (game.waveGenerator.getEffectiveWaveDuration(wave) +
						      game.waveGenerator.getEffectiveWaveDowntime(wave)) * pixelsPerFrame;
			}
		}
		
		g2d.setColor(GameState.UI_GOLD);
		g2d.setStroke(new BasicStroke(4));
		g2d.drawLine(currentTimeX, yLineBot1, currentTimeX, yLineBot1 + unitSize);
		g2d.setStroke(new BasicStroke(1));
		
		//draw tower info panel and wave info panel
		g2d.setColor(Color.lightGray);
		g2d.fillRect(xLineCenter1, yLineBot2, unitSize*5, panelHeight); //draws all the way to bottom of screen
		g2d.fillRect(xLineRight1, yLineBot2, unitSize*8, panelHeight); //draws all the way to bottom of screen
		
		//if tower mouseover, fill tower info panel
		if(game.towerMouseOver != null){
			//TODO: remove debugging prints (these of buffs)
			if(game.gameState.frameNumber % 20 == 0){
				System.out.println("buffs on curr tower: ");
				for(Buff b : game.towerMouseOver.buffs.values())
					System.out.println(b);
			}
			
			List<String> towerStats = new ArrayList<String>();
			
			towerStats.add(game.towerMouseOver.name + " [Tier " + game.towerMouseOver.tier + "]");
			
			double healthRegenRate = 1000 / GameRunner.STEP_DURATION * game.towerMouseOver.healthRegen.modifiedValue;
			towerStats.add("Health: " + (int)game.towerMouseOver.currHealth + 
				           " / " + (int)game.towerMouseOver.maxHealth.modifiedValue +
				           " (+" + String.format("%.2f", healthRegenRate) + " /sec)");
			
			double attackDamage = game.towerMouseOver.attackDamage.modifiedValue;
			towerStats.add("Attack Damage: " + String.format("%.1f", attackDamage));
			
			double attackRate = 1000 / GameRunner.STEP_DURATION / game.towerMouseOver.attackDelay.modifiedValue;
			towerStats.add("Attack Rate: " + String.format("%.2f", attackRate));
			
			towerStats.add("DPS: " + String.format("%.1f", attackDamage * attackRate));
			
			towerStats.add("Attack Range: " + String.format("%.1f", game.towerMouseOver.attackRange.modifiedValue));
			
			if(game.towerMouseOver.canAOE)
				towerStats.add("Attack AOE: " + String.format("%.1f", game.towerMouseOver.attackAOE.modifiedValue));
			
			drawStatsBox(g2d, xLineCenter1, yLineBot2, panelFont, panelFontColor, 1*subUnitSize, 2*subUnitSize, towerStats);
		}
		
		//if enemy wave incoming, fill enemy info panel
		if(!game.waveGenerator.getIncomingWaves().isEmpty()){
			List<String> enemyStats = new ArrayList<String>();
			
			EnemyWave incomingWave = game.waveGenerator.getIncomingWaves().getFirst();
			Enemy effectiveEnemy = game.waveGenerator.getEffectiveSpawnedEnemy(incomingWave);
			
			String waveInfo = "Wave #" + incomingWave.waveNumber + " : " + incomingWave.enemy.name;
			if(incomingWave.modifier != WaveModifier.NONE)
				waveInfo += " *" + incomingWave.modifier + "*";
			enemyStats.add(waveInfo);
			
			int waveDurationInSecs = game.waveGenerator.getEffectiveWaveDuration(incomingWave) /
					                 (1000 / GameRunner.STEP_DURATION);
			int waveSize = game.waveGenerator.getEffectiveWaveSize(incomingWave);
			enemyStats.add("Wave: " + waveSize + " foes over " + waveDurationInSecs + " secs");
			
			enemyStats.add("Health: " + String.format("%.0f", effectiveEnemy.maxHealth.modifiedValue));
			
			double attackDamage = effectiveEnemy.attackDamage.modifiedValue;
			enemyStats.add("Attack Damage: " + String.format("%.1f", attackDamage));
			
			double attackRate = 1000 / GameRunner.STEP_DURATION / effectiveEnemy.attackDelay.modifiedValue;
			enemyStats.add("Attack Rate: " + String.format("%.2f", attackRate));
			
			enemyStats.add("DPS: " + String.format("%.1f", attackDamage * attackRate));
			
			enemyStats.add("Attack Range: " + String.format("%.1f", effectiveEnemy.attackRange.modifiedValue));
			
			double movementRate = 1000 / GameRunner.STEP_DURATION * effectiveEnemy.moveSpeed.modifiedValue;
			enemyStats.add("Movement Speed: " + String.format("%.2f", movementRate) + " units/sec");
			
			drawStatsBox(g2d, xLineRight1, yLineBot2, panelFont, panelFontColor, 1*subUnitSize, 2*subUnitSize, enemyStats);
		}
		
		//draw pause state
		if(game.isPaused){
			g2d.setColor(Color.red);
			g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int) (unitSize*0.6)));
			g2d.drawString("***PAUSED***", (int) (xLineLeft1 + unitSize*0.5), (int) (yLineBot2-unitSize*0.3));
		}
	}
	
	private double xScreenLocToBoardLoc(int screenX){
		return ((double)(screenX - xBoardLeft) / tileSize) - 0.5;
	}
	
	private double yScreenLocToBoardLoc(int screenY){
		return ((double)(screenY - yLineTop) / tileSize) - 0.5;
	}
	
	private int xBoardLocToScreenLoc(double boardX){
		return (int) ((boardX * tileSize) + xBoardLeft + tileSize/2);
	}
	
	private int yBoardLocToScreenLoc(double boardY){
		return (int) ((boardY * tileSize) + yLineTop + tileSize/2);
	}
	
	private void drawStatsBox(Graphics2D g2d, int cornerX, int cornerY, Font font, Color fontColor, 
			                  int lineSpacing, int leftMargin, List<String> stats){
		int linePeriod = font.getSize() + lineSpacing;
		g2d.setColor(fontColor);
		g2d.setFont(font);
		int i=1;
		for(String s : stats){
			g2d.drawString(s, cornerX + leftMargin, cornerY + linePeriod*i);
			i++;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		double boardX = xScreenLocToBoardLoc(mouseX);
		double boardY = yScreenLocToBoardLoc(mouseY);
		if(boardX > 0 && boardX <= xNodes && boardY > 0 && boardY <= yNodes)
			game.boardClicked(new Point2d(boardX, boardY));
		
		game.boardMouseOver(new Point2d(boardX, boardY));
		
		repaint(); //paints the UI results of this, even if paused
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		double boardX = xScreenLocToBoardLoc(mouseX);
		double boardY = yScreenLocToBoardLoc(mouseY);
		
		game.boardMouseOver(new Point2d(boardX, boardY));
		
		repaint(); //paints the UI results of this, even if paused
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_R: game.redSelected(); break;
		case KeyEvent.VK_G: game.greenSelected(); break;
		case KeyEvent.VK_B: game.blueSelected(); break;
		case KeyEvent.VK_C: game.conduitSelected(); break;
		case KeyEvent.VK_T: game.teleportSelected(); break;
		case KeyEvent.VK_S: game.sellSelected(); break;
		case KeyEvent.VK_SHIFT: game.noneSelected(); break;
		
		case KeyEvent.VK_SPACE:
			if(game.isPaused)
				game.unpause();
			else
				game.pause();
			break;
			
		case KeyEvent.VK_BACK_SPACE: game.newGame(); break;
		case KeyEvent.VK_ESCAPE: game.quitToMenu();; break;
		}
		repaint(); //paint the UI results of this, even if paused
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}
