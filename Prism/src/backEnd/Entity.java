package backEnd;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Animation;
import util.AttachedAnimation;
import util.PaintableShapes;

/**
 * An abstract entity that has an active status, a location, has hit points, and can take damage or be healed.
 * It also has 'action types' that can be disabled or made immune.
 * Both Tower and Enemy will stem from this.
 * 
 * @author Kelton Finch
 */
public abstract class Entity {
	
	public boolean isActive;
	
	public double xLoc, yLoc;
	
	protected Stat maxHealth;
	protected double currHealth;
	protected Stat receivedDamageModifier;
	protected Stat receivedHealingModifier;
	protected Stat healthRegen;
	
	protected ActionType moveAction; //any action that involves moving this entity
	protected ActionType attackAction; //this entity's basic attack(s) (if it has one)
	protected ActionType specialAction; //this entity's special actions, abilities, etc ???
	protected ActionType passiveAction; //this entity's passive actions, like reloading, regenerating, auras, etc...
	protected ActionType changeAction; //this entity's actions that change it, like an upgrade or sell
	
	protected PaintableShapes shapes;
	
	protected Map<String, Buff> buffs;
	
	protected boolean showHealthBar;
	protected double healthBarOffset;
	protected double healthBarWidth;
	protected double healthBarHeight;
	
	public Entity(double xLoc, double yLoc, double maxHealth, double healthRegen, PaintableShapes shapes){
		this.isActive = true;
		
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
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
	
	public void harm(double damage){
		receivedDamageModifier.baseValue = damage;
		receivedDamageModifier.update();
		double modDamage = receivedDamageModifier.modifiedValue;
		if(modDamage < 0)
			modDamage = 0;
		currHealth -= modDamage;
	}
	
	public void heal(double healing){
		receivedHealingModifier.baseValue = healing;
		receivedHealingModifier.update();
		double modHealing = receivedHealingModifier.modifiedValue;
		if(modHealing < 0)
			modHealing = 0;
		currHealth += modHealing;
		if(currHealth > maxHealth.modifiedValue)
			currHealth = maxHealth.modifiedValue;
	}
	
	public void moveBy(double changeXLoc, double changeYLoc){
		xLoc += changeXLoc;
		yLoc += changeYLoc;
		shapes.xLoc = xLoc;
		shapes.yLoc = yLoc;
	}
	
	public void moveTo(double newXLoc, double newYLoc){
		xLoc = newXLoc;
		yLoc = newYLoc;
		shapes.xLoc = xLoc;
		shapes.yLoc = yLoc;
	}
	
	public void addBuff(Buff buff, GameState gameState){
		if(buffs.containsKey(buff.id))
			buff.handleDuplicate(buffs.get(buff.id), gameState);
		else{
			buffs.put(buff.id, buff);
			buff.apply(this, gameState);
		}
	}
	
	public boolean dispelBuff(Buff buff, GameState gameState){
		if(buff.isDispellable){
			removeBuff(buff, gameState);
			return true;
		}
		else
			return false;
	}
	
	public void removeBuff(Buff buff, GameState gameState){
		buff.remove(this, gameState);
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
	public void onSpawn(GameState gameState){}
	
	/**
	 * Called right before this entity is removed from the game state due to isActive == false
	 */
	public void onDespawn(GameState gameState){}
	
	/**
	 * Auras are applied, HoTs and DoTs tick, etc...
	 */
	public void preStep(GameState gameState){
		if(passiveAction.canAct()){
			currHealth += healthRegen.modifiedValue;
			if(currHealth > maxHealth.modifiedValue)
				currHealth = maxHealth.modifiedValue;
		}
		
		for(Buff b : buffs.values()){
			b.step(this, gameState);
		}
	}
	
	/**
	 * Entities move.
	 */
	public void moveStep(GameState gameState){
		
	}
	
	/**
	 * Actions! Entities attack and/or use abilities, etc...
	 */
	public void actionStep(GameState gameState){
		
	}
	
	/**
	 * Cleanup! Timed out effects are removed, dead entities are removed, etc...
	 */
	public void postStep(GameState gameState){
		if(currHealth <= 0)
			isActive = false;
		
		for(Buff b : buffs.values()){
			if(!b.isActive)
				removeBuff(b, gameState);
		}
	}
	
	/**
	 * Paints this entity.
	 */
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		int centerX = (int) (cornerX + xLoc*tileSize);
		int centerY = (int) (cornerY + yLoc*tileSize);
		shapes.xLoc = xLoc;
		shapes.yLoc = yLoc;
		shapes.paintShapes(g2d, centerX, centerY, tileSize);
		if(showHealthBar && currHealth != maxHealth.modifiedValue)
			paintHealthBar(g2d, centerX, centerY, tileSize);
	}
	
	protected void paintHealthBar(Graphics2D g2d, int centerX, int centerY, int tileSize){
		g2d.setColor(GameState.HEALTH_BAR_EMPTY);
		g2d.fillRect((int)(centerX - (healthBarWidth/2)*tileSize), (int)(centerY + (healthBarOffset)*tileSize),
			         (int)(healthBarWidth*tileSize), (int)(healthBarHeight*tileSize));
		
		double healthPercent = currHealth / maxHealth.modifiedValue;
		g2d.setColor(GameState.HEALTH_BAR_FULL);
		g2d.fillRect((int)(centerX - (healthBarWidth/2)*tileSize), (int)(centerY + (healthBarOffset)*tileSize),
			         (int)(healthBarWidth*healthPercent*tileSize), (int)(healthBarHeight*tileSize));
	}
	
}
