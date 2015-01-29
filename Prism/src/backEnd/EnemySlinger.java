package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;

public class EnemySlinger extends SimpleEnemy {

	public static final String ID = "slingerenemy";
	public static final String NAME = "Slingers";
	public static final String DESCRIPTION = "A balanced ranged foe.";
	
	public static final int WAVE_SIZE = 45;
	public static final double BASE_KILL_REWARD = 2;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 50;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 7;
	public static final double ATTACK_DELAY = 35;
	public static final double ATTACK_RANGE = 3.2;
	public static final double MOVE_SPEED = 0.06;
	
	public static final boolean TRACKS_TARGET = true;
	public static final boolean NODE_ATTRACTION = true;
	public static final double FIRE_ON_THE_MOVE = 0.5;
	public static final double[][] MOVE_PRIORITIES = {{2.7, 3, 2.7},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = true;
	public static final double PROJECTILE_SPEED = 0.3;
	public static final double SHOT_ORIGIN_DISTANCE = 0.3;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemySlinger(GameState gameState, Point2d loc, double tier, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
				  MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TRACKS_TARGET,
				  NODE_ATTRACTION, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	public EnemySlinger(double tier){
		this(null, null, tier, null, 0);
	}
	
	@Override
	public Enemy generateCopy(double tier) {
		return new EnemySlinger(tier);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.3, GameState.ENEMY_DRABGREEN);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0, 0.5, 0};
		double[] yPoints1 = {0.2, 0, -0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.ENEMY_DRABGREEN);
		
		return result;
	}
	
	@Override
	protected PaintableShapes generateProjectileShapes(Point2d projLoc){
		PaintableShapes result = new PaintableShapes(projLoc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0, 0.3, 0};
		double[] yPoints1 = {0.15, 0, -0.15};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.ENEMY_PROJECTILE_DRABGREEN);
		
		return result;
	}
	
}
