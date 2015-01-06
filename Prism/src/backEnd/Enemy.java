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
	
	protected Node currNode, nextNode; //node this enemy is on, and node it's headed to.
	protected boolean toNextNode; //true if this enemy has reached it's next node.
	
	protected double priority; //attack priority of this enemy
	protected int spawnFrame; //frame this enemy spawned on
	
	protected Stat attackDamage, attackDelay, attackRange; //enemy's attack stats
	protected int attackTimer; //tracks when enemy can next attack
	protected Stat moveSpeed; //enemy's move stat
	
	public Enemy(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, double maxHealth,
			     double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     double moveSpeed, PaintableShapes shapes){
		super(xLoc, yLoc, maxHealth, healthRegen, shapes);
		
		this.currNode = currNode;
		this.nextNode = null;
		this.toNextNode = false;
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		
		this.attackDamage = new BasicStat(attackDamage);
		this.attackDelay = new ReverseMultStat(attackDelay);
		this.attackRange = new BasicStat(attackRange);
		this.attackTimer = -1;
		
		this.moveSpeed = new BasicStat(moveSpeed);
		
		this.showHealthBar = true;
		this.healthBarOffset = 0.3;
		this.healthBarWidth = 0.6;
		this.healthBarHeight = 0.2;
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
