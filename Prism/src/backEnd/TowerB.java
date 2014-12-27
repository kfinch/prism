package backEnd;

import util.PaintableShapes;

public class TowerB extends SimpleAOETower {
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 100;
	public static final double HEALTH_REGEN = 0.1;
	public static final double ATTACK_DAMAGE = 20;
	public static final double ATTACK_DELAY = 100;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = 0.15;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6;
	public static final double SHOT_AOE = 0.4;
	
	public TowerB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, PROJECTILE_SPEED, SHOT_AOE, SHOT_ORIGIN_DISTANCE, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 12;
		double[] xPoints1 = {-0.4, -0.2, -0.2, 0.2, 0.2, 0.6, 0.6, 0.2, 0.2, -0.2, -0.2, -0.4};
		double[] yPoints1 = {-0.2, -0.2, -0.4, -0.4, -0.2, -0.2, 0.2, 0.2, 0.4, 0.4, 0.2, 0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(xLoc, yLoc, 0.2, GameState.PROJECTILE_BLUE);
		
		return result;
	}
}
