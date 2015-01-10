package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.SimpleCircleAnimation;

public class TowerGBB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 40;
	public static final double ATTACK_DELAY = 30;
	public static final double ATTACK_RANGE = 7;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.85;
	public static final double ATTACK_AOE = 0.4;
	
	public static final int SLOW_DURATION = 60;
	public static final double SLOW_STRENGTH = 1.8;
	
	public TowerGBB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, true, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
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
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addRotatableRectangle(0.3, -0.32, 0.85, 0.32, GameState.TOWER_BLUE);
		
		result.addFixedCircle(0, 0, 0.6, GameState.TOWER_GREEN);
		
		int nPoints1 = 8;
		double[] xPoints1 = {0, 0.1, 0.3, 0.1, 0, -0.1, -0.3, -0.1};
		double[] yPoints1 = {-0.3, -0.1, 0, 0.1, 0.3, 0.1, 0, -0.1};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		int nPoints1 = 8;
		double[] xPoints1 = {0, 0.09, 0.25, 0.09, 0, -0.09, -0.25, -0.09};
		double[] yPoints1 = {-0.25, -0.09, 0, 0.09, 0.25, 0.09, 0, -0.09};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_GREENBLUE);
		
		return result;
	}
	
	@Override
	protected Buff generateAttackDebuff(){
		return new SlowingTowerDebuff(SLOW_DURATION, SLOW_STRENGTH);
	}
	
	@Override
	protected Animation generateAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(10, 0.2, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_GREENBLUE);
	}
}
