package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerGGBB extends SimpleTower {
	
	public static String ID = "TowerGGBB";
	public static String NAME = "Crippling Tower";
	public static String DESCRIPTION = "Upgrade to TowerGBB. " + 
			"Attacks with frost, crippling the movement and attack speeds of afflicted enemies in a large area.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G2_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 80;
	public static final double ATTACK_DELAY = 25;
	public static final double ATTACK_RANGE = 8;
	public static final double PROJECTILE_SPEED = TowerGB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.85; //TODO: update
	public static final double ATTACK_AOE = 1.0;
	
	public static final int SLOW_DURATION = 60;
	public static final double SLOW_STRENGTH = 2.2;
	
	public TowerGGBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
		      gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, true, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addRotatableRectangle(0.3, -0.35, 0.90, 0.35, GameState.TOWER_BLUE);
		
		result.addFixedCircle(0, 0, 0.70, GameState.TOWER_GREEN);
		
		result.addFixedCircle(0, 0, 0.55, GameState.TOWER_BASE);
		
		result.addFixedCircle(0, 0, 0.45, GameState.TOWER_GREEN);
		
		int nPoints1 = 8;
		double[] xPoints1 = {0, 0.11, 0.32, 0.11, 0, -0.11, -0.32, -0.11};
		double[] yPoints1 = {-0.32, -0.11, 0, 0.11, 0.32, 0.11, 0, -0.11};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
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
		
		int nPoints1 = 8;
		double[] xPoints1 = {0, 0.11, 0.28, 0.11, 0, -0.11, -0.28, -0.11};
		double[] yPoints1 = {-0.28, -0.11, 0, 0.11, 0.28, 0.11, 0, -0.11};
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
