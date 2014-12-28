package backEnd;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import util.PaintableShapes;

/**
 * One of the player's towers. Extensions of this class can represent different sorts of towers.
 * 
 * @author Kelton Finch
 */
public abstract class Tower extends Entity {
	
	protected static final int DEFAULT_GHOST_DURATION = 1000;
	protected static final String GHOST_DEBUFF_ID = "ghosttowerdebuff";
	
	protected Node currNode;
	
	protected Stat ghostDuration;
	
	protected double priority;
	protected int spawnFrame;
	protected int tier;
	
	protected Stat attackDamage, attackDelay, attackRange;
	protected int attackTimer;
	
	protected double facing;
	
	public Tower(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, int tier, double maxHealth,
			     double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     PaintableShapes shapes){
		super(xLoc, yLoc, maxHealth, healthRegen, shapes);
		
		this.currNode = currNode;
		
		this.ghostDuration = new BasicStat(DEFAULT_GHOST_DURATION);
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		this.tier = tier;
		
		this.attackDamage = new BasicStat(attackDamage);
		this.attackDelay = new ReverseMultStat(attackDelay);
		this.attackRange = new BasicStat(attackRange);
		this.attackTimer = -1;
		
		this.facing = 0;
	}
	
	protected static PaintableShapes generateBaseShapes(double xLoc, double yLoc){
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {-1, 1, 1, -1};
		double[] yPoints1 = {-1, -1, 1, 1};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BASE);
		
		return result;
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
	
	@Override
	public void postStep(GameState gameState){
		super.postStep(gameState);
		
		//tower aren't removed when they die, they instead become "ghosts" before respawning
		if(currHealth <= 0){
			isActive = true;
			addBuff(new GhostDebuff((int) ghostDuration.modifiedValue));
		}
	}
	
	@Override
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		
	}
}

class GhostDebuff extends TimedBuff {

	public GhostDebuff(int duration) {
		super(Tower.GHOST_DEBUFF_ID, "Ghost Tower", "This tower is dead! It will respawn after a while.",
			  false, false, duration);
	}

	@Override
	public void handleDuplicate(Buff b) {
		throw new IllegalStateException("A dead tower should not be able to die again... ");
	}
	
	@Override
	public void apply(Entity e) {
		//remove all removable buffs
		for(Buff b : e.buffs.values())
			e.removeBuff(b);
		
		//full heal
		e.currHealth = e.maxHealth.modifiedValue;
		
		//suppress everything
		e.attackAction.startSuppress();
		e.moveAction.startSuppress();
		e.specialAction.startSuppress();
		e.passiveAction.startSuppress();
		e.changeAction.startSuppress();
	}

	@Override
	public void remove(Entity e) {
		e.attackAction.endSuppress();
		e.moveAction.endSuppress();
		e.specialAction.endSuppress();
		e.passiveAction.endSuppress();
		e.changeAction.endSuppress();
	}
}
