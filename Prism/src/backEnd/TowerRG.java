package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerRG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = Tower.T2G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 50;
	public static final double ATTACK_DELAY = 50;
	public static final double ATTACK_RANGE = 0;
	public static final double ATTACK_AOE = 2;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerRG(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.65, GameState.TOWER_GREEN);
		
		result.addFixedCircle(0, 0, 0.4, GameState.TOWER_BASE);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.3, 0, -0.3};
		double[] yPoints1 = {-0.3, 0, 0.3, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		return result;
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRRG(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerRGG(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerRGB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Enemy acquireTarget(){
		attackRange.modifiedValue = attackAOE.modifiedValue;
		return super.acquireTarget();
	}
	
	@Override
	protected void instantAttack(){
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(loc, attackAOE.modifiedValue);
		for(Enemy e : enemiesInBlast)
			e.harm(attackDamage.modifiedValue, true, this);
		
		Animation a = generateAttackAnimation();
		if(a != null){
			a.setLocation(loc);
			gameState.playAnimation(a);
		}
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(20, attackAOE.modifiedValue*2, attackAOE.modifiedValue*2, 0.6f, 0.0f,
				                         GameState.PROJECTILE_REDGREEN);
	}
}
