package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class TowerRRR extends SimpleTower{

	public static String ID = "TowerRRR";
	public static String NAME = "Pew Pew Pew Tower";
	public static String DESCRIPTION = "Upgrade to TowerRR. Very rapidly attacks targets at medium range.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 35;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 5.5;
	public static final double PROJECTILE_SPEED = TowerR.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.85;
	
	public TowerRRR(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, 
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, true, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.55, 0, -0.55};
		double[] yPoints1 = {-0.55, 0, 0.55, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 4;
		double[] xPoints2 = {0.25, 0.85, 0.85, 0.55};
		double[] yPoints2 = {0.4, 0.4, 0.1, 0.1};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 4;
		double[] xPoints3 = {0.25, 0.85, 0.85, 0.55};
		double[] yPoints3 = {-0.4, -0.4, -0.1, -0.1};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		return result;
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRRRR(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerRRRG(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerRRRB(gameState, loc, currNode, spawnFrame);
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.19, GameState.PROJECTILE_RED);
		
		return result;
	}

}
