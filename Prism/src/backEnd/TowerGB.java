package backEnd;

import java.util.Set;

import util.PaintableShapes;

//TODO: REDO WITH USE OF MORE (NYI) HELPERS
public class TowerGB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = 400;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 20;
	public static final double ATTACK_DELAY = 40;
	public static final double ATTACK_RANGE = 4;
	public static final double PROJECTILE_SPEED = 0.15;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6; //TODO: update
	public static final double ATTACK_AOE = 0.2;
	
	public static final double SLOW_STRENGTH = 1.3;
	
	public TowerGB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
	}
	
	//TODO: update
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 12;
		double[] xPoints1 = {-0.4, -0.2, -0.2, 0.2, 0.2, 0.6, 0.6, 0.2, 0.2, -0.2, -0.2, -0.4};
		double[] yPoints1 = {-0.2, -0.2, -0.4, -0.4, -0.2, -0.2, 0.2, 0.2, 0.4, 0.4, 0.2, 0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override //TODO: update
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(xLoc, yLoc, 0.2, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Projectile generateAOEProjectile(GameState gameState, double xLoc, double yLoc){
		return new TowerGBProjectile(xLoc, yLoc, target, projectileSpeed, attackDamage.modifiedValue,
				                       attackAOE.modifiedValue, generateProjectileShapes(xLoc, yLoc));
	}
}

class TowerGBProjectile extends SimpleAOEProjectile{

	public TowerGBProjectile(double xLoc, double yLoc, Entity target,
			double moveSpeed, double damage, double aoe, PaintableShapes shapes) {
		super(xLoc, yLoc, target, moveSpeed, damage, aoe, shapes);
	}

	@Override
	public void payload(GameState gameState){
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, aoe);
		for(Enemy e : enemiesInBlast){
			e.harm(damage);
		}
	}
}

//TODO: make its own class, to be used by all the slowing towers
class TowerGBSlowingDebuff extends TimedBuff {

	
}
