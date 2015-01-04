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
	
	protected static final int DEFAULT_UPGRADE_MULTIPLIER = 100;
	protected static final String UPGRADE_DEBUFF_ID = "upgradingtowerdebuff";
	
	protected static final int DEFAULT_SELL_DURATION = 200;
	protected static final String SELL_DEBUFF_ID = "sellingtowerdebuff";
	
	protected static final String CANT_UPGRADE_MAX_LEVEL = "Can't upgrade this tower; it's already max level!";
	protected static final String CANT_UPGRADE_NO_PATH = "Can't upgrade this tower; there's no upgrade path with that color.";
	
	protected Node currNode;
	
	protected Stat ghostDuration;
	
	protected double priority;
	protected int spawnFrame;
	protected int tier;
	
	protected Stat attackDamage, attackDelay, attackRange, attackAOE;
	protected boolean canAOE;
	protected int attackTimer;
	
	protected double facing;
	
	protected boolean isLit;
	
	protected PaintableShapes shapes;
	
	public Tower(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, int tier, double maxHealth,
			     double healthRegen, double attackDamage, double attackDelay, double attackRange, double attackAOE,
			     boolean canAOE, PaintableShapes shapes){
		super(xLoc, yLoc, maxHealth, healthRegen, shapes);
		
		this.currNode = currNode;
		
		this.ghostDuration = new BasicStat(DEFAULT_GHOST_DURATION);
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		this.tier = tier;
		
		this.attackDamage = new BasicStat(attackDamage);
		this.attackDelay = new ReverseMultStat(attackDelay);
		this.attackRange = new BasicStat(attackRange);
		this.attackAOE = new BasicStat(attackAOE);
		this.canAOE = canAOE;
		this.attackTimer = -1;
		
		this.facing = 0;
		
		this.isLit = true;
		
		this.shapes = shapes;
	}
	
	protected void prepareUpgradedTower(Tower upgrade){
		Stat.copyStatMods(maxHealth, upgrade.maxHealth);
		Stat.copyStatMods(healthRegen, upgrade.healthRegen);
		Stat.copyStatMods(attackDamage, upgrade.attackDamage);
		Stat.copyStatMods(attackDelay, upgrade.attackDelay);
		Stat.copyStatMods(attackRange, upgrade.attackRange);
		Stat.copyStatMods(attackAOE, upgrade.attackAOE);
		
		for(Buff b : buffs.values())
			upgrade.buffs.put(b.id, b);
		
		double healthPercent = currHealth / maxHealth.modifiedValue;
		upgrade.currHealth = upgrade.maxHealth.modifiedValue * healthPercent;
	}
	
	protected void upgradeToTower(GameState gameState, Tower upgrade){
		prepareUpgradedTower(upgrade);
		gameState.addTower(currNode.xLoc, currNode.yLoc, upgrade);
		currNode.tower = upgrade;
		gameState.towers.add(upgrade);
		upgrade.addBuff(new UpgradingDebuff(tier));
		gameState.towers.remove(this);
	}
	
	public abstract String addRed(GameState gameState);
	
	public abstract String addGreen(GameState gameState);
	
	public abstract String addBlue(GameState gameState);
	
	@Override
	public void onSpawn(GameState gameState){}
	
	@Override
	public void onDespawn(GameState gameState){}
	
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
		
		if(!gameState.isLit(xLoc, yLoc) && isLit){
			moveAction.startSuppress();
			attackAction.startSuppress();
			specialAction.startSuppress();
			passiveAction.startSuppress();
			changeAction.startSuppress();
		}
		else if(gameState.isLit(xLoc, yLoc) && !isLit){
			moveAction.endSuppress();
			attackAction.endSuppress();
			specialAction.endSuppress();
			passiveAction.endSuppress();
			changeAction.endSuppress();
		}
		
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
			addBuff(new GhostDebuff());
		}
	}
}

class DisableTowerDebuff extends TimedBuff {
	
	public DisableTowerDebuff(String id, String name, String description, int duration){
		super(id, name, description, false, false, duration);
	}
	
	@Override
	public void handleDuplicate(Buff b){}
	
	@Override
	public void apply(Entity e) {
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

class GhostDebuff extends DisableTowerDebuff {

	public GhostDebuff() {
		super(Tower.GHOST_DEBUFF_ID, "Ghost Tower", "This tower is a ghost, it will rematerialize after some time.",
			  Tower.DEFAULT_GHOST_DURATION);
	}

	@Override
	public void apply(Entity e){
		super.apply(e);
		//remove all removable buffs
		for(Buff b : e.buffs.values())
			e.dispelBuff(b);
				
		//full heal
		e.currHealth = e.maxHealth.modifiedValue;
	}

}

class UpgradingDebuff extends DisableTowerDebuff {
	
	public UpgradingDebuff(int tier) {
		super(Tower.UPGRADE_DEBUFF_ID, "Upgrading...", "This tower is upgrading, and is disabled until it is finished.",
		      Tower.DEFAULT_UPGRADE_MULTIPLIER*tier);
	}
}


