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
	
	public void harm(double damage, Entity source){
		receivedDamageModifier.baseValue = damage;
		receivedDamageModifier.update();
		double modDamage = receivedDamageModifier.modifiedValue;
		if(modDamage < 0)
			modDamage = 0;
		currHealth -= modDamage;
	}
	
	public void heal(double healing, Entity source){
		receivedHealingModifier.baseValue = healing;
		receivedHealingModifier.update();
		double modHealing = receivedHealingModifier.modifiedValue;
		if(modHealing < 0)
			modHealing = 0;
		currHealth += modHealing;
		if(currHealth > maxHealth.modifiedValue)
			currHealth = maxHealth.modifiedValue;
	}
	
	public void die(GameState gameState){
		isActive = false;
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
			die(gameState);
		
		Iterator<Buff> bIter = buffs.values().iterator();
		Buff b;
		while(bIter.hasNext()){
			b = bIter.next();
			if(!b.isActive){
				b.remove(this, gameState);
				bIter.remove();
			}
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
