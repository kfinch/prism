package backEnd;

import util.PaintableShapes;

public class TowerR extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = Tower.T1G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 10;
	public static final double ATTACK_DELAY = 25;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = 0.4;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6;
	
	public TowerR(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, true, true, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.4, 0, -0.4};
		double[] yPoints1 = {-0.4, 0, 0.4, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 4;
		double[] xPoints2 = {0.2, 0.4, 0.4, 0.2};
		double[] yPoints2 = {-0.2, -0.2, 0.2, 0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		return result;
	}

	protected Tower generateRedUpgrade(){
		return new TowerRR(currNode, xLoc, yLoc, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerRG(currNode, xLoc, yLoc, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerRB(currNode, xLoc, yLoc, spawnFrame);
	}
	
	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.17, GameState.PROJECTILE_RED);
		
		return result;
	}

}
