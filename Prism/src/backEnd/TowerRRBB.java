package backEnd;

import java.util.Set;

import util.Animation;
import util.GeometryUtils;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleRayAnimation;
import util.Vector2d;

public class TowerRRBB extends SimpleTower{

	public static final String ID = "TowerRRBB";
	public static final String NAME = "Rail Tower II";
	public static final String DESCRIPTION = "Upgrade to TowerRBB" +
			"Periodically rails targets, doing very high damage at very long range to all enemies in a line";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 380;
	public static final double ATTACK_DELAY = TowerRBB.ATTACK_DELAY;
	public static final double ATTACK_RANGE = 12;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double RAIL_WIDTH = 0.3; //rail width not counted as an AOE (so can't be modified by buffs)
	
	public TowerRRBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.8, 0.3, 0.3};
		double[] yPoints1 = {-0.35, -0.7, -0.35};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {-0.8, 0.3, 0.3};
		double[] yPoints2 = {0.35, 0.7, 0.35};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		int nPoints3 = 3;
		double[] xPoints3 = {-0.05, 0.90, -0.05};
		double[] yPoints3 = {-0.20, 0, 0.20};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		int nPoints4 = 4;
		double[] xPoints4 = {-0.50, -0.15, -0.15, -0.50};
		double[] yPoints4 = {-0.20, -0.20, 0.20, 0.20};
		result.addRotatablePolygon(nPoints4, xPoints4, yPoints4, GameState.TOWER_RED);
		
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
	protected void instantAttack(){
		Vector2d attackVec = new Vector2d(loc, target.loc);
		attackVec = Vector2d.vectorFromAngleAndMagnitude(attackVec.getAngle(), attackRange.modifiedValue);
		Point2d end = loc.afterTranslate(attackVec);
		
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(loc, attackRange.modifiedValue);
		for(Enemy e : enemiesInBlast){
			if(GeometryUtils.distPointToLine(e.loc, loc, end) <= RAIL_WIDTH)
				e.harm(attackDamage.modifiedValue, true, this);
		}
		
		//could be width RAIL_WIDTH*2, but drawing smaller than actual AoE,
		//because actual AoE checks only vs center of enemy
		Animation attackAnim = new SimpleRayAnimation(7, loc, end, RAIL_WIDTH, 0.8f, 0.3f, GameState.PROJECTILE_BLUE);
		attackAnim.loc = loc;
		gameState.playAnimation(attackAnim);
	}
	
}