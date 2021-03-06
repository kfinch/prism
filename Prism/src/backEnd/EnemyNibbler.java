package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;

public class EnemyNibbler extends SimpleEnemy {

	public static final String ID = "nibblerenemy";
	public static final String NAME = "Nibblers";
	public static final String DESCRIPTION = "A balanced melee foe.";
	
	public static final int WAVE_SIZE = 45; //45
	public static final double BASE_KILL_REWARD = 2;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 65;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 8;
	public static final double ATTACK_DELAY = 35;
	public static final double ATTACK_RANGE = 0.1;
	public static final double MOVE_SPEED = 0.07;
	
	public static final boolean TRACKS_TARGET = true;
	public static final boolean NODE_ATTRACTION = true;
	public static final double FIRE_ON_THE_MOVE = 0.1;
	public static final double[][] MOVE_PRIORITIES = {{2.5, 3, 2.5},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyNibbler(GameState gameState, Point2d loc, double tier, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
				  MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TRACKS_TARGET,
				  NODE_ATTRACTION, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	public EnemyNibbler(double tier){
		this(null, null, tier, null, 0);
	}
	
	@Override
	public Enemy generateCopy(double tier) {
		return new EnemyNibbler(tier);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.35, GameState.ENEMY_ORANGE);
		
		return result;
	}
	
	@Override
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return null; //TODO: make "spike" animation
	}
	
}
