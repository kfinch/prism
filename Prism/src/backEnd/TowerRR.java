package backEnd;

import util.PaintableShapes;

public class TowerRR extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 200;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 25;
	public static final double ATTACK_DELAY = 15;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = TowerR.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6; //TODO: update
	
	public TowerRR(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, true, true, generateShapes(xLoc, yLoc));
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
		
		//TODO: update shapes
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.5, 0, -0.5};
		double[] yPoints1 = {-0.5, 0, 0.5, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 5;
		double[] xPoints2 = {0.5, 0.85, 0.85, 0.5, 0.7};
		double[] yPoints2 = {-0.2, -0.2, 0.2, 0.2, 0.0};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.19, GameState.PROJECTILE_RED);
		
		return result;
	}

}
