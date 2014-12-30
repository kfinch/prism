package backEnd;

import util.PaintableShapes;

public class TowerBB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 200;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 60;
	public static final double ATTACK_DELAY = 100;
	public static final double ATTACK_RANGE = 7;
	public static final double PROJECTILE_SPEED = 0.15;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6; //TODO: update
	public static final double ATTACK_AOE = 0.5;
	
	public TowerBB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
	}
	
	private static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 11;
		double[] xPoints1 = {-0.5, -0.2, -0.2, 0.2, 0.2, -0.2, 0.2, 0.2, -0.2, -0.2, -0.5};
		double[] yPoints1 = {0.2, 0.2, 0.4, 0.4, 0.2, 0.0, -0.2, -0.4, -0.4, -0.2, -0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 5;
		double[] xPoints2 = {0.3, 0.7, 0.7, 0.3, -0.1};
		double[] yPoints2 = {-0.2, -0.2, 0.2, 0.2, 0.0};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(xLoc, yLoc, 0.22, GameState.PROJECTILE_BLUE);
		
		return result;
	}
}
