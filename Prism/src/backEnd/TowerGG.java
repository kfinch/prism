package backEnd;

import util.PaintableShapes;

public class TowerGG extends SimpleTower{
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 700;
	public static final double HEALTH_REGEN = 3;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerGG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, generateShapes(xLoc, yLoc));
		attackAction.startDisable(); //TowerGG can't attack
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.6, GameState.TOWER_GREEN);
		
		result.addFixedCircle(0, 0, 0.4, GameState.TOWER_BASE);
		
		result.addFixedCircle(0, 0, 0.2, GameState.TOWER_GREEN);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		return null; //TowerGG can't attack
	}
}
