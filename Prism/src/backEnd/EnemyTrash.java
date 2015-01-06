package backEnd;

import util.PaintableShapes;

public class EnemyTrash extends SimpleEnemy {

	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 150;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 50;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0.1;
	public static final double MOVE_SPEED = 0.1;
	
	public static final double TOWER_AFFINITY = 0;
	public static final double BRAVERY = 0;
	public static final boolean FIRE_ON_THE_MOVE = true;
	public static final double[][] MOVE_PRIORITIES = {{2.3, 3, 2.3},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyTrash(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN,
				ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TOWER_AFFINITY,
				BRAVERY, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(xLoc, yLoc));
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
