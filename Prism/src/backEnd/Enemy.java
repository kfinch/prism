package backEnd;

import util.Animation;
import util.CombatTextAnimation;
import util.PaintableShapes;
import util.Point2d;

/**
 * Abstract class representing an enemy unit in Prism.
 * Contains all the basic stats that all enemies share.
 * The abstract method step() determines how the enemy will behave; it will be called on each simulation step.
 * 
 * @author Kelton Finch
 */
public abstract class Enemy extends EntityWithAttack {
	
	protected static final double HEALTH_BAR_OFFSET = -0.3;
	protected static final double HEALTH_BAR_WIDTH = 0.6;
	protected static final double HEALTH_BAR_HEIGHT = 0.2;
	
	protected static final double TIER_STAT_MULTIPLIER = 0.15;
	
	public String id, name, description;
	
	public double tier; //enemy's tier. Unlike for towers, this isn't fixed, enemy's stats should scale with their tier.
	
	public Node currNode, nextNode; //node this enemy is on, and node it's headed to.
	public boolean toNextNode; //true if this enemy has reached it's next node.
	
	public double priority; //attack priority of this enemy
	public int spawnFrame; //frame this enemy spawned on
	
	public Stat moveSpeed; //enemy's move stat
	
	public Enemy(String id, String name, String description,
				 GameState gameState, Point2d loc, double tier, Node currNode, double priority, int spawnFrame,
			     double maxHealth, double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     double moveSpeed, PaintableShapes shapes){
		super(gameState, loc, maxHealth * Math.pow(1+TIER_STAT_MULTIPLIER, tier), healthRegen,
				attackDamage * Math.pow(1+TIER_STAT_MULTIPLIER, tier), attackDelay, attackRange, shapes);
		
		this.id = id;
		this.name = name;
		this.description = description;
		
		this.tier = tier;
		
		this.currNode = currNode;
		this.nextNode = null;
		this.toNextNode = false;
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		
		this.moveSpeed = new BasicStat(moveSpeed);
		
		this.showHealthBar = true;
		this.healthBarOffset = HEALTH_BAR_OFFSET;
		this.healthBarWidth = HEALTH_BAR_WIDTH;
		this.healthBarHeight = HEALTH_BAR_HEIGHT;
	}
	
	public abstract int getBaseWaveSize();
	
	public abstract double getKillReward();
	
	public abstract Enemy generateCopy(double tier);
	
	@Override
	public void die(){
		super.die();
		gameState.redResources += getKillReward();
		gameState.greenResources += getKillReward();
		gameState.blueResources += getKillReward();
		
		Animation killRewardCombatText = new CombatTextAnimation("+" + (int)(getKillReward()), 0.5,
				                                                 GameState.UI_GOLD, 0.5, 30);
		killRewardCombatText.loc = loc;
		gameState.playAnimation(killRewardCombatText);
	}
	
}
