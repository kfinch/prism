package backEnd;

import java.awt.Graphics2D;

import util.PaintableShapes;


/*
 * Acts as a proxy for other towers that are mid teleport.
 * Draws itself the same as the tower it's proxying for, any healing, harming, or buffs is forwarded to tower proxied for.
 * This means that AoE that catches both proxy and source will double hit the tower. //TODO: reasonable way to prevent that?
 */
public class TowerProxy extends Tower {

	Tower proxied;
	
	public TowerProxy(Tower proxied, Node currNode, double xLoc, double yLoc) {
		super(currNode, xLoc, yLoc, proxied.priority, proxied.spawnFrame, proxied.tier, proxied.maxHealth.modifiedValue,
				0, 0, 0, 0, 0, false, null);
		this.proxied = proxied;
	}

	@Override
	public void harm(double damage){
		proxied.harm(damage);
	}
	
	@Override
	public void heal(double healing){
		proxied.heal(healing);
	}
	
	@Override
	public void addBuff(Buff buff, GameState gameState){
		proxied.addBuff(buff, gameState);
	}
	
	@Override
	public boolean dispelBuff(Buff buff, GameState gameState){
		return proxied.dispelBuff(buff, gameState);
	}
	
	@Override
	public void removeBuff(Buff buff, GameState gameState){
		proxied.removeBuff(buff, gameState);
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
	public void preStep(GameState gameState){
		if(!proxied.isActive)
			isActive = false;
	}
	
	@Override
	public void paintEntity(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		int xOffset = (int)(proxied.xLoc - xLoc)*tileSize;
		int yOffset = (int)(proxied.yLoc - yLoc)*tileSize;
		proxied.paintEntity(g2d, cornerX-xOffset, cornerY-yOffset, tileSize);
	}
	
}
