package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class EnemyKamikaze extends SimpleEnemy {

	public static final String ID = "kamikazeenemy";
	public static final String NAME = "Kamikazes";
	public static final String DESCRIPTION = "A numerous enemy willing to sacrifice itself to break your line!";
	
	public static final int WAVE_SIZE = 150;
	public static final double BASE_KILL_REWARD = 0.8;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 25;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 9;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0.1;
	public static final double MOVE_SPEED = 0.08;
	
	public static final boolean TRACKS_TARGET = true;
	public static final boolean NODE_ATTRACTION = true;
	public static final double FIRE_ON_THE_MOVE = 0;
	public static final double[][] MOVE_PRIORITIES = {{2.4, 3, 2.4},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyKamikaze(GameState gameState, Point2d loc, double tier, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
				  MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TRACKS_TARGET,
				  NODE_ATTRACTION, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	public EnemyKamikaze(double tier){
		this(null, null, tier, null, 0);
	}
	
	@Override
	public Enemy generateCopy(double tier) {
		return new EnemyKamikaze(tier);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.2, 0.3, -0.2};
		double[] yPoints1 = {-0.22, 0, 0.22};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.ENEMY_PURPLE);
		
		return result;
	}
	
	@Override
	protected void instantAttack(Entity target){
		super.instantAttack(target);
		die(); //suicide attacker
	}
	
	@Override
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(12, 0.2, 0.9, 0.8f, 0.4f,
                GameState.ENEMY_PROJECTILE_PURPLE);
	}
	
}
