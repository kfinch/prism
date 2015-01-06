package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.SimpleCircleAnimation;

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
		
		Animation a = generateAttackAnimation(gameState);
		if(a != null){
			a.setLocation(xLoc, yLoc);
			gameState.playAnimation(a);
		}
	}
	
	private static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.65, GameState.TOWER_GREEN);
		
		result.addFixedCircle(0, 0, 0.4, GameState.TOWER_BASE);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.3, 0, -0.3};
		double[] yPoints1 = {-0.3, 0, 0.3, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(20, attackAOE.modifiedValue*2, attackAOE.modifiedValue*2, 0.6f, 0.0f,
				                         GameState.PROJECTILE_REDGREEN);
	}
}
