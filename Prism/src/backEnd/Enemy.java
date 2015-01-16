package backEnd;

import util.Animation;
import util.CombatTextAnimation;
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
	
	protected static final double TIER_STAT_MULTIPLIER = 0.15;
	
	public int tier; //enemy's tier. Unlike for towers, this isn't fixed, enemy's stats should scale with their tier.
	
	public Node currNode, nextNode; //node this enemy is on, and node it's headed to.
	public boolean toNextNode; //true if this enemy has reached it's next node.
	
	public double priority; //attack priority of this enemy
	public int spawnFrame; //frame this enemy spawned on
	
	public Stat attackDamage, attackDelay, attackRange; //enemy's attack stats
	public int attackTimer; //tracks when enemy can next attack
	public Stat moveSpeed; //enemy's move stat
	
	public Enemy(int tier, Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, double maxHealth,
			     double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     double moveSpeed, PaintableShapes shapes){
		super(xLoc, yLoc, maxHealth * Math.pow(1+TIER_STAT_MULTIPLIER, tier), healthRegen, shapes);
		
		this.tier = tier;
		
		this.currNode = currNode;
		this.nextNode = null;
		this.toNextNode = false;
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		
		this.attackDamage = new BasicStat(attackDamage * Math.pow(1+TIER_STAT_MULTIPLIER, tier));
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
		
		Animation killRewardCombatText = new CombatTextAnimation("+" + (int)(getKillReward()), 0.5,
				                                                 GameState.UI_GOLD, 0.5, 30);
		killRewardCombatText.setLocation(xLoc, yLoc);
		gameState.playAnimation(killRewardCombatText);
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
