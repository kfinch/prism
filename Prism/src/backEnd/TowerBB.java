package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.SimpleCircleAnimation;

public class TowerBB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 200;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 60;
	public static final double ATTACK_DELAY = 100;
	public static final double ATTACK_RANGE = 8;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.7;
	public static final double ATTACK_AOE = 0.6;
	
	public TowerBB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
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
	
	private static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 11;
		double[] xPoints1 = {-0.5, -0.2, -0.2, 0.2, 0.2, -0.2, 0.2, 0.2, -0.2, -0.2, -0.5};
		double[] yPoints1 = {0.2, 0.2, 0.4, 0.4, 0.2, 0.0, -0.2, -0.4, -0.4, -0.2, -0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 5;
		double[] xPoints2 = {0.4, 0.7, 0.7, 0.4, 0.0};
		double[] yPoints2 = {-0.2, -0.2, 0.2, 0.2, 0.0};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.22, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(10, 0.22, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
