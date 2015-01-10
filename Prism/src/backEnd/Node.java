package backEnd;

import java.util.HashSet;
import java.util.Set;

/**
 * A tile on the game board.
 * Tracks what kind of terrain effects it has, if it's occupied by enemies, and if it contains a tower.
 * 
 * @author Kelton Finch
 */
public class Node {
	
	public boolean isBuffer; //is this node a "buffer node"? (not in bounds)
	
	public int xLoc, yLoc;
	
	public Set<TerrainEffect> terrainEffects;
	
	//measures how 'attractive' this node is to a moving enemy. 
	//should only do multiplicative buffs/debuffs to this
	public Stat attractiveness;
	
	public Set<Enemy> enemies; //enemies either on this node or traveling from it.
	public Tower tower; //tower centered on this node (or null if there is none)
	
	public Node(int xLoc, int yLoc){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
		isBuffer = false;
		terrainEffects = new HashSet<TerrainEffect>();
		attractiveness = new BasicStat(1);
		enemies = new HashSet<Enemy>();
		tower = null;
	}
	
	public boolean hasLiveTower(){
		return tower != null && !tower.isGhost;
	}
}
