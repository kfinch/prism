package backEnd;

import util.PaintableShapes;

public class TowerRB extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 200;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 80;
	public static final double ATTACK_DELAY = 65;
	public static final double ATTACK_RANGE = 8;
	public static final double PROJECTILE_SPEED = 0.7;
	public static final double SHOT_ORIGIN_DISTANCE = 0.75;
	
	public TowerRB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
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
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.5, 0, 0};
		double[] yPoints1 = {0, -0.5, 0.5};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0.15, 0.75, 0.15};
		double[] yPoints2 = {-0.2, 0, 0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.17, GameState.PROJECTILE_REDBLUE);
		
		return result;
	}

}
