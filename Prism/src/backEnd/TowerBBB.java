package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.SimpleCircleAnimation;

public class TowerBBB extends SimpleTower {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = 400;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 160;
	public static final double ATTACK_DELAY = 80;
	public static final double ATTACK_RANGE = 10;
	public static final double PROJECTILE_SPEED = TowerB.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.8;
	public static final double ATTACK_AOE = 1.0;
	
	public TowerBBB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(xLoc, yLoc));
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
	
	//TODO: update
	private static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 11;
		double[] xPoints1 = {-0.55, -0.2, -0.2, 0.2, 0.2, -0.2, 0.2, 0.2, -0.2, -0.2, -0.55};
		double[] yPoints1 = {0.3, 0.3, 0.45, 0.45, 0.25, 0.0, -0.25, -0.45, -0.45, -0.3, -0.3};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		result.addRotatableRectangle(-0.9, -0.08, 0.9, 0.08, GameState.TOWER_BASE);
		
		int nPoints2 = 5;
		double[] xPoints2 = {0.4, 0.8, 0.8, 0.4, 0.0};
		double[] yPoints2 = {-0.25, -0.25, 0.25, 0.25, 0.0};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		return result;
	}

	@Override //TODO: update
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		int nPoints1 = 5;
		double[] xPoints1 = {-0.28, 0.1, 0.28, 0.1, -0.28};
		double[] yPoints1 = {-0.17, -0.17, 0, 0.17, 0.17};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_BLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(10, 0.28, attackAOE.modifiedValue*2, 0.6f, 0.3f, GameState.PROJECTILE_BLUE);
	}
}
