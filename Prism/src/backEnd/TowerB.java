package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.SimpleCircleAnimation;

public class TowerB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = 100;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 20;
	public static final double ATTACK_DELAY = 100;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = 0.3;
	public static final double SHOT_ORIGIN_DISTANCE = 0.6;
	public static final double ATTACK_AOE = 0.4;
	
	public TowerB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 12;
		double[] xPoints1 = {-0.4, -0.2, -0.2, 0.2, 0.2, 0.6, 0.6, 0.2, 0.2, -0.2, -0.2, -0.4};
		double[] yPoints1 = {-0.2, -0.2, -0.4, -0.4, -0.2, -0.2, 0.2, 0.2, 0.4, 0.4, 0.2, 0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		return result;
	}
	
	@Override
	public String addRed(GameState gameState){
		TowerRB upgrade = new TowerRB(currNode, xLoc, yLoc, spawnFrame);
		upgradeToTower(gameState, upgrade);
		return null;
	}
	
	@Override
	public String addGreen(GameState gameState){
		TowerGB upgrade = new TowerGB(currNode, xLoc, yLoc, spawnFrame);
		upgradeToTower(gameState, upgrade);
		return null;
	}
	
	@Override
	public String addBlue(GameState gameState){
		TowerBB upgrade = new TowerBB(currNode, xLoc, yLoc, spawnFrame);
		upgradeToTower(gameState, upgrade);
		return null;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.2, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(10, 0.2, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
