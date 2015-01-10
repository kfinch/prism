package backEnd;

import util.PaintableShapes;

public class EnemyTrash extends SimpleEnemy {

	public static final int WAVE_SIZE = 50;
	public static final double BASE_KILL_REWARD = 1;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 50;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 30;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0.2;
	public static final double MOVE_SPEED = 0.08;
	
	public static final double TOWER_AFFINITY = 0;
	public static final boolean FIRE_ON_THE_MOVE = true;
	public static final double[][] MOVE_PRIORITIES = {{2.3, 3, 2.3},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyTrash(int tier, Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN,
			  ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TOWER_AFFINITY,
			  FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
			  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(xLoc, yLoc));
	}
	
	@Override
	public Enemy generateCopy(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		return new EnemyTrash(tier, currNode, xLoc, yLoc, spawnFrame);
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = new PaintableShapes(yLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.3, GameState.ENEMY_DRAB_GREEN);
		
		return result;
	}

	@Override
	protected void instantAttack(GameState gameState, Entity target){
		super.instantAttack(gameState, target);
		isActive = false; //suicide attacker
	}
	
}
