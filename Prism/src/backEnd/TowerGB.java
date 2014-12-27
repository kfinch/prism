package backEnd;

import java.awt.Color;
import java.util.Set;

import util.PaintableShapes;

public class TowerGB extends SimpleAOETower {
	
	public static final Color SLOWING_PROJECTILE_COLOR = Color.decode("#00cccc");
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 450;
	public static final double HEALTH_REGEN = 1;
	public static final double ATTACK_DAMAGE = 20;
	public static final double ATTACK_DELAY = 40;
	public static final double ATTACK_RANGE = 4;
	public static final double PROJECTILE_SPEED = 0.15;
	public static final double SHOT_ORIGIN_DISTANCE = 0.7;
	
	public static final double SHOT_AOE = 0.2;
	
	public static final double MULT_SLOW_STRENGTH = 1.5;
	public static final int SLOW_DURATION = 50;
	
	public TowerGB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, PROJECTILE_SPEED, SHOT_AOE, SHOT_ORIGIN_DISTANCE, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.5, GameState.TOWER_GREEN);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0.2, 0.7, 0.7, 0.2};
		double[] yPoints1 = {-0.2, -0.2, 0.2, 0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		int nPoints1 = 8;
		double[] xPoints1 = {0, 5, 20, 5, 0, -5, -20, -5};
		double[] yPoints1 = {-20, -5, 0, 5, 20, 5, 0, -5};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, TowerGB.SLOWING_PROJECTILE_COLOR);
		
		return result;
	}
	
	@Override
	protected Projectile generateProjectile(GameState gameState, double xLoc, double yLoc){
		return new ProjectileGB(xLoc, yLoc, target, projectileSpeed,
                attackDamage.modifiedValue, attackAOE.modifiedValue, generateProjectileShapes(xLoc, yLoc));
	}
}

class ProjectileGB extends SimpleAOEProjectile {

	public ProjectileGB(double xLoc, double yLoc, Entity target,
			double moveSpeed, double damage, double aoe, PaintableShapes shapes) {
		super(xLoc, yLoc, target, moveSpeed, damage, aoe, shapes);
	}
	
	@Override
	public void moveStep(GameState gs){
		super.moveStep(gs);
		shapes.rotate(0.5);
	}
	
	@Override
	public void payload(GameState gameState){
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, aoe);
		for(Enemy e : enemiesInBlast){
			e.hurt(damage);
			e.addBuff(new SlowingDebuffGB());
		}
	}
	
}

class SlowingDebuffGB extends TimedBuff {

	Double penalty;
	
	public SlowingDebuffGB() {
		super("tower-slow", "GB Tower Slow", "This enemy is slowed", false, TowerGB.SLOW_DURATION);
		penalty = new Double(TowerGB.MULT_SLOW_STRENGTH);
	}

	@Override
	public void apply(Entity e) {
		super.step(e);
		Enemy en = (Enemy) e;
		en.moveSpeed.multPenalties.add(penalty);
		en.moveSpeed.update();
	}

	@Override
	public void step(Entity e) {
		super.step(e);
	}

	@Override
	public void remove(Entity e) {
		super.step(e);
		Enemy en = (Enemy) e;
		en.moveSpeed.multPenalties.remove(penalty);
		en.moveSpeed.update();
	}
	
}
