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

import javax.swing.JPanel;

import util.Animation;
import util.Point2d;
import backEnd.Entity;
import backEnd.GameRunner;
import backEnd.LightSource;

public class GamePanel extends JPanel implements MouseListener, KeyListener {

	private static final int EDGE_BUFFER_SIZE = 30;
	
	protected int panelWidth, panelHeight; //size of game panel in pixels
	protected int tileSize; //size of game tiles, in pixels
	protected int topMargin, bottomMargin, leftMargin, rightMargin; //margin between edge of panel and edge of board
	
	protected int xNodes, yNodes; //size of game board, in tiles
	
	private GameRunner game; //reference to game runner (and thus the current game state to be displayed)
	
	private int mouseX, mouseY; //mouse's current location
	
	public GamePanel(GameRunner game){
		this.game = game;
		
		//adds listeners to receive input
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		
		mouseX = 0;
		mouseY = 0;
	}
	
	private void updateDimensions(){
		panelWidth = getSize().width;
		panelHeight = getSize().height;
		xNodes = game.gameState.xNodes;
		yNodes = game.gameState.yNodes;
		tileSize = Math.min((panelWidth-EDGE_BUFFER_SIZE*2)/xNodes, (panelWidth-EDGE_BUFFER_SIZE*2)/yNodes);
		
		topMargin = EDGE_BUFFER_SIZE;
		bottomMargin = panelHeight - yNodes*tileSize - EDGE_BUFFER_SIZE;
		leftMargin = EDGE_BUFFER_SIZE;
		rightMargin = panelWidth - xNodes*tileSize - EDGE_BUFFER_SIZE;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		updateDimensions();
		doPainting((Graphics2D)g);
	}
	
	private void doPainting(Graphics2D g2d){
		//draw light levels
		g2d.setColor(Color.darkGray);
		g2d.fillRect(leftMargin, topMargin, tileSize*xNodes, tileSize*yNodes);
		g2d.setColor(Color.lightGray);
		for(LightSource ls : game.gameState.lightSources){
			double radiance = ls.lightRadius();
			Point2d loc = ls.getLocation();
			int x = xBoardLocToScreenLoc(loc.x - radiance);
			int y = yBoardLocToScreenLoc(loc.y - radiance);
			int diameter = (int) (radiance * 2 * tileSize);
			g2d.fillOval(x, y, diameter, diameter);
		}
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, leftMargin, panelHeight);
		g2d.fillRect(0, 0, panelWidth, topMargin);
		g2d.fillRect(leftMargin + tileSize*xNodes, 0, panelWidth, panelHeight);
		g2d.fillRect(0, topMargin + tileSize*yNodes, panelWidth, panelHeight);
		
		//dot individual nodes
		g2d.setColor(Color.black);
		int dotSize = 3;
		for(int i=0; i<xNodes; i++){
			for(int j=0; j<yNodes; j++){
				g2d.fillOval(leftMargin + i*tileSize + tileSize/2 - dotSize/2,
						     topMargin + j*tileSize + tileSize/2 - dotSize/2,
						     dotSize, dotSize);
			}
		}
		
		//draw path lines
		//TODO: implement
		
		//draw entities
		for(Entity e : game.gameState.towers)
			e.paintEntity(g2d, leftMargin + tileSize/2, topMargin + tileSize/2, tileSize);
		
		for(Entity e : game.gameState.enemies)
			e.paintEntity(g2d, leftMargin + tileSize/2, topMargin + tileSize/2, tileSize);
		
		for(Entity e : game.gameState.projectiles)
			e.paintEntity(g2d, leftMargin + tileSize/2, topMargin + tileSize/2, tileSize);
		
		for(Entity e : game.gameState.miscEntities)
			e.paintEntity(g2d, leftMargin + tileSize/2, topMargin + tileSize/2, tileSize);
		
		//draw animations
		for(Animation a : game.gameState.animations)
			a.paintAnimation(g2d, leftMargin + tileSize/2, topMargin + tileSize/2, tileSize);
		
		//draw resource counts / frame number
		g2d.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
		
		g2d.setColor(Color.red);
		g2d.drawString("RED: " + ((int) game.redResources), 20, 18);
		
		g2d.setColor(Color.green);
		g2d.drawString("GREEN: " + ((int) game.greenResources), 220, 18);
		
		g2d.setColor(Color.blue);
		g2d.drawString("BLUE: " + ((int) game.blueResources), 420, 18);
		
		g2d.setColor(Color.gray);
		g2d.drawString("FLUX: " + ((int) game.fluxResources), 620, 18);
		
		//some debugging info here
		g2d.setColor(Color.black);
		g2d.drawString("Frame #" + game.gameState.frameNumber, 20, panelHeight - 2);
		g2d.drawString("Node last clicked on: (" + xScreenLocToBoardLoc(mouseX) + "," +
				        yScreenLocToBoardLoc(mouseY) + ")", 420, panelHeight - 2);
		
		//draw selected tools (and highlight selected)
		int yLine = panelHeight - bottomMargin + 20;
		int fontSize = 30;
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
		g2d.setStroke(new BasicStroke(4));
		
		g2d.setColor(Color.red);
		g2d.drawString("R", 40, yLine + fontSize + 5);
		if(game.actionSelected == GameRunner.ADD_RED_ACTION)
			g2d.drawRect(35, yLine, 40, fontSize + 10);
		
		g2d.setColor(Color.green);
		g2d.drawString("G", 100, yLine + fontSize + 5);
		if(game.actionSelected == GameRunner.ADD_GREEN_ACTION)
			g2d.drawRect(95, yLine, 40, fontSize + 10);
		
		g2d.setColor(Color.blue);
		g2d.drawString("B", 160, yLine + fontSize + 5);
		if(game.actionSelected == GameRunner.ADD_BLUE_ACTION)
			g2d.drawRect(155, yLine, 40, fontSize + 10);
		
		g2d.setColor(Color.darkGray);
		g2d.drawString("C", 220, yLine + fontSize + 5);
		if(game.actionSelected == GameRunner.ADD_CONDUIT_ACTION)
			g2d.drawRect(215, yLine, 40, fontSize + 10);

		g2d.setStroke(new BasicStroke(1));
	}
	
	private double xScreenLocToBoardLoc(int screenX){
		return ((screenX - leftMargin) / tileSize) + 1;
	}
	
	private double yScreenLocToBoardLoc(int screenY){
		return ((screenY - topMargin) / tileSize) + 1;
	}
	
	private int xBoardLocToScreenLoc(double boardX){
		return (int) ((boardX * tileSize) + leftMargin + tileSize/2);
	}
	
	private int yBoardLocToScreenLoc(double boardY){
		return (int) ((boardY * tileSize) + topMargin + tileSize/2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		int boardX = (int) xScreenLocToBoardLoc(mouseX);
		int boardY = (int) yScreenLocToBoardLoc(mouseY);
		if(boardX > 0 && boardX <= xNodes && boardY > 0 && boardY <= yNodes)
			game.nodeClicked(game.gameState.nodes[boardX][boardY]);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_R: game.redSelected(); break;
		case KeyEvent.VK_G: game.greenSelected(); break;
		case KeyEvent.VK_B: game.blueSelected(); break;
		case KeyEvent.VK_C: game.conduitSelected(); break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	
}
