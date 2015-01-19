package backEnd;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import util.PaintableShapes;
import util.Point2d;

/**
 * One of the player's towers. Extensions of this class can represent different sorts of towers.
 * 
 * @author Kelton Finch
 */
public abstract class Tower extends EntityWithAttack {
	
	protected static final int GHOST_DURATION = 2000; //TODO: look into ways of making ghost more harmfruu
	
	protected static final String UPGRADE_DEBUFF_ID = "upgradingtowerdebuff";
	protected static final int UPGRADE_DURATION_MULTIPLIER = 100;
	
	protected static final String SELL_DEBUFF_ID = "sellingtowerdebuff";
	protected static final int DEFAULT_SELL_DURATION = 100;
	
	protected static final String TELEPORT_DEBUFF_ID = "teleportingtowerdebuff";
	protected static final int DEFAULT_TELEPORT_DURATION = 100;
	
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
	
	public final String id;
	public final String name;
	public final String description;
	
	public Node currNode;
	
	public boolean isGhost;
	public int ghostTimer;
	
	public double priority;
	public int spawnFrame;
	public int tier;
	
	public Stat attackAOE;
	public boolean canAOE;
	
	public double facing;
	
	public boolean isLit;
	
	public PaintableShapes shapes;
	
	public Tower(String id, String name, String description,
			     GameState gameState, Point2d loc, Node currNode, double priority, int spawnFrame, int tier,
			     double maxHealth, double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     double attackAOE, boolean canAOE, PaintableShapes shapes){
		super(gameState, loc, maxHealth, healthRegen, attackDamage, attackDelay, attackRange, shapes);
		
		this.id = id;
		this.name = name;
		this.description = description;
		
		this.currNode = currNode;
		
		this.isGhost = false;
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		this.tier = tier;
		
		this.attackAOE = new BasicStat(attackAOE);
		this.canAOE = canAOE;
		
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
	 * Attempts to change this tower to the given tower, returns true if it works, false if it can't.
	 */
	protected boolean morphTower(Tower morph){
		if(changeAction.canAct() && !isGhost){
			prepareUpgradedTower(morph); //generate morphed version
			
			//remove this, replace it with upgraded version
			gameState.removeTower(this);
			gameState.addTower(currNode.xLoc, currNode.yLoc, morph);
			
			morph.addBuff(new UpgradingDebuff(gameState, tier));
			return true;
		}
		else
			return false;
	}
	
	/*
	 * Attempts to teleport this tower to the target location, returns true if it works, false if it can't.
	 */
	protected boolean teleportTower(Node dst){
		if(changeAction.canAct() && !isGhost && gameState.isValidTowerLocation(dst.xLoc, dst.yLoc)){
			addBuff(new TeleportingDebuff(gameState, dst));
			return true;
		}
		else
			return false;
	}
	
	public String sellTower(){
		if(changeAction.canAct()){
			addBuff(new SellingDebuff(gameState));
			return null;
		}
		else{
			return CANT_UPGRADE_ACTION_DISABLED;
		}
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
	
	public String addRed(){
		Tower upgrade = generateRedUpgrade();
		if(upgrade == null)
			return Tower.CANT_UPGRADE_MAX_LEVEL;
		else if(!morphTower(upgrade))
			return Tower.CANT_UPGRADE_ACTION_DISABLED;
		else
			return null;
	}
	
	public String addGreen(){
		Tower upgrade = generateGreenUpgrade();
		if(upgrade == null)
			return Tower.CANT_UPGRADE_MAX_LEVEL;
		else if(!morphTower(upgrade))
			return Tower.CANT_UPGRADE_ACTION_DISABLED;
		else
			return null;
	}
	
	public String addBlue(){
		Tower upgrade = generateBlueUpgrade();
		if(upgrade == null)
			return Tower.CANT_UPGRADE_MAX_LEVEL;
		else if(!morphTower(upgrade))
			return Tower.CANT_UPGRADE_ACTION_DISABLED;
		else
			return null;
	}
	
	protected static PaintableShapes generateBaseShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {-0.97, 0.97, 0.97, -0.97};
		double[] yPoints1 = {-0.97, -0.97, 0.97, 0.97};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BASE);
		
		return result;
	}
	
	//called when Tower is moved from src to dst
	public void onMove(Node src, Node dst){}
	
	//called when Tower is upgraded (to this)
	public void onUpgrade(){} //TODO: do I need this (because onSpawn() is also called on upgrade)
	
	@Override
	public void preStep(){
		super.preStep();
		
		boolean currIsLit = true;
		for(int xi=currNode.xLoc-1; xi<=currNode.xLoc+1; xi++){
			for(int yi=currNode.yLoc-1; yi<=currNode.yLoc+1; yi++){
				if(!gameState.isLit(new Point2d(xi, yi)))
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
	}
	
	@Override
	public void postStep(){
		super.postStep();
		//if(currNode.tower != this){ //TODO: remove debugging
		//	System.out.println("Representation mismatch with " + this);
		//	System.out.println("I'm @ " + xLoc + " " + yLoc +
		//			           "  curr node = " + currNode + " @ " + currNode.xLoc + " " + currNode.yLoc);
		//	System.out.println("My node's tower is " + currNode.tower);
		//}
	}
	
	@Override
	public void die(){
		setGhost(true); //rather than being completely removed, towers become ghosts on death
	}
	
	@Override
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		if(isGhost)
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		
		super.paintEntity(g2d, cornerX, cornerY, tileSize);
		
		int centerX = (int) (cornerX + loc.x*tileSize); //TODO: this basically repeated code =/ Solution?
		int centerY = (int) (cornerY + loc.y*tileSize);
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
		if(hasBuff(TELEPORT_DEBUFF_ID)){
			TimedBuff tb = (TimedBuff) buffs.get(TELEPORT_DEBUFF_ID);
			double percentComplete = ((double)tb.initialDuration - (double)tb.timer) / ((double)tb.initialDuration);
			super.paintStatusBar(g2d, centerX, centerY, tileSize, 0, 0, 1.8, 0.6, percentComplete,
					             Color.black, Color.white);
		}
		
		if(isGhost)
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}

class DisableTowerDebuff extends TimedBuff {
	
	public DisableTowerDebuff(GameState gameState, String id, String name, String description, int duration){
		super(gameState, id, name, description, false, false, duration);
	}
	
	@Override
	public void handleDuplicate(Buff b){}
	
	@Override
	public void apply(Entity e) {
		//suppress everything (SHUT. DOWN. EVERYTHING.)
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

class UpgradingDebuff extends DisableTowerDebuff {
	
	public UpgradingDebuff(GameState gameState, int tier) {
		super(gameState, Tower.UPGRADE_DEBUFF_ID, "Upgrading...",
			  "This tower is upgrading, and is disabled until it is finished.",
		      Tower.UPGRADE_DURATION_MULTIPLIER*tier);
	}
}

class SellingDebuff extends DisableTowerDebuff {
	
	public SellingDebuff(GameState gameState){
		super(gameState, Tower.SELL_DEBUFF_ID, "Selling...",
			  "This tower is being sold for resources, and is disabled until it is finished.", Tower.DEFAULT_SELL_DURATION);
	}
	
	@Override
	public void remove(Entity e){
		super.remove(e);
		e.isActive = false;
		//TODO: give player resources for the sell! (reduced if tower is ghost on sell completion)
	}
}

//TODO: THINK LONG AND HARD ABOUT POSSIBLE SPECIAL CASES THAT WILL HAVE INCORRECT BEHAVIOR.
class TeleportingDebuff extends DisableTowerDebuff {

	public Node dst;
	public Tower proxy;
	
	public TeleportingDebuff(GameState gameState, Node dst){
		super(gameState, Tower.TELEPORT_DEBUFF_ID, "Teleporting...",
			  "This tower is teleporting, and is disabled until it finishes",
			  Tower.DEFAULT_TELEPORT_DURATION);
		this.dst = dst;
	}
	
	@Override
	public void apply(Entity e){
		super.apply(e);
		Tower t = (Tower) e;
		proxy = new TowerProxy(gameState, new Point2d(dst.xLoc, dst.yLoc), t, dst);
		gameState.addTower(dst.xLoc, dst.yLoc, proxy);
	}
	
	@Override
	public void remove(Entity e){
		super.remove(e);
		Tower t = (Tower) e;
		gameState.moveTower(dst.xLoc, dst.yLoc, t); //this kills the proxy
	}
	
}


