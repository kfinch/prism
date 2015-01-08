package backEnd;

import util.PaintableShapes;

public class TowerG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = 200;
	public static final double HEALTH_REGEN = MAX_HEALTH / 200;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(xLoc, yLoc));
		attackAction.startSuppress(); //TowerG can't attack
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRG(currNode, xLoc, yLoc, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerGG(currNode, xLoc, yLoc, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerGB(currNode, xLoc, yLoc, spawnFrame);
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.5, GameState.TOWER_GREEN);
		
		return result;
	}
}
