package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerGB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = Tower.T2G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 15;
	public static final double ATTACK_DELAY = 25;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.75;
	public static final double ATTACK_AOE = 0.3;
	
	public static final int SLOW_DURATION = 60;
	public static final double SLOW_STRENGTH = 1.4;
	
	public TowerGB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, true, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRGB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerGGB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerGBB(gameState, loc, currNode, spawnFrame);
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addRotatableRectangle(0.3, -0.3, 0.75, 0.3, GameState.TOWER_BLUE);
		result.addFixedCircle(0, 0, 0.5, GameState.TOWER_GREEN);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 8;
		double[] xPoints1 = {0, 0.07, 0.22, 0.07, 0, -0.07, -0.22, -0.07};
		double[] yPoints1 = {-0.22, -0.07, 0, 0.07, 0.22, 0.07, 0, -0.07};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_GREENBLUE);
		
		return result;
	}
	
	@Override
	protected Buff generateAttackDebuff(){
		return new SlowingTowerDebuff(gameState, SLOW_DURATION, SLOW_STRENGTH);
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(10, 0.2, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_GREENBLUE);
	}
}
