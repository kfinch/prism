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
	
	private static final double MIN_LIGHT_LEVEL = 0;
	private static final double MAX_LIGHT_LEVEL = 100;
	private static final double MIN_LIGHT_CHANGE = 0.1;
	
	protected boolean isBuffer; //is this node a "buffer node"? (not in bounds)
	
	protected int xLoc, yLoc;
	
	protected Set<TerrainEffect> terrainEffects;
	
	//the light level of a node determines if a tower can be built / function on it.
	//expected light level is modified by proximity to the prism and/or conduit towers.
	//when expected light level changes, the actual light level takes some time to "catch up"
	protected Stat expectedLightLevel;
	protected double actualLightLevel;
	
	//measures how 'attractive' this node is to a moving enemy. 
	//should only do multiplicative buffs/debuffs to this
	protected Stat attractiveness;
	
	protected Set<Enemy> enemies; //enemies either on this node or traveling from it.
	protected Tower tower; //tower centered on this node (or null if there is none)
	
	public Node(int xLoc, int yLoc, double baseLightLevel){
		isBuffer = false;
		terrainEffects = new HashSet<TerrainEffect>();
		expectedLightLevel = new BasicStat(baseLightLevel);
		actualLightLevel = baseLightLevel;
		attractiveness = new BasicStat(1);
		enemies = new HashSet<Enemy>();
		tower = null;
	}
	
	public boolean hasSolidTower(){
		return tower != null && !tower.hasBuff(Tower.GHOST_DEBUFF_ID);
	}
	
	public void step(){
		updateLightLevel();
	}
	
	private void updateLightLevel(){
		double expected = expectedLightLevel.modifiedValue;
		if(expected > MAX_LIGHT_LEVEL)
			expected = MAX_LIGHT_LEVEL;
		else if(expected < MIN_LIGHT_LEVEL)
			expected = MIN_LIGHT_LEVEL;
		
		double diff = actualLightLevel - expectedLightLevel.modifiedValue;
		
		if(diff == 0)
			return;
		else if(Math.abs(diff) <= MIN_LIGHT_CHANGE){
			actualLightLevel = expectedLightLevel.modifiedValue;
		}
		else{
			double change = -diff/100;
			if(Math.abs(change) < MIN_LIGHT_CHANGE)
				change = MIN_LIGHT_CHANGE * Math.signum(change);
			actualLightLevel += change;
		}
	}
}
