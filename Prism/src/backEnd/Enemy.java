package backEnd;

import util.PaintableShapes;

/**
 * Abstract class representing an enemy unit in Prism.
 * Contains all the basic stats that all enemies share.
 * The abstract method step() determines how the enemy will behave; it will be called on each simulation step.
 * 
 * @author Kelton Finch
 */
public abstract class Enemy extends Entity {
	
	protected static final double HEALTH_BAR_OFFSET = -0.3;
	protected static final double HEALTH_BAR_WIDTH = 0.6;
	protected static final double HEALTH_BAR_HEIGHT = 0.2;
	
	protected static final double TIER_STAT_MULTIPLIER = 0.1;
	
	protected int tier; //enemy's tier. Unlike for towers, this isn't fixed, enemy's stats should scale with their tier.
	
	protected Node currNode, nextNode; //node this enemy is on, and node it's headed to.
	protected boolean toNextNode; //true if this enemy has reached it's next node.
	
	protected double priority; //attack priority of this enemy
	protected int spawnFrame; //frame this enemy spawned on
	
	protected Stat attackDamage, attackDelay, attackRange; //enemy's attack stats
	protected int attackTimer; //tracks when enemy can next attack
	protected Stat moveSpeed; //enemy's move stat
	
	public Enemy(int tier, Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, double maxHealth,
			     double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     double moveSpeed, PaintableShapes shapes){
		super(xLoc, yLoc, maxHealth * (1 + tier*TIER_STAT_MULTIPLIER), healthRegen, shapes);
		
		this.tier = tier;
		
		this.currNode = currNode;
		this.nextNode = null;
		this.toNextNode = false;
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		
		this.attackDamage = new BasicStat(attackDamage * (1 + tier*TIER_STAT_MULTIPLIER));
		this.attackDelay = new ReverseMultStat(attackDelay);
		this.attackRange = new BasicStat(attackRange);
		this.attackTimer = -1;
		
		this.moveSpeed = new BasicStat(moveSpeed);
		
		this.showHealthBar = true;
		this.healthBarOffset = HEALTH_BAR_OFFSET;
		this.healthBarWidth = HEALTH_BAR_WIDTH;
		this.healthBarHeight = HEALTH_BAR_HEIGHT;
	}
	
	public abstract int getWaveSize();
	
	public abstract double getKillReward();
	
	public abstract Enemy generateCopy(Node currNode, double xLoc, double yLoc, int spawnFrame);
	
	@Override
	public void die(GameState gameState){
		super.die(gameState);
		gameState.redResources += getKillReward();
		gameState.greenResources += getKillReward();
		gameState.blueResources += getKillReward();
	}
	
	@Override
	public void preStep(GameState gameState){
		super.preStep(gameState);
		if(passiveAction.canAct()){
			if(attackTimer >= 0)
				attackTimer++;
			if(attackTimer >= attackDelay.modifiedValue)
				attackTimer = -1;
		}
	}
}
