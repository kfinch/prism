package backEnd;

import java.util.Set;

import util.Point2d;
import util.PaintableShapes;

public class EnemyBigBoss extends SimpleEnemy {

	public static final int WAVE_SIZE = 1; //2
	public static final double BASE_KILL_REWARD = 60;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 1000;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 30;
	public static final double ATTACK_DELAY = 40;
	public static final double ATTACK_RANGE = 0.2;
	public static final double MOVE_SPEED = 0.06;
	
	public static final double TOWER_AFFINITY = 0;
	public static final boolean FIRE_ON_THE_MOVE = false;
	public static final double[][] MOVE_PRIORITIES = {{2.5, 3, 2.5},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyBigBoss(GameState gameState, Point2d loc, int tier, Node currNode, int spawnFrame) {
		super(gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
				MAX_HEALTH, HEALTH_REGEN,
				ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED,
				TOWER_AFFINITY, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	@Override
	public Enemy generateCopy(Point2d loc, Node currNode, int spawnFrame) {
		return new EnemyBigBoss(gameState, loc, tier, currNode, spawnFrame);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints = 9;
		double[] xPoints = {-0.4,-0.2,0.4,0.4,0.1,0.1,0.4,0.4,-0.2};
		double[] yPoints = {0,-0.4,-0.4,-0.2,-0.2,0.2,0.2,0.4,0.4};
		result.addRotatablePolygon(nPoints, xPoints, yPoints, GameState.ENEMY_PURPLE);
		
		result.addFixedCircle(0, 0, 0.40, GameState.ENEMY_PURPLE);
		
		return result;
	}
	
}
