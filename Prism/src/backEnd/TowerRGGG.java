package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class TowerRGGG extends SimpleTower{

	public static String ID = "TowerRGGG";
	public static String NAME = "Thumper Tower III";
	public static String DESCRIPTION = "Upgrade to TowerRGG. " +
			"Tough tower that periodically thumps the ground, damaging nearby enemies.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G3_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 200;
	public static final double ATTACK_DELAY = TowerRG.ATTACK_DELAY;
	public static final double ATTACK_RANGE = 0;
	public static final double ATTACK_AOE = 2.6;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerRGGG(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, 
		      gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.85, GameState.TOWER_GREEN);
		
		result.addFixedCircle(0, 0, 0.55, GameState.TOWER_BASE);
		
		result.addRotatableRectangle(-0.15, -0.9, 0.15, 0, GameState.TOWER_BASE);
		result.rotate(Math.PI*2/3);
		
		result.addRotatableRectangle(-0.15, -0.9, 0.15, 0, GameState.TOWER_BASE);
		result.rotate(Math.PI*2/3);
		
		result.addRotatableRectangle(-0.15, -0.9, 0.15, 0, GameState.TOWER_BASE);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.45, 0, -0.45};
		double[] yPoints1 = {-0.45, 0, 0.45, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		return result;
	}
	
	@Override
	protected Tower generateRedUpgrade(){
		return null;
	}
	
	@Override
	protected Tower generateGreenUpgrade(){
		return null;
	}
	
	@Override
	protected Tower generateBlueUpgrade(){
		return null;
	}
	
	@Override
	protected Entity acquireTarget(){
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
			a.loc = loc;
			gameState.playAnimation(a);
		}
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleCircleAnimation(20, attackAOE.modifiedValue*2, attackAOE.modifiedValue*2, 0.6f, 0.0f,
				                         GameState.PROJECTILE_REDGREEN);
	}
}
