package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerB extends SimpleTower {
	
	public static final String ID = "TowerB";
	public static final String NAME = "Rocket Tower";
	public static final String DESCRIPTION = "Shoots medium range AoE rockets.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = Tower.T1G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 20;
	public static final double ATTACK_DELAY = 70;
	public static final double ATTACK_RANGE = 6;
	public static final double PROJECTILE_SPEED = 0.3;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6;
	public static final double ATTACK_AOE = 0.4;
	
	public TowerB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 12;
		double[] xPoints1 = {-0.4, -0.2, -0.2, 0.2, 0.2, 0.6, 0.6, 0.2, 0.2, -0.2, -0.2, -0.4};
		double[] yPoints1 = {-0.2, -0.2, -0.4, -0.4, -0.2, -0.2, 0.2, 0.2, 0.4, 0.4, 0.2, 0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		return result;
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerGB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerBB(gameState, loc, currNode, spawnFrame);
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 5;
		double[] xPoints1 = {-0.2, 0.12, 0.2, 0.12, -0.2};
		double[] yPoints1 = {-0.12, -0.12, 0, 0.12, 0.12};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(10, 0.2, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
