package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerBBB extends SimpleTower {
	
	public static final String ID = "TowerBBB";
	public static final String NAME = "Rocket Tower III";
	public static final String DESCRIPTION = "Upgrade to TowerBB. Shoots long range big-AoE rockets.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 160;
	public static final double ATTACK_DELAY = TowerB.ATTACK_DELAY;
	public static final double ATTACK_RANGE = 10;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.8;
	public static final double ATTACK_AOE = 1.0;
	
	public TowerBBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 11;
		double[] xPoints1 = {-0.55, -0.2, -0.2, 0.2, 0.2, -0.2, 0.2, 0.2, -0.2, -0.2, -0.55};
		double[] yPoints1 = {0.3, 0.3, 0.45, 0.45, 0.25, 0.0, -0.25, -0.45, -0.45, -0.3, -0.3};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		result.addRotatableRectangle(-0.9, -0.08, 0.9, 0.08, GameState.TOWER_BASE);
		
		int nPoints2 = 5;
		double[] xPoints2 = {0.4, 0.8, 0.8, 0.4, 0.0};
		double[] yPoints2 = {-0.25, -0.25, 0.25, 0.25, 0.0};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		return result;
	}
	
	@Override
	protected Tower generateRedUpgrade(){
		return new TowerRBBB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Tower generateGreenUpgrade(){
		return new TowerGBBB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Tower generateBlueUpgrade(){
		return new TowerBBBB(gameState, loc, currNode, spawnFrame);
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 5;
		double[] xPoints1 = {-0.28, 0.1, 0.28, 0.1, -0.28};
		double[] yPoints1 = {-0.17, -0.17, 0, 0.17, 0.17};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(10, 0.28, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
