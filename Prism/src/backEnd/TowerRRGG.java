package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;
import util.SimpleRayAnimation;

public class TowerRRGG extends SimpleTower{

	public static String ID = "TowerRRGG";
	public static String NAME = "Vamp Tower II";
	public static String DESCRIPTION = "Upgrade to TowerRRG. " + 
			"Drains targeted enemies, healing itself and nearby towers for a percentage of the damage done.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G2_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 50;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 5.5;
	public static final double ATTACK_AOE = 1.5; //this is only used for the heal AoE, not the attack
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double LIFESTEAL_PERCENT = 0.15;
	
	public TowerRRGG(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, 
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, false, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 7;
		double[] xPoints1 = {-0.5, -0.5, 0.2, 0.75, 0.2, -0.2, -0.2};
		double[] yPoints1 = {0.25, 0.65, 0.65, 0.2, 0.4, 0.4, 0.25};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 7;
		double[] xPoints2 = {-0.5, -0.5, 0.2, 0.75, 0.2, -0.2, -0.2};
		double[] yPoints2 = {-0.25, -0.65, -0.65, -0.2, -0.4, -0.4, -0.25};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 4;
		double[] xPoints3 = {0.10, 0.55, 0.30, 0.55};
		double[] yPoints3 = {0, -0.30, 0, 0.30};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_GREEN);
		
		result.addRotatableCircle(-0.25, 0, 0.25, GameState.TOWER_GREEN);
		
		return result;
	}
	
	protected Tower generateRedUpgrade(){
		return null;
	}
	
	protected Tower generateGreenUpgrade(){
		return null;
	}
	
	protected Tower generateBlueUpgrade(){
		return null;
	}
	
	@Override
	protected void instantAttack(){
		super.instantAttack();
		double healing = attackDamage.modifiedValue * LIFESTEAL_PERCENT;
		Set<Tower> nearbyTowers = gameState.getTowersInEdgeRange(loc, attackAOE.modifiedValue);
		for(Tower t : nearbyTowers)
			t.heal(healing, true, this);
		Animation healingAnimation = new SimpleCircleAnimation(15, 0.6, 2.2, 0.5f, 0.3f, GameState.PROJECTILE_GREEN);
		healingAnimation.loc = loc;
		gameState.playAnimation(healingAnimation);
	}
	
	@Override
	protected Animation generateAttackAnimation(){
		return new SimpleRayAnimation(10, loc, target.loc, 0.30, 0.7f, 0.1f, GameState.PROJECTILE_RED);
	}
}