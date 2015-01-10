package backEnd;

import java.awt.AlphaComposite;
import java.awt.Color;
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
	
	protected static final int GHOST_DURATION = 2000; //TODO: look into ways of making ghost more harmfruu
	
	protected static final String UPGRADE_DEBUFF_ID = "upgradingtowerdebuff";
	
	protected static final int DEFAULT_SELL_DURATION = 100;
	protected static final String SELL_DEBUFF_ID = "sellingtowerdebuff";
	
	protected static final int UPGRADE_DURATION_MULTIPLIER = 100;
	
	protected static final String CANT_UPGRADE_MAX_LEVEL = "This tower is already max level!";
	protected static final String CANT_UPGRADE_NO_PATH = "There's no upgrade path with that color.";
	protected static final String CANT_UPGRADE_ACTION_DISABLED = "This tower can't currently do that action";
	
	protected static final double HEALTH_BAR_OFFSET = -0.7;
	protected static final double HEALTH_BAR_WIDTH = 1.6;
	protected static final double HEALTH_BAR_HEIGHT = 0.3;
	
	protected static final double T1G0_HEALTH = 100;
	protected static final double T1G1_HEALTH = 250;
	
	protected static final double T2G0_HEALTH = 200;
	protected static final double T2G1_HEALTH = 400;
	protected static final double T2G2_HEALTH = 750;
	
	protected static final double T3G0_HEALTH = 400;
	protected static final double T3G1_HEALTH = 750;
	protected static final double T3G2_HEALTH = 1400;
	protected static final double T3G3_HEALTH = 2000;
	
	protected static final double T4G0_HEALTH = 800;
	protected static final double T4G1_HEALTH = 1300;
	protected static final double T4G2_HEALTH = 2000;
	protected static final double T4G3_HEALTH = 3200;
	protected static final double T4G4_HEALTH = 4000;
	
	protected static final double BASE_HEALTH_REGEN = 0.02;
	
	protected Node currNode;
	
	protected boolean isGhost;
	protected int ghostTimer;
	
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
		
		this.isGhost = false;
		
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
		
		this.showHealthBar = true;
		this.healthBarOffset = HEALTH_BAR_OFFSET;
		this.healthBarWidth = HEALTH_BAR_WIDTH;
		this.healthBarHeight = HEALTH_BAR_HEIGHT;
	}
	
	public void setGhost(boolean newIsGhost){
		if(newIsGhost && !isGhost){
			isGhost = true;
			ghostTimer = GHOST_DURATION;
			showHealthBar = false;
			attackAction.startSuppress();
			moveAction.startSuppress();
			specialAction.startSuppress();
			passiveAction.startSuppress();
		}
		else if(!newIsGhost && isGhost){
			isGhost = false;
			showHealthBar = true;
			currHealth = maxHealth.modifiedValue;
			attackAction.endSuppress();
			moveAction.endSuppress();
			specialAction.endSuppress();
			passiveAction.endSuppress();
		}
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
	
	/*
	 * Attempts to upgrade to the given tower, returns true if it works, false if it can't.
	 */
	protected boolean upgradeToTower(GameState gameState, Tower upgrade){
		if(changeAction.canAct() && !isGhost){
			prepareUpgradedTower(upgrade);
			gameState.addTower(currNode.xLoc, currNode.yLoc, upgrade);
			currNode.tower = upgrade;
			gameState.towers.add(upgrade);
			upgrade.addBuff(new UpgradingDebuff(tier), gameState);
			gameState.towers.remove(this);
			return true;
		}
		else
			return false;
	}
	
	protected Tower generateRedUpgrade(){
		return null;
	}
	
	protected Tower generateGreenUpgrade(){
		return null;
	}
	
	protected Tower generateBlueUpgrade(){
		return null;
	}
	
	public String addRed(GameState gameState){
		Tower upgrade = generateRedUpgrade();
		if(upgrade == null)
			return Tower.CANT_UPGRADE_MAX_LEVEL;
		else if(!upgradeToTower(gameState, upgrade))
			return Tower.CANT_UPGRADE_ACTION_DISABLED;
		else
			return null;
	}
	
	public String addGreen(GameState gameState){
		Tower upgrade = generateGreenUpgrade();
		if(upgrade == null)
			return Tower.CANT_UPGRADE_MAX_LEVEL;
		else if(!upgradeToTower(gameState, upgrade))
			return Tower.CANT_UPGRADE_ACTION_DISABLED;
		else
			return null;
	}
	
	public String addBlue(GameState gameState){
		Tower upgrade = generateBlueUpgrade();
		if(upgrade == null)
			return Tower.CANT_UPGRADE_MAX_LEVEL;
		else if(!upgradeToTower(gameState, upgrade))
			return Tower.CANT_UPGRADE_ACTION_DISABLED;
		else
			return null;
	}
	
	public String sell(GameState gameState){
		if(changeAction.canAct()){
			addBuff(new SellingDebuff(), gameState);
			return null;
		}
		else{
			return CANT_UPGRADE_ACTION_DISABLED;
		}
	}
	
	protected static PaintableShapes generateBaseShapes(double xLoc, double yLoc){
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {-0.97, 0.97, 0.97, -0.97};
		double[] yPoints1 = {-0.97, -0.97, 0.97, 0.97};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BASE);
		
		return result;
	}
	
	@Override
	public void preStep(GameState gameState){
		super.preStep(gameState);
		
		boolean currIsLit = true;
		for(int xi=currNode.xLoc-1; xi<=currNode.xLoc+1; xi++){
			for(int yi=currNode.yLoc-1; yi<=currNode.yLoc+1; yi++){
				if(!gameState.isLit(xi, yi))
					currIsLit = false;
			}
		}
		
		if(!currIsLit && isLit){
			moveAction.startSuppress();
			attackAction.startSuppress();
			specialAction.startSuppress();
			passiveAction.startSuppress();
			changeAction.startSuppress();
			isLit = false;
		}
		else if(currIsLit && !isLit){
			moveAction.endSuppress();
			attackAction.endSuppress();
			specialAction.endSuppress();
			passiveAction.endSuppress();
			changeAction.endSuppress();
			isLit = true;
		}
		
		if(passiveAction.canAct()){
			if(attackTimer >= 0)
				attackTimer++;
			if(attackTimer >= attackDelay.modifiedValue)
				attackTimer = -1;
		}
	}
	
	@Override
	public void die(GameState gameState){
		setGhost(true); //rather than being completely removed, towers become ghosts on death
	}
	
	@Override
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		if(isGhost)
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		
		super.paintEntity(g2d, cornerX, cornerY, tileSize);
		
		int centerX = (int) (cornerX + xLoc*tileSize); //TODO: this basically repeated code =/ Solution?
		int centerY = (int) (cornerY + yLoc*tileSize);
		if(hasBuff(UPGRADE_DEBUFF_ID)){
			TimedBuff tb = (TimedBuff) buffs.get(UPGRADE_DEBUFF_ID);
			double percentComplete = ((double)tb.initialDuration - (double)tb.timer) / ((double)tb.initialDuration);
			super.paintStatusBar(g2d, centerX, centerY, tileSize, 0, 0, 1.8, 0.6, percentComplete,
					             Color.black, Color.white);
		}
		if(hasBuff(SELL_DEBUFF_ID)){
			TimedBuff tb = (TimedBuff) buffs.get(SELL_DEBUFF_ID);
			double percentComplete = ((double)tb.timer) / ((double)tb.initialDuration);
			super.paintStatusBar(g2d, centerX, centerY, tileSize, 0, 0, 1.8, 0.6, percentComplete,
					             Color.black, Color.white);
		}
		
		if(isGhost)
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}

class DisableTowerDebuff extends TimedBuff {
	
	public DisableTowerDebuff(String id, String name, String description, int duration){
		super(id, name, description, false, false, duration);
	}
	
	@Override
	public void handleDuplicate(Buff b, GameState gameState){}
	
	@Override
	public void apply(Entity e, GameState gameState) {
		//suppress everything (SHUT. DOWN. EVERYTHING.)
		e.attackAction.startSuppress();
		e.moveAction.startSuppress();
		e.specialAction.startSuppress();
		e.passiveAction.startSuppress();
		e.changeAction.startSuppress();
	}

	@Override
	public void remove(Entity e, GameState gameState) {
		e.attackAction.endSuppress();
		e.moveAction.endSuppress();
		e.specialAction.endSuppress();
		e.passiveAction.endSuppress();
		e.changeAction.endSuppress();
	}
}

class UpgradingDebuff extends DisableTowerDebuff {
	
	public UpgradingDebuff(int tier) {
		super(Tower.UPGRADE_DEBUFF_ID, "Upgrading...", "This tower is upgrading, and is disabled until it is finished.",
		      Tower.UPGRADE_DURATION_MULTIPLIER*tier);
	}
}

class SellingDebuff extends DisableTowerDebuff {
	
	public SellingDebuff(){
		super(Tower.SELL_DEBUFF_ID, "Selling...",
			  "This tower is being sold for resources, and is disabled until it is finished.", Tower.DEFAULT_SELL_DURATION);
	}
	
	@Override
	public void remove(Entity e, GameState gameState){
		super.remove(e, gameState);
		e.isActive = false;
		//TODO: give player resources for the sell! (reduced if tower is ghost on sell completion)
	}
}


