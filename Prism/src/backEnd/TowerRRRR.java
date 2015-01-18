package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class TowerRRRR extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 55;
	public static final double ATTACK_DELAY = 7;
	public static final double ATTACK_RANGE = 6;
	public static final double PROJECTILE_SPEED = TowerR.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.88;
	
	public TowerRRRR(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, true, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.62, 0, -0.62};
		double[] yPoints1 = {-0.62, 0, 0.62, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 4;
		double[] xPoints2 = {0.25, 0.88, 0.88, 0.55};
		double[] yPoints2 = {0.5, 0.5, 0.2, 0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 4;
		double[] xPoints3 = {0.25, 0.88, 0.88, 0.55};
		double[] yPoints3 = {-0.5, -0.5, -0.2, -0.2};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		int nPoints4 = 5;
		double[] xPoints4 = {0.55, 0.88, 0.88, 0.55, 0.70};
		double[] yPoints4 = {0.15, 0.15, -0.15, -0.15, 0};
		result.addRotatablePolygon(nPoints4, xPoints4, yPoints4, GameState.TOWER_RED);
		
		return result;
	}
	
	//TODO: update
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
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.20, GameState.PROJECTILE_RED);
		
		return result;
	}

}
