package backEnd;

import util.PaintableShapes;

public class TowerRG extends SimplePBAOETower {

	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 500;
	public static final double HEALTH_REGEN = 2;
	public static final double ATTACK_DAMAGE = 50;
	public static final double ATTACK_DELAY = 100;
	public static final double ATTACK_RANGE = 0; //TowerRG's attack is a PBAoE, this will be overwritten
	public static final double PROJECTILE_SPEED = 0.2;
	public static final double ATTACK_AOE = 1.2;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6;
	
	public TowerRG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, PROJECTILE_SPEED, ATTACK_AOE, SHOT_ORIGIN_DISTANCE, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(xLoc, yLoc, 0.6, GameState.TOWER_GREEN);
		
		result.addFixedCircle(xLoc, yLoc, 0.4, GameState.TOWER_BASE);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.2, 0, -0.2};
		double[] yPoints1 = {-0.2, 0, 0.2, 0};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_GREEN);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		return null; //TowerRG's attack has no projectile
	}

}
