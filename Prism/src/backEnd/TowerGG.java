package backEnd;

import util.PaintableShapes;

public class TowerGG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 750;
	public static final double HEALTH_REGEN = MAX_HEALTH / 200;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerGG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(xLoc, yLoc));
		attackAction.startSuppress(); //TowerGG can't attack
	}
	
	@Override
	public String addRed(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public String addGreen(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public String addBlue(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.65, GameState.TOWER_GREEN);
		result.addFixedCircle(0, 0, 0.4, GameState.TOWER_BASE);
		result.addFixedCircle(0, 0, 0.2, GameState.TOWER_GREEN);
		
		return result;
	}
}
