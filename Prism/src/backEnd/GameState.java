package backEnd;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
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
	
	public static final Color HEALTH_BAR_FULL = Color.decode("#006600");
	public static final Color HEALTH_BAR_EMPTY = Color.decode("#660000");
	
	public static final Color TOWER_BASE = Color.decode("#444444");
	public static final Color TOWER_RED = Color.decode("#aa0000");
	public static final Color TOWER_GREEN = Color.decode("#00aa00");
	public static final Color TOWER_BLUE = Color.decode("#0000aa");
	
	public static final Color ENEMY_DRAB_GREEN = Color.decode("#556b2f");
	public static final Color ENEMY_ORANGE = Color.decode("#ff8c00");
	public static final Color ENEMY_PURPLE = Color.decode("#ff00bb");
	
	public static final Color PROJECTILE_RED = Color.decode("#cc0000");
	public static final Color PROJECTILE_GREEN = Color.decode("#00cc00");
	public static final Color PROJECTILE_BLUE = Color.decode("#0000cc");
	public static final Color PROJECTILE_REDGREEN = Color.decode("#b8860b");
	public static final Color PROJECTILE_GREENBLUE = Color.decode("#44cdaa");
	public static final Color PROJECTILE_REDBLUE = Color.decode("#a020f0");
	
	public int frameNumber;
	
	public int xNodes, yNodes; //x and y size of board, in nodes (NOT including buffer nodes)
	public Node[][] nodes; //array of game's nodes.
	
	//the prism is always located at x=-1, all y values.
	//effectively anything that moves to the low x buffer has hit the prism
	protected int prismCurrHealth, prismMaxHealth; //current and maximum health of the prism
	
	public Set<Enemy> enemies; //a set of all active enemies
	public Set<Tower> towers; //a set of all active towers
	public Set<Projectile> projectiles; //a set of all active projectiles
	public Set<Entity> miscEntities; //a set of all active misc entities
	public Set<LightSource> lightSources; //a set of all light sources
	
	public List<Animation> animations; //a list of all active animations
	
	public GameState(int xNodes, int yNodes){
		this.frameNumber = 0;
		
		this.xNodes = xNodes;
		this.yNodes = yNodes;
		nodes = new Node[xNodes+2][yNodes+2];//+2 is buffer nodes
		for(int i=0; i<xNodes+2; i++){
			for(int j=0; j<yNodes+2; j++){
				nodes[i][j] = new Node(i-1, j-1);
				if(i == 0 || i == xNodes+1 || j == 0 || j == yNodes+1)
					nodes[i][j].isBuffer = true;
			}
		}
		
		enemies = new HashSet<Enemy>();
		towers = new HashSet<Tower>();
		projectiles = new HashSet<Projectile>();
		miscEntities = new HashSet<Entity>();
		lightSources = new HashSet<LightSource>();
		
		//TODO: this should probably be it's own special entity
		Prism prism = new Prism(-15, yNodes/2, 22);
		miscEntities.add(prism);
		lightSources.add(prism);
		
		animations = new LinkedList<Animation>();
	}
	
	public Node nodeAt(int x, int y){
		return nodes[x+1][y+1];
	}
	
	public void playAnimation(Animation anim){
		animations.add(anim);
	}
	
	public boolean[][] getValidMoveDirections(int x, int y){
		x++; y++; //should have used nodeAt for everything here, but too lazy to change it t_t. This fix suffices.
		boolean[][] result = new boolean[3][3];
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				result[i][j] = true;
			}
		}
		
		//check north
		if(nodes[x][y-1].hasLiveTower()){
			result[0][0] = false;
			result[1][0] = false;
			result[2][0] = false;
		}
		else if(nodes[x][y-1].isBuffer)
			result[1][0] = false;
		else if((nodes[x+1][y-1].hasLiveTower() || nodes[x+1][y].hasLiveTower()) &&
				(nodes[x-1][y-1].hasLiveTower() || nodes[x-1][y].hasLiveTower()))
			result[1][0] = false;
		
		//check east
		if(nodes[x+1][y].hasLiveTower()){
			result[2][0] = false;
			result[2][1] = false;
			result[2][2] = false;
		}
		else if(nodes[x+1][y].isBuffer)
			result[2][1] = false;
		else if((nodes[x][y-1].hasLiveTower() || nodes[x+1][y-1].hasLiveTower()) &&
				(nodes[x][y+1].hasLiveTower() || nodes[x+1][y+1].hasLiveTower()))
			result[2][1] = false;
		
		//check south
		if(nodes[x][y+1].hasLiveTower()){
			result[0][2] = false;
			result[1][2] = false;
			result[2][2] = false;
		}
		else if(nodes[x][y+1].isBuffer)
			result[1][2] = false;
		else if((nodes[x+1][y+1].hasLiveTower() || nodes[x+1][y].hasLiveTower()) &&
				(nodes[x-1][y+1].hasLiveTower() || nodes[x-1][y].hasLiveTower()))
			result[1][2] = false;
		
		//check west
		if(nodes[x-1][y].hasLiveTower()){
			result[0][0] = false;
			result[0][1] = false;
			result[0][2] = false;
		}
		else if(nodes[x-1][y].isBuffer)
			result[0][1] = false;
		else if((nodes[x][y-1].hasLiveTower() || nodes[x-1][y-1].hasLiveTower()) &&
				(nodes[x][y+1].hasLiveTower() || nodes[x-1][y+1].hasLiveTower()))
			result[0][1] = false;
		
		//check north-east
		if(nodes[x+1][y-1].isBuffer || nodes[x+1][y-1].hasLiveTower())
			result[2][0] = false;
		
		//check south-east
		if(nodes[x+1][y+1].isBuffer || nodes[x+1][y+1].hasLiveTower())
			result[2][2] = false;
				
		//check south-west
		if(nodes[x-1][y+1].isBuffer || nodes[x-1][y+1].hasLiveTower())
			result[0][2] = false;
				
		//check north-west
		if(nodes[x-1][y-1].isBuffer || nodes[x-1][y-1].hasLiveTower())
			result[0][0] = false;
		
		//TODO: remove these debugging prints
		/*
		System.out.println(x + " " + y);
		for(int j=0; j<3; j++){
			for(int i=0; i<3; i++){
				System.out.print(result[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		*/
		
		return result;
	}
	
	public boolean isValidTowerLocation(int x, int y){
		Node n;
		for(int xi = x-1; xi<=x+1; xi++){
			for(int yi = y-1; yi<=y+1; yi++){
				//TODO: allow to build over ghost towers? (currently does not allow)
				n = nodeAt(xi,yi);
				if(n.isBuffer || n.tower != null || !n.enemies.isEmpty())
					return false;
			}
		}
		for(LightSource ls : lightSources){
			if(dist(ls.getLocation().x, ls.getLocation().y, x, y) <= ls.lightRadius() - Math.sqrt(2))
				return true;
		}
		return false;
	}
	
	public void addTower(int x, int y, Tower tower){
		nodeAt(x,y).tower = tower;
		towers.add(tower);
		tower.onSpawn(this);
	}
	
	public void addEnemy(int x, int y, Enemy enemy){
		nodeAt(x,y).enemies.add(enemy);
		enemies.add(enemy);
		enemy.onSpawn(this);
	}
	
	public boolean isLit(double x, double y){
		for(LightSource ls : lightSources){
			if(dist(ls.getLocation().x, ls.getLocation().y, x, y) <= ls.lightRadius())
				return true;
		}
		return false;
	}
	
	public void step(){
		frameNumber++;
		
		//TODO: put enemy spawning somewhere that makes sense?
		if(frameNumber % 40 == 0){
			int spawnX = xNodes-1;
			int spawnY = (int) (Math.random()*yNodes);
			addEnemy(spawnX, spawnY, new EnemyTrash(nodeAt(spawnX, spawnY), spawnX, spawnY, frameNumber));
		}
		
		//prestep all entities
		for(Entity e : enemies)
			e.preStep(this);
		for(Entity e : towers)
			e.preStep(this);
		for(Entity e : projectiles)
			e.preStep(this);
		for(Entity e : miscEntities)
			e.preStep(this);
		
		//movestep all entities
		for(Entity e : enemies)
			e.moveStep(this);
		for(Entity e : towers)
			e.moveStep(this);
		for(Entity e : projectiles)
			e.moveStep(this);
		for(Entity e : miscEntities)
			e.moveStep(this);
		
		//actionstep all entities
		for(Entity e : enemies)
			e.actionStep(this);
		for(Entity e : towers)
			e.actionStep(this);
		for(Entity e : projectiles)
			e.actionStep(this);
		for(Entity e : miscEntities)
			e.actionStep(this);
		
		//poststep all entities
		for(Entity e : enemies)
			e.postStep(this);
		for(Entity e : towers)
			e.postStep(this);
		for(Entity e : projectiles)
			e.postStep(this);
		for(Entity e : miscEntities)
			e.postStep(this);
		
		//clean up inactive entities
		Iterator<Enemy> enIter = enemies.iterator();
		Enemy en;
		while(enIter.hasNext()){
			en = enIter.next();
			if(!en.isActive){
				en.onDespawn(this);
				boolean removed = en.currNode.enemies.remove(en);
				if(!removed)
					System.out.println("failed to fully remove enemy!");
				enIter.remove();
			}
		}
		
		Iterator<Tower> tIter = towers.iterator();
		Tower t;
		while(tIter.hasNext()){
			t = tIter.next();
			if(!t.isActive){
				t.onDespawn(this);
				nodeAt((int)t.xLoc,(int)t.yLoc).tower = null;
				tIter.remove();
			}
			else if(t.isGhost){
				t.ghostTimer--;
				if(t.ghostTimer <= 0){
					boolean ressurect = true;
					for(int xi=-1; xi<2; xi++){
						for(int yi=-1; yi<2; yi++){
							if(!nodeAt(t.currNode.xLoc+xi, t.currNode.yLoc+yi).enemies.isEmpty())
								ressurect = false;
						}
					}
					if(ressurect)
						t.setGhost(false);
				}
			}
		}
		
		Iterator<Projectile> pIter = projectiles.iterator();
		Projectile p;
		while(pIter.hasNext()){
			p = pIter.next();
			if(!p.isActive)
				pIter.remove();
		}
		
		Iterator<Entity> eIter = miscEntities.iterator();
		Entity e;
		while(eIter.hasNext()){
			e = eIter.next();
			if(!e.isActive)
				eIter.remove();
		}
		
		//step all animations, remove those that are no longer active
		Iterator<Animation> aIter = animations.iterator();
		Animation a;
		while(aIter.hasNext()){
			a = aIter.next();
			if(!a.isActive)
				aIter.remove();
			else
				a.step();
		}
		
	}
	
	public static double dist(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	public static double distFromTowerEdge(double x1, double y1, double x2, double y2){
		double xDiff = Math.abs(x1 - x2);
		double yDiff = Math.abs(y1 - y2);
		xDiff = (xDiff <= 1) ? 0 : xDiff - 1;
		yDiff = (yDiff <= 1) ? 0 : yDiff - 1;
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}
	
	public Set<Node> getNodesInRange(double xLoc, double yLoc, double range){
		Set<Node> result = new HashSet<Node>();
		
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
				if(dist(x,y,xLoc,yLoc) <= range){
					result.add(nodes[x][y]);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Generates a list of all enemies within a given range of a given point.
	 * Distance to enemies is measured from that enemy's center.
	 * @param xLoc x coordinate of the point to find nearby enemies to.
	 * @param yLoc y coordinate of the point to find nearby enemies to.
	 * @param range range around the point to search
	 * @return a list of all enemies within range of (xLoc,yLoc)
	 */
	//TODO: failing to detect things it should sometimes, investigate
	/*public Set<Enemy> getEnemiesInRange(double xLoc, double yLoc, double range){
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
					for(Enemy e : nodeAt(x,y).enemies){
						if(dist(e.xLoc, e.yLoc, xLoc, yLoc) <= range)
							result.add(e);
					}
				}
			}
		}
		return result;
	}*/
	
	//much simpler (but also slower) version of getEnemiesInRange()
	public Set<Enemy> getEnemiesInRange(double xLoc, double yLoc, double range){
		Set<Enemy> result = new HashSet<Enemy>();
		for(Enemy en : enemies){
			if(dist(en.xLoc, en.yLoc, xLoc, yLoc) <= range)
				result.add(en);
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
	/*
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
	}*/
	
	//much simpler (but also slower) version of getTowersInRange()
	//this version also DOES NOT see ghost towers
	public Set<Tower> getTowersInRange(double xLoc, double yLoc, double range){
		Set<Tower> result = new HashSet<Tower>();
		for(Tower t : towers){
			if(!t.isGhost && distFromTowerEdge(t.xLoc, t.yLoc, xLoc, yLoc) <= range)
				result.add(t);
		}
		return result;
	}
}
