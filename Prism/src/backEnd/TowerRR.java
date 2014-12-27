package backEnd;

import util.PaintableShapes;

public class TowerRR extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 250;
	public static final double HEALTH_REGEN = 0.1;
	public static final double ATTACK_DAMAGE = 15;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = 0.2;
	public static final double SHOT_ORIGIN_DISTANCE = 0.7;
	
	public TowerRR(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.45, 0, 0.45};
		double[] yPoints1 = {-0.05, -0.5, -0.05};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 4;
		double[] xPoints2 = {0, 0.7, 0.7, 0};
		double[] yPoints2 = {-0.05, -0.05, -0.2, -0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 3;
		double[] xPoints3 = {-0.45, 0, 0.45};
		double[] yPoints3 = {0.05, 0.5, 0.05};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		int nPoints4 = 4;
		double[] xPoints4 = {0, 0.7, 0.7, 0};
		double[] yPoints4 = {0.05, 0.05, 0.2, 0.2};
		result.addRotatablePolygon(nPoints4, xPoints4, yPoints4, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(xLoc, yLoc, 0.15, GameState.PROJECTILE_RED);
		
		return result;
	}
}
