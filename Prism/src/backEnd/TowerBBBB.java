package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerBBBB extends SimpleTower {
	
	public static final String ID = "TowerBBBB";
	public static final String NAME = "Nuke Tower";
	public static final String DESCRIPTION = "Upgrade to TowerBBB. Shoots very long range huge-AoE rockets.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 360;
	public static final double ATTACK_DELAY = TowerB.ATTACK_DELAY;
	public static final double ATTACK_RANGE = 12;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.88;
	public static final double ATTACK_AOE = 1.2;
	
	public TowerBBBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
		      gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		//int nPoints1 = 11;
		//double[] xPoints1 = {-0.65, -0.2, -0.2, 0.2, 0.2, -0.2, 0.2, 0.2, -0.2, -0.2, -0.65};
		//double[] yPoints1 = {0.3, 0.3, 0.50, 0.50, 0.25, 0.0, -0.25, -0.50, -0.50, -0.3, -0.3};
		//result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		//result.addRotatableRectangle(-0.9, -0.08, 0.9, 0.08, GameState.TOWER_BASE);
		
		int nPoints1 = 5;
		double[] xPoints1 = {0.40, 0.88, 0.88, 0.40, 0.00};
		double[] yPoints1 = {-0.28, -0.28, 0.28, 0.30, 0.00};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 4;
		double[] xPoints2 = {-0.10, -0.10, 0.35, 0.35};
		double[] yPoints2 = {-0.05, -0.60, -0.60, -0.40};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		int nPoints3 = 4;
		double[] xPoints3 = {-0.10, -0.10, 0.35, 0.35};
		double[] yPoints3 = {0.05, 0.60, 0.60, 0.40};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_BLUE);
		
		int nPoints4 = 4;
		double[] xPoints4 = {-0.65, -0.20, -0.20, -0.65};
		double[] yPoints4 = {-0.20, -0.35, 0.35, 0.20};
		result.addRotatablePolygon(nPoints4, xPoints4, yPoints4, GameState.TOWER_BLUE);
		
		return result;
	}
	
	@Override
	protected Tower generateRedUpgrade(){
		return null;
	}
	
	@Override
	protected Tower generateGreenUpgrade(){
		return null;
	}
	
	@Override
	protected Tower generateBlueUpgrade(){
		return null;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 5;
		double[] xPoints1 = {-0.32, 0.1, 0.32, 0.1, -0.32};
		double[] yPoints1 = {-0.19, -0.19, 0, 0.19, 0.19};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(10, 0.32, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
