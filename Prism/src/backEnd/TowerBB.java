package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerBB extends SimpleTower {
	
	public static final String ID = "TowerBB";
	public static final String NAME = "Rocket Tower II";
	public static final String DESCRIPTION = "Upgrade to TowerB. Shoots medium-long range AoE rockets.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = Tower.T2G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 60;
	public static final double ATTACK_DELAY = TowerB.ATTACK_DELAY;
	public static final double ATTACK_RANGE = 8;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.7;
	public static final double ATTACK_AOE = 0.8;
	
	public TowerBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRBB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerGBB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerBBB(gameState, loc, currNode, spawnFrame);
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
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
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 5;
		double[] xPoints1 = {-0.24, 0.1, 0.24, 0.1, -0.24};
		double[] yPoints1 = {-0.14, -0.14, 0, 0.14, 0.14};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(10, 0.24, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
