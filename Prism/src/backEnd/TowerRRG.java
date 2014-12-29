package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;

public class TowerRRG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = 750;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 150;
	public static final double ATTACK_DELAY = 50;
	public static final double ATTACK_RANGE = 0;
	public static final double ATTACK_AOE = 2.5;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerRRG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(xLoc, yLoc));
	}
	
	@Override
	protected Enemy acquireTarget(GameState gameState){
		attackRange.modifiedValue = attackAOE.modifiedValue;
		return super.acquireTarget(gameState);
	}
	
	@Override
	protected void instantAttack(GameState gameState){
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, attackAOE.modifiedValue);
		for(Enemy e : enemiesInBlast)
			e.harm(attackDamage.modifiedValue);
		gameState.playAnimation(generateInstantAttackAnimation(gameState));
	}
	
	//TODO: update
	private static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.4, 0, -0.4};
		double[] yPoints1 = {-0.4, 0, 0.4, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 4;
		double[] xPoints2 = {0.2, 0.4, 0.4, 0.2};
		double[] yPoints2 = {-0.2, -0.2, 0.2, 0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		return result;
	}
	
	@Override
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return null; //TODO: update
	}
}