package frontEnd;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import backEnd.GameRunner;

public class GamePanel extends JPanel {

	private static final int EDGE_BUFFER_SIZE = 20;
	
	protected int panelWidth, panelHeight; //size of game panel in pixels
	protected int tileSize; //size of game tiles, in pixels
	protected int xNodes, yNodes; //size of game board, in tiles
	
	private GameRunner game; //reference to game runner (and thus the current game state to be displayed)
	
	public GamePanel(GameRunner game){
		this.game = game;
		
		//adds listeners to receive input
		addKeyListener(game);
		addMouseListener(game);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		updateDimensions();
		doPainting((Graphics2D)g);
	}
	
	private void updateDimensions(){
		panelWidth = getSize().width;
		panelHeight = getSize().height;
		xNodes = game.gameState.xNodes;
		yNodes = game.gameState.yNodes;
		tileSize = Math.min((panelWidth-EDGE_BUFFER_SIZE*2)/xNodes, (panelWidth-EDGE_BUFFER_SIZE*2)/yNodes);
	}
	
	private void doPainting(Graphics2D g2d){
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
		
		g2d.drawString("Frame #" + game.gameState.frameNumber, 20, panelHeight - 2);
		
		//frame game board
		g2d.setColor(Color.black);
		g2d.drawRect(EDGE_BUFFER_SIZE, EDGE_BUFFER_SIZE, tileSize*xNodes, tileSize*yNodes);
		
		//draw tile light levels
		//TODO: implement
		for(int i=1; i<xNodes+1; i++){
			for(int j=1; j<yNodes+1; j++){
				float lightLevel = (float) game.gameState.nodes[i][j].lightLevel();
				g2d.setColor(new Color(lightLevel/100, lightLevel/100, lightLevel/100));
				g2d.fillRect(EDGE_BUFFER_SIZE + (i-1)*tileSize, EDGE_BUFFER_SIZE + (j-1)*tileSize,
						     tileSize, tileSize);
			}
		}
		
		//dot individual nodes
		g2d.setColor(Color.black);
		int dotSize = 3;
		for(int i=1; i<xNodes+1; i++){
			for(int j=1; j<yNodes+1; j++){
				g2d.fillOval(EDGE_BUFFER_SIZE + i*tileSize - tileSize/2 - dotSize/2,
						     EDGE_BUFFER_SIZE + j*tileSize - tileSize/2 - dotSize/2,
						     dotSize, dotSize);
			}
		}
		
		//draw path lines
		//TODO: implement
		
		//draw entities
		//TODO: implement
	}
}
