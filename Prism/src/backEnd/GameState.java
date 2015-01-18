package backEnd;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.Animation;
import util.GeometryUtils;
import util.Point2d;
import util.Vector2d;

/**
 * Fully encapsulates a prism game state, and provides methods for inspecting or modifying the state.
 * 
 * @author Kelton Finch
 */
public class GameState {
	
	public static final Color BACKGROUND_LIGHT = Color.decode("#aaaaaa");
	public static final Color BACKGROUND_DARK = Color.decode("#666666");
	
	public static final Color HEALTH_BAR_FULL = Color.decode("#006600");
	public static final Color HEALTH_BAR_EMPTY = Color.decode("#660000");
	
	public static final Color TOWER_BASE = Color.decode("#444444");
	public static final Color TOWER_RED = Color.decode("#aa0000");
	public static final Color TOWER_GREEN = Color.decode("#00aa00");
	public static final Color TOWER_BLUE = Color.decode("#0000aa");
	
	public static final Color UI_GOLD = Color.decode("#ffe80f");
	
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
	public Set<AttractSource> attractSources; //a set of all attract sources
	
	public Prism prism;
	public DarkPrism darkPrism;
	
	public List<Animation> animations; //a list of all active animations
	
	public double redResources;
	public double greenResources;
	public double blueResources;
	public double fluxResources;
	
	public double redResourcesGain;
	public double greenResourcesGain;
	public double blueResourcesGain;
	public double fluxResourcesGain;
	
	public double maximumFlux;
	
	public boolean playerWin;
	public boolean playerLose;
	
	public GameState(int xNodes, int yNodes){
		this(xNodes, yNodes, GameRunner.STARTING_COLOR, GameRunner.STARTING_FLUX,
			  GameRunner.COLOR_GAIN_RATE, GameRunner.FLUX_GAIN_RATE, GameRunner.MAXIMUM_FLUX);
	}
	
	public GameState(int xNodes, int yNodes,
			         double startingColorResources, double startingFluxResources,
			         double colorResourcesGain, double fluxResourcesGain, double maximumFlux){
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
		attractSources = new HashSet<AttractSource>();
		
		this.prism = new Prism(this, new Point2d(-2, yNodes/2), 22, 15); //TODO make some of these constants
		addMiscEntity(prism);
		
		this.darkPrism = new DarkPrism(this, new Point2d(xNodes+1, yNodes/2));
		addMiscEntity(darkPrism);
		
		animations = new LinkedList<Animation>();
		
		this.redResources = startingColorResources;
		this.greenResources = startingColorResources;
		this.blueResources = startingColorResources;
		this.fluxResources = startingFluxResources;
		
		this.redResourcesGain = colorResourcesGain;
		this.greenResourcesGain = colorResourcesGain;
		this.blueResourcesGain = colorResourcesGain;
		this.fluxResourcesGain = fluxResourcesGain;
		
		this.maximumFlux = maximumFlux;
		
		this.playerWin = false;
		this.playerLose = false;
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
		for(int xi = x-1; xi<=x+1; xi++){ //check for not over another tower, or on edge of screen
			for(int yi = y-1; yi<=y+1; yi++){
				n = nodeAt(xi,yi);
				if(n.isBuffer || n.tower != null)
					return false;
			}
		}
		
		for(Enemy e : enemies){ //check if enemies in the way
			if(GeometryUtils.distFromTowerEdge(e.loc.x, e.loc.y, x, y) <= 0) //TODO: make margin bigger?
				return false;
		}
		
		for(LightSource ls : lightSources){ //check for is lit
			if(GeometryUtils.dist(ls.getLocation().x, ls.getLocation().y, x, y) <= ls.lightRadius() - Math.sqrt(2))
				return true;
		}
		
		return false;
	}
	
	public void addTower(int x, int y, Tower tower){
		nodeAt(x,y).tower = tower;
		towers.add(tower);
		tower.onSpawn();
	}
	
	public void removeTower(Tower tower){
		tower.currNode.tower = null;
		towers.remove(tower);
		tower.onDespawn();
	}
	
	/*
	 * Moves the given tower to a new location. Will overwrite and set inactive a tower at the destination.
	 * Also does not check if the destination is valid.
	 */
	public void moveTower(int x, int y, Tower tower){
		Node dst = nodeAt(x,y);
		Node src = tower.currNode;
		//System.out.println("Teleporting from src @ " + src.xLoc + " " + src.yLoc + " " + src.tower +
		//		           " to dst @ " + dst.xLoc + " " + dst.yLoc + " " + dst.tower);
		                   
		
		//this special case so it won't deactivate itself if told to move to where it already is.
		if(src == dst)
			return;
		
		src.tower = null;
		dst.tower.isActive = false; //kills off any tower already at dst (check your destination!)
		tower.currNode = dst;
		tower.loc = new Point2d(x,y);
		dst.tower = tower;
		tower.onMove(src, dst);
		
		//System.out.println("After: src @ " + src.xLoc + " " + src.yLoc + " " + src.tower +
		//                   " to dst @ " + dst.xLoc + " " + dst.yLoc + " " + dst.tower);
	}
	
	public void addEnemy(int x, int y, Enemy enemy){
		nodeAt(x,y).enemies.add(enemy);
		enemies.add(enemy);
		enemy.onSpawn();
	}
	
	public void addMiscEntity(Entity entity){
		miscEntities.add(entity);
		entity.onSpawn();
	}
	
	public boolean isLit(Point2d loc){
		for(LightSource ls : lightSources){
			if(ls.getLocation().distanceTo(loc) <= ls.lightRadius())
				return true;
		}
		return false;
	}
	
	public Vector2d getAttractionAtPoint(Point2d point){
		Vector2d result = new Vector2d(0,0);
		for(AttractSource as : attractSources)
			result = result.afterAdd(as.getAttractionVectorFromPoint(point));
		return result;
	}
	
	public void step(){
		//pre step all entities
		for(Entity e : enemies)
			e.preStep();
		for(Entity e : towers)
			e.preStep();
		for(Entity e : projectiles)
			e.preStep();
		for(Entity e : miscEntities)
			e.preStep();
		
		//move step all entities
		for(Entity e : enemies)
			e.moveStep();
		for(Entity e : towers)
			e.moveStep();
		for(Entity e : projectiles)
			e.moveStep();
		for(Entity e : miscEntities)
			e.moveStep();
		
		//action step all entities
		for(Entity e : enemies)
			e.actionStep();
		for(Entity e : towers)
			e.actionStep();
		for(Entity e : projectiles)
			e.actionStep();
		for(Entity e : miscEntities)
			e.actionStep();
		
		//post step all entities
		for(Entity e : enemies)
			e.postStep();
		for(Entity e : towers)
			e.postStep();
		for(Entity e : projectiles)
			e.postStep();
		for(Entity e : miscEntities)
			e.postStep();
		
		//clean up inactive entities
		Iterator<Enemy> enIter = enemies.iterator();
		Enemy en;
		while(enIter.hasNext()){
			en = enIter.next();
			if(!en.isActive){
				en.onDespawn();
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
				t.onDespawn();
				Node n = nodeAt((int)t.loc.x,(int)t.loc.y);
				if(n.tower == t) //this check here so as not to accidentally kill a moving tower that overwrote another.
					nodeAt((int)t.loc.x,(int)t.loc.y).tower = null;
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
					if(!t.isLit)
						ressurect = false;
					if(ressurect)
						t.setGhost(false);
				}
			}
		}
		
		Iterator<Projectile> pIter = projectiles.iterator();
		Projectile p;
		while(pIter.hasNext()){
			p = pIter.next();
			if(!p.isActive){
				p.onDespawn();
				pIter.remove();
			}
		}
		
		Iterator<Entity> eIter = miscEntities.iterator();
		Entity e;
		while(eIter.hasNext()){
			e = eIter.next();
			if(!e.isActive){
				e.onDespawn();
				eIter.remove();
			}
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
		
		//player gains resources
		redResources += redResourcesGain;
		greenResources += greenResourcesGain;
		blueResources += blueResourcesGain;
		fluxResources += fluxResourcesGain;
		if(fluxResources > maximumFlux)
			fluxResources = maximumFlux;
		
		//check for game over
		if(prism.currHealth <= 0){ //player lose
			playerLose = true;
		}
		else if(darkPrism.currHealth <= 0){ //player win
			playerWin = true;
		}
		
		frameNumber++;
	}
	
	public Set<Node> getNodesInRange(Point2d loc, double range){
		Set<Node> result = new HashSet<Node>();
		
		int xMin = (int) (loc.x - range);
		if(xMin <= 0)
			xMin = 1;
		
		int xMax = (int) (loc.x + range);
		if(xMax > xNodes)
			xMax = xNodes;
		
		int yMin = (int) (loc.y - range);
		if(yMin <= 0)
			yMin = 1;
		
		int yMax = (int) (loc.y + range);
		if(yMax > yNodes)
			yMax = yNodes;
		
		for(int x=xMin; x<=xMax; x++){
			for(int y=yMin; y<=yMax; y++){
				if(GeometryUtils.dist(x,y,loc.x,loc.y) <= range){
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
	public Set<Enemy> getEnemiesInRange(Point2d loc, double range){
		Set<Enemy> result = new HashSet<Enemy>();
		for(Enemy en : enemies){
			if(en.loc.distanceTo(loc) <= range)
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
	public Set<Projectile> getProjectilesInRange(Point2d loc, double range){
		Set<Projectile> result = new HashSet<Projectile>();
		for(Projectile p : projectiles){
			if(p.loc.distanceTo(loc) <= range)
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
	public Set<Tower> getTowersInEdgeRange(Point2d loc, double range){
		Set<Tower> result = new HashSet<Tower>();
		for(Tower t : towers){
			if(!t.isGhost && GeometryUtils.distFromTowerEdge(t.loc.x, t.loc.y, loc.x, loc.y) <= range)
				result.add(t);
		}
		return result;
	}
	
	public Set<Tower> getTowersInCenterRange(Point2d loc, double range){
		Set<Tower> result = new HashSet<Tower>();
		for(Tower t : towers){
			if(!t.isGhost && t.loc.distanceTo(loc) <= range)
				result.add(t);
		}
		return result;
	}
	
	public boolean isPrismInRange(Point2d loc, double range){
		return (range >= loc.x);
	}
	
	public Set<Entity> getTowersAndPrismInRange(Point2d loc, double range){
		Set<Entity> result = new HashSet<Entity>();
		for(Tower t : towers){
			if(!t.isGhost && GeometryUtils.distFromTowerEdge(t.loc, loc) <= range)
				result.add(t);
		}
		if(isPrismInRange(loc, range))
			result.add(prism);
		
		return result;
	}
	
	public void spawnEnemy(Enemy waveEnemies){
		int spawnX = xNodes-1;
		int spawnY = (int) (Math.random()*yNodes);
		addEnemy(spawnX, spawnY, waveEnemies.generateCopy(new Point2d(spawnX, spawnY), 
				                                          nodeAt(spawnX, spawnY), frameNumber));
	}
}
