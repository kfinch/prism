package backEnd;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Animation;
import util.AttachedAnimation;
import util.PaintableShapes;
import util.Point2d;
import util.Vector2d;

/**
 * An abstract entity that has an active status, a location, has hit points, and can take damage or be healed.
 * It also has 'action types' that can be disabled or made immune.
 * Both Tower and Enemy will stem from this.
 * 
 * @author Kelton Finch
 */
public abstract class Entity {
	
	public boolean isActive;
	
	protected GameState gameState;
	
	public Point2d loc;
	
	public Stat maxHealth;
	public double currHealth;
	public Stat receivedDamageModifier;
	public Stat receivedHealingModifier;
	public Stat healthRegen;
	
	public ActionType moveAction; //any action that involves moving this entity
	public ActionType attackAction; //this entity's basic attack(s) (if it has one)
	public ActionType specialAction; //this entity's special actions, abilities, etc ???
	public ActionType passiveAction; //this entity's passive actions, like reloading, regenerating, auras, etc...
	public ActionType changeAction; //this entity's actions that change it, like an upgrade or sell
	
	public PaintableShapes shapes;
	
	public Map<String, Buff> buffs;
	
	public boolean showHealthBar;
	public double healthBarOffset;
	public double healthBarWidth;
	public double healthBarHeight;
	
	public Entity(GameState gameState, Point2d loc, double maxHealth, double healthRegen, PaintableShapes shapes){
		this.isActive = true;
		
		this.gameState = gameState;
		
		this.loc = loc;
		
		this.maxHealth = new BasicStat(maxHealth);
		this.currHealth = this.maxHealth.modifiedValue;
		this.receivedDamageModifier = new BasicStat(0);
		this.receivedHealingModifier = new BasicStat(0);
		this.healthRegen = new BasicStat(healthRegen);
		
		this.moveAction = new ActionType();
		this.attackAction = new ActionType();
		this.specialAction = new ActionType();
		this.passiveAction = new ActionType();
		this.changeAction = new ActionType();
		
		this.shapes = shapes;
		
		buffs = new HashMap<String, Buff>();
		
		showHealthBar = false;
	}
	
	public double harm(double damage, boolean isDirectAttack, Entity source){
		receivedDamageModifier.baseValue = damage;
		receivedDamageModifier.update();
		double modDamage = receivedDamageModifier.modifiedValue;
		if(modDamage < 0)
			modDamage = 0;
		currHealth -= modDamage;
		return modDamage;
	}
	
	public double heal(double healing, boolean isDirectHeal, Entity source){
		receivedHealingModifier.baseValue = healing;
		receivedHealingModifier.update();
		double modHealing = receivedHealingModifier.modifiedValue;
		if(modHealing < 0)
			modHealing = 0;
		if(currHealth + modHealing > maxHealth.modifiedValue)
			modHealing = maxHealth.modifiedValue - currHealth;
		currHealth += modHealing;
		return modHealing;
	}
	
	public void die(){
		isActive = false;
	}
	
	public void moveBy(Vector2d moveVec){
		loc = loc.afterTranslate(moveVec);
		shapes.loc = shapes.loc.afterTranslate(moveVec);
	}
	
	public void moveTo(Point2d newLoc){
		loc = new Point2d(newLoc);
		shapes.loc = new Point2d(newLoc);
	}
	
	public void addBuff(Buff buff){
		if(buffs.containsKey(buff.id)){
			Buff duplicate = buffs.get(buff.id);
			duplicate.remove();
			buff.handleDuplicate(duplicate);
			duplicate.apply(this);
		}
		else{
			buffs.put(buff.id, buff);
			buff.apply(this);
		}
	}
	
	public boolean dispelBuff(Buff buff){
		if(buff.isDispellable){
			removeBuff(buff);
			return true;
		}
		else
			return false;
	}
	
	public void removeBuff(Buff buff){
		buff.remove();
		buffs.remove(buff.id);
	}
	
	public Buff getBuff(String id){
		return buffs.get(id);
	}
	
	public boolean hasBuff(String id){
		return buffs.containsKey(id);
	}
	
	/**
	 * Called right after this entity is added to the game state.
	 */
	public void onSpawn(){}
	
	/**
	 * Called right before this entity is removed from the game state due to isActive == false
	 */
	public void onDespawn(){}
	
	/**
	 * Auras are applied, HoTs and DoTs tick, etc...
	 */
	public void preStep(){
		if(passiveAction.canAct()){
			currHealth += healthRegen.modifiedValue;
			if(currHealth > maxHealth.modifiedValue)
				currHealth = maxHealth.modifiedValue;
		}
		
		for(Buff b : buffs.values()){
			b.step();
		}
	}
	
	/**
	 * Entities move.
	 */
	public void moveStep(){}
	
	/**
	 * Actions! Entities attack and/or use abilities, etc...
	 */
	public void actionStep(){}
	
	/**
	 * Cleanup! Timed out effects are removed, dead entities are removed, etc...
	 */
	public void postStep(){
		if(currHealth <= 0)
			die();
		
		Iterator<Buff> bIter = buffs.values().iterator();
		Buff b;
		while(bIter.hasNext()){
			b = bIter.next();
			if(!b.isActive){
				b.remove();
				bIter.remove();
			}
		}
	}
	
	/**
	 * Paints this entity.
	 */
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		int centerX = (int) (cornerX + loc.x*tileSize);
		int centerY = (int) (cornerY + loc.y*tileSize);
		shapes.loc = new Point2d(loc);
		shapes.paintShapes(g2d, centerX, centerY, tileSize);
		if(showHealthBar && currHealth != maxHealth.modifiedValue)
			paintHealthBar(g2d, centerX, centerY, tileSize);
	}
	
	protected void paintStatusBar(Graphics2D g2d, int centerX, int centerY, int tileSize, double xOffset, double yOffset,
			                      double width, double height, double percentFull, Color emptyColor, Color fullColor){
		g2d.setColor(emptyColor);
		g2d.fillRect((int)(centerX - (width/2 + xOffset)*tileSize), (int)(centerY - (height/2 + yOffset)*tileSize),
				     (int)(width*tileSize), (int)(height*tileSize));
		g2d.setColor(fullColor);
		g2d.fillRect((int)(centerX - (width/2 + xOffset)*tileSize), (int)(centerY - (height/2 + yOffset)*tileSize),
				     (int)(width*percentFull*tileSize), (int)(height*tileSize));
	}
	
	protected void paintHealthBar(Graphics2D g2d, int centerX, int centerY, int tileSize){
		paintStatusBar(g2d, centerX, centerY, tileSize, 0, healthBarOffset, healthBarWidth, healthBarHeight, 
				       currHealth / maxHealth.modifiedValue, GameState.HEALTH_BAR_EMPTY, GameState.HEALTH_BAR_FULL);
	}
	
}
