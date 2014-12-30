package backEnd;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.Animation;

/**
 * Fully encapsulates a prism game state, and provides methods for inspecting or modifying the state.
 * 
 * @author Kelton Finch
 */
public class GameState {
	
	public static final Color TOWER_BASE = Color.decode("#444444");
	public static final Color TOWER_RED = Color.decode("#aa0000");
	public static final Color TOWER_GREEN = Color.decode("#00aa00");
	public static final Color TOWER_BLUE = Color.decode("#0000aa");
	public static final Color PROJECTILE_RED = Color.decode("#cc0000");
	public static final Color PROJECTILE_GREEN = Color.decode("#00cc00");
	public static final Color PROJECTILE_BLUE = Color.decode("#0000cc");
	
	public int frameNumber;
	
	public int xNodes, yNodes; //x and y size of board, in nodes (NOT including buffer nodes)
	public Node[][] nodes; //array of game's nodes.
	
	//the prism is always located at x=-1, all y values.
	//effectively anything that moves to the low x buffer has hit the prism
	protected int prismCurrHealth, prismMaxHealth; //current and maximum health of the prism
	
	public Set<Enemy> enemies; //a list of all active enemies
	public Set<Tower> towers; //a list of all active towers
	public Set<Projectile> projectiles; //a list of all active projectiles
	public Set<Entity> miscEntities; //a list of all active misc entities
	
	public List<Animation> animations; //a list of all active animations
	
	public GameState(int xNodes, int yNodes){
		this.frameNumber = 0;
		
		this.xNodes = xNodes;
		this.yNodes = yNodes;
		nodes = new Node[xNodes+2][yNodes+2];//+2 is buffer nodes
		for(int i=0; i<xNodes+2; i++){
			for(int j=0; j<yNodes+2; j++){
				nodes[i][j] = new Node(i-1, j-1, i+j); //TODO: make prism give off light
			}
		}
		
		enemies = new HashSet<Enemy>();
		towers = new HashSet<Tower>();
		projectiles = new HashSet<Projectile>();
		miscEntities = new HashSet<Entity>();
		
		animations = new LinkedList<Animation>();
	}
	
	public void playAnimation(Animation anim){
		animations.add(anim);
	}
	
	public boolean[][] getValidMoveDirections(int x, int y){
		boolean[][] result = new boolean[3][3];
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				result[i][j] = true;
			}
		}
		
		//check north
		if(nodes[x][y-1].hasSolidTower()){
			result[x-1][y-1] = false;
			result[x][y-1] = false;
			result[x+1][y-1] = false;
		}
		else if(nodes[x][y-1].isBuffer)
			result[x][y-1] = false;
		else if((nodes[x+1][y-1].hasSolidTower() || nodes[x+1][y].hasSolidTower()) &&
				(nodes[x-1][y-1].hasSolidTower() || nodes[x-1][y].hasSolidTower()))
			result[x][y-1] = false;
		
		//check east
		if(nodes[x+1][y].hasSolidTower()){
			result[x+1][y-1] = false;
			result[x+1][y] = false;
			result[x+1][y+1] = false;
		}
		else if(nodes[x+1][y].isBuffer)
			result[x+1][y] = false;
		else if((nodes[x][y-1].hasSolidTower() || nodes[x+1][y-1].hasSolidTower()) &&
				(nodes[x][y+1].hasSolidTower() || nodes[x+1][y+1].hasSolidTower()))
			result[x+1][y] = false;
		
		//check south
		if(nodes[x][y+1].hasSolidTower()){
			result[x-1][y+1] = false;
			result[x][y+1] = false;
			result[x+1][y+1] = false;
		}
		else if(nodes[x][y+1].isBuffer)
			result[x][y+1] = false;
		else if((nodes[x+1][y+1].hasSolidTower() || nodes[x+1][y].hasSolidTower()) &&
				(nodes[x-1][y+1].hasSolidTower() || nodes[x-1][y].hasSolidTower()))
			result[x][y+1] = false;
		
		//check east
		if(nodes[x-1][y].hasSolidTower()){
			result[x-1][y-1] = false;
			result[x-1][y] = false;
			result[x-1][y+1] = false;
		}
		else if(nodes[x-1][y].isBuffer)
			result[x-1][y] = false;
		else if((nodes[x][y-1].hasSolidTower() || nodes[x-1][y-1].hasSolidTower()) &&
				(nodes[x][y+1].hasSolidTower() || nodes[x-1][y+1].hasSolidTower()))
			result[x-1][y] = false;
		
		//check north-east
		if(nodes[x+1][y-1].isBuffer || nodes[x+1][y-1].hasSolidTower())
			result[x+1][y-1] = false;
		
		//check south-east
		if(nodes[x+1][y+1].isBuffer || nodes[x+1][y-1].hasSolidTower())
			result[x+1][y+1] = false;
				
		//check south-west
		if(nodes[x-1][y+1].isBuffer || nodes[x+1][y-1].hasSolidTower())
			result[x-1][y+1] = false;
				
		//check north-west
		if(nodes[x-1][y-1].isBuffer || nodes[x+1][y-1].hasSolidTower())
			result[x-1][y-1] = false;
		
		return result;
	}
	
	public boolean isValidTowerLocation(int x, int y){
		for(int xi = x-1; xi<x+1; x++){
			for(int yi = y-1; yi<y+1; y++){
				//TODO: allow to build over ghost towers? (currently does not allow)
				if(nodes[xi][yi].isBuffer || nodes[xi][yi].tower != null || !nodes[xi][yi].enemies.isEmpty())
					return false;
			}
		}
		return true;
	}
	
	public void addTower(int x, int y, Tower tower){
		nodes[x][y].tower = tower;
	}
	
	public void removeTower(int x, int y){
		nodes[x][y].tower = null;
	}
	
	public void step(){
		frameNumber++;
		
		for(Node[] na : nodes){
			for(Node n : na){
				n.step();
			}
		}
		
		for(Entity e : enemies)
			e.preStep(this);
		for(Entity e : towers)
			e.preStep(this);
		for(Entity e : projectiles)
			e.preStep(this);
		for(Entity e : miscEntities)
			e.preStep(this);
		
		for(Entity e : enemies)
			e.moveStep(this);
		for(Entity e : towers)
			e.moveStep(this);
		for(Entity e : projectiles)
			e.moveStep(this);
		for(Entity e : miscEntities)
			e.moveStep(this);
		
		for(Entity e : enemies)
			e.actionStep(this);
		for(Entity e : towers)
			e.actionStep(this);
		for(Entity e : projectiles)
			e.actionStep(this);
		for(Entity e : miscEntities)
			e.actionStep(this);
		
		for(Entity e : enemies)
			e.postStep(this);
		for(Entity e : towers)
			e.postStep(this);
		for(Entity e : projectiles)
			e.postStep(this);
		for(Entity e : miscEntities)
			e.postStep(this);
		
		for(Animation a : animations)
			a.step();
	}
	
	public static double dist(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	/**
	 * Generates a list of all enemies within a given range of a given point.
	 * Distance to enemies is measured from that enemy's center.
	 * @param xLoc x coordinate of the point to find nearby enemies to.
	 * @param yLoc y coordinate of the point to find nearby enemies to.
	 * @param range range around the point to search
	 * @return a list of all enemies within range of (xLoc,yLoc)
	 */
	public Set<Enemy> getEnemiesInRange(double xLoc, double yLoc, double range){
		Set<Enemy> result = new HashSet<Enemy>();
		int xMin = (int) (xLoc - range - 1);
		if(xMin <= 0)
			xMin = 1;
		
		int xMax = (int) (xLoc + range + 1);
		if(xMax > xNodes)
			xMax = xNodes;
		
		int yMin = (int) (yLoc - range - 1);
		if(yMin <= 0)
			yMin = 1;
		
		int yMax = (int) (yLoc + range + 1);
		if(yMax > yNodes)
			yMax = yNodes;
		
		for(int x=xMin; x<=xMax; x++){
			for(int y=yMin; y<=yMax; y++){
				if(dist(x,y,xLoc,yLoc) <= range+1.5){
					for(Enemy e : nodes[x][y].enemies){
						if(dist(e.xLoc, e.yLoc, xLoc, yLoc) <= range)
							result.add(e);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Generates a list of all projectiles within a given range of a given point.
	 * Distance to enemies is measured from that projectile's center.
	 * Note that this method cannot be optimized like getTowersInRange() and getEnemiesInRange()
	 * because Projectiles aren't tied to a specific node or nodes.
	 * As such, this method should be used sparingly.
	 * @param xLoc x coordinate of the point to find nearby projectiles to.
	 * @param yLoc y coordinate of the point to find nearby projectiles to.
	 * @param range range around the point to search
	 * @return a list of all projectiles within range of (xLoc,yLoc)
	 */
	public Set<Projectile> getProjectilesInRange(double xLoc, double yLoc, double range){
		Set<Projectile> result = new HashSet<Projectile>();
		
		for(Projectile p : projectiles){
			if(dist(p.xLoc, p.yLoc, xLoc, yLoc) <= range)
				result.add(p);
		}
		
		return result;
	}
	
	/**
	 * Generates a list of all towers within a given range of a given point.
	 * Distance to towers is measure from that tower's *edges*.
	 * @param xLoc x coordinate of the point to find nearby towers to.
	 * @param yLoc y coordinate of the point to find nearby towers to.
	 * @param range range around the point to search
	 * @return a list of all towers within range of (xLoc,yLoc)
	 */
	public Set<Tower> getTowersInRange(double xLoc, double yLoc, double range){
		Set<Tower> result = new HashSet<Tower>();
		int xMin = (int) (xLoc - range);
		if(xMin <= 0)
			xMin = 1;
		
		int xMax = (int) (xLoc + range);
		if(xMax > xNodes)
			xMax = xNodes;
		
		int yMin = (int) (yLoc - range);
		if(yMin <= 0)
			yMin = 1;
		
		int yMax = (int) (yLoc + range);
		if(yMax > yNodes)
			yMax = yNodes;
		
		for(int x=xMin; x<=xMax; x++){
			for(int y=yMin; y<=yMax; y++){
				//TODO: rice this part a little more t_t. Can do better on efficiency.
				if(dist(x-1, y-1, xLoc, yLoc) <= range && nodes[x-1][y-1].tower != null)
					result.add(nodes[x-1][y-1].tower);
				if(dist(x+1, y-1, xLoc, yLoc) <= range && nodes[x+1][y-1].tower != null)
					result.add(nodes[x+1][y-1].tower);
				if(dist(x-1, y+1, xLoc, yLoc) <= range && nodes[x-1][y+1].tower != null)
					result.add(nodes[x-1][y+1].tower);
				if(dist(x+1, y+1, xLoc, yLoc) <= range && nodes[x+1][y+1].tower != null)
					result.add(nodes[x+1][y+1].tower);
			}
		}
		return result;
	}
}
