package backEnd;

import java.util.Set;

import util.PaintableShapes;

public class EnemyBigBoss extends SimpleEnemy {

	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 1000;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 30;
	public static final double ATTACK_DELAY = 40;
	public static final double ATTACK_RANGE = 0.2;
	public static final double MOVE_SPEED = 0.08;
	
	public static final double TOWER_AFFINITY = 0;
	public static final boolean FIRE_ON_THE_MOVE = false;
	public static final double[][] MOVE_PRIORITIES = {{2.3, 3, 2.3},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyBigBoss(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN,
				ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TOWER_AFFINITY,
				FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = new PaintableShapes(yLoc, yLoc);
		
		int nPoints = 9;
		double[] xPoints = {-0.4,-0.2,0.4,0.4,0.1,0.1,0.4,0.4,-0.2};
		double[] yPoints = {0,-0.4,-0.4,-0.2,-0.2,0.2,0.2,0.4,0.4};
		result.addRotatablePolygon(nPoints, xPoints, yPoints, GameState.ENEMY_PURPLE);
		
		result.addFixedCircle(0, 0, 0.40, GameState.ENEMY_PURPLE);
		
		return result;
	}
	
}
