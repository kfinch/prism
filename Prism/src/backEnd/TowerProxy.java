package backEnd;

import java.awt.Graphics2D;

import util.PaintableShapes;
import util.Point2d;


/*
 * Acts as a proxy for other towers that are mid teleport.
 * Draws itself the same as the tower it's proxying for, any healing, harming, or buffs is forwarded to tower proxied for.
 * This means that AoE that catches both proxy and source will double hit the tower. //TODO: reasonable way to prevent that?
 */
public class TowerProxy extends Tower {

	public static String ID = "";
	public static String NAME = "";
	public static String DESCRIPTION = "";
	
	Tower proxied;
	
	public TowerProxy(GameState gameState, Point2d loc, Tower proxied, Node currNode) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, proxied.priority, proxied.spawnFrame, proxied.tier,
		      proxied.maxHealth.modifiedValue, 0, 0, 0, 0, 0, false, null);
		this.proxied = proxied;
	}

	@Override
	public void harm(double damage, boolean isDirectAttack, Entity source){
		proxied.harm(damage, isDirectAttack, source);
	}
	
	@Override
	public void heal(double healing, boolean isDirectHeal, Entity source){
		proxied.heal(healing, isDirectHeal, source);
	}
	
	@Override
	public void addBuff(Buff buff){
		proxied.addBuff(buff);
	}
	
	@Override
	public boolean dispelBuff(Buff buff){
		return proxied.dispelBuff(buff);
	}
	
	@Override
	public void removeBuff(Buff buff){
		proxied.removeBuff(buff);
	}
	
	@Override
	public Buff getBuff(String id){
		return proxied.getBuff(id);
	}
	
	@Override
	public boolean hasBuff(String id){
		return proxied.hasBuff(id);
	}
	
	@Override
	public void preStep(){
		if(!proxied.isActive)
			isActive = false;
	}
	
	@Override
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		int xOffset = (int)(proxied.loc.x - loc.x)*tileSize;
		int yOffset = (int)(proxied.loc.y - loc.y)*tileSize;
		proxied.paintEntity(g2d, cornerX-xOffset, cornerY-yOffset, tileSize);
	}
	
}
