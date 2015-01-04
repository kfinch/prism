package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;

public class TowerRG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 400;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 50;
	public static final double ATTACK_DELAY = 50;
	public static final double ATTACK_RANGE = 0;
	public static final double ATTACK_AOE = 2;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerRG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(xLoc, yLoc));
	}
	
	@Override
	public String addRed(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public String addGreen(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public String addBlue(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
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
