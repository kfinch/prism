package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class TowerRB extends SimpleTower{

	public static final String ID = "TowerRB";
	public static final String NAME = "Sniper Tower";
	public static final String DESCRIPTION = "Periodically snipes targets at long range for high damage";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = Tower.T2G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 80;
	public static final double ATTACK_DELAY = 65;
	public static final double ATTACK_RANGE = 7.5;
	public static final double PROJECTILE_SPEED = 0.7;
	public static final double SHOT_ORIGIN_DISTANCE = 0.75;
	
	public TowerRB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, true, true, generateShapes(loc));
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRRB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerRGB(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerRBB(gameState, loc, currNode, spawnFrame);
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.5, 0, 0};
		double[] yPoints1 = {0, -0.5, 0.5};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0.15, 0.75, 0.15};
		double[] yPoints2 = {-0.2, 0, 0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.17, GameState.PROJECTILE_REDBLUE);
		
		return result;
	}

}
