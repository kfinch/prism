package backEnd;

import java.util.Set;

import util.PaintableShapes;

public class TowerGGB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = 1400;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 40;
	public static final double ATTACK_DELAY = 40;
	public static final double ATTACK_RANGE = 4;
	public static final double PROJECTILE_SPEED = 0.15;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6; //TODO: update
	public static final double ATTACK_AOE = 0.3;
	
	public static final int SLOW_DURATION = 60;
	public static final double SLOW_STRENGTH = 1.5;
	
	public TowerGGB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, true, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
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
	protected Buff generateAttackDebuff(){
		return new SlowingTowerDebuff(SLOW_DURATION, SLOW_STRENGTH);
	}
}
