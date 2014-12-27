package backEnd;

import java.util.ArrayList;
import java.util.List;

import util.PaintableShapes;

/**
 * One of the player's towers. Extensions of this class can represent different sorts of towers.
 * 
 * @author Kelton Finch
 */
public abstract class Tower extends Entity {
	
	protected Node currNode;
	
	protected double priority;
	protected int spawnFrame;
	
	protected Stat attackDamage, attackDelay, attackRange;
	protected int attackTimer;
	
	public Tower(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, double maxHealth,
			     double healthRegen, double attackDamage, double attackDelay, double attackRange,
			     PaintableShapes shapes){
		super(xLoc, yLoc, maxHealth, healthRegen, shapes);
		
		this.currNode = currNode;
		
		this.priority = priority;
		this.spawnFrame = spawnFrame;
		
		this.attackDamage = new BasicStat(attackDamage);
		this.attackDelay = new ReverseMultStat(attackDelay);
		this.attackRange = new BasicStat(attackRange);
		this.attackTimer = -1;
	}
	
	public static PaintableShapes generateBaseShapes(double xLoc, double yLoc){
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
}
