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
	
	protected boolean isBuffer;
	protected boolean adjacentTower;
	protected Set<TerrainEffect> terrainEffects;
	protected Set<Enemy> enemies;
	protected Tower tower;
	
	public Node(){
		isBuffer = false;
		adjacentTower = false;
		terrainEffects = new HashSet<TerrainEffect>();
		enemies = new HashSet<Enemy>();
		tower = null;
	}
}
