package backEnd;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import util.PaintableShapes;

/**
 * An abstract entity that has an active status, a location, has hit points, and can take damage or be healed.
 * It also has 'action types' that can be disabled or made immune.
 * Both Tower and Enemy will stem from this.
 * 
 * @author Kelton Finch
 */
public abstract class Entity {

	protected boolean isActive;
	
	protected double xLoc, yLoc;
	
	protected Stat maxHealth;
	protected double currHealth;
	protected Stat receivedDamageModifier;
	protected Stat receivedHealingModifier;
	protected Stat healthRegen;
	
	protected ActionType moveAction; //any action that involves moving this entity
	protected ActionType attackAction; //this entity's basic attack(s) (if it has one)
	protected ActionType specialAction; //this entity's special actions, abilities, etc ???
	protected ActionType passiveAction; //this entity's passive actions, like reloading, regenerating, auras, etc...
	
	protected PaintableShapes shapes;
	
	protected Set<Buff> buffs;
	
	public Entity(double xLoc, double yLoc, double maxHealth, double healthRegen, PaintableShapes shapes){
		this.isActive = true;
		
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
		this.maxHealth = new BasicStat(maxHealth);
		this.currHealth = maxHealth;
		this.receivedDamageModifier = new BasicStat(0);
		this.receivedHealingModifier = new BasicStat(0);
		this.healthRegen = new BasicStat(healthRegen);
		
		this.moveAction = new ActionType();
		this.attackAction = new ActionType();
		this.specialAction = new ActionType();
		this.passiveAction = new ActionType();
		
		this.shapes = shapes;
		
		buffs = new HashSet<Buff>();
	}
	
	public void hurt(double damage){
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
	
	public void addBuff(Buff buff){
		buffs.add(buff);
		buff.apply(this);
	}
	
	public void removeBuff(Buff buff){
		buff.remove(this);
		buffs.remove(buff);
	}
	
	/**
	 * Auras are applied, HoTs and DoTs tick, etc...
	 */
	public void preStep(GameState gameState){
		if(passiveAction.canAct()){
			currHealth += healthRegen.modifiedValue;
			if(currHealth > maxHealth.modifiedValue)
				currHealth = maxHealth.modifiedValue;
		}
		
		for(Buff b : buffs){
			b.step(this);
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
		if(currHealth < 0)
			isActive = false;
		
		for(Buff b : buffs){
			if(!b.isActive){
				b.remove(this);
				buffs.remove(b);
			}
		}
	}
	
	/**
	 * Paints this entity.
	 */
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		shapes.paintShape(g2d, cornerX, cornerY, tileSize);
	}
	
}
