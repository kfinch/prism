package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class TowerRR extends SimpleTower{

	public static String ID = "TowerRR";
	public static String NAME = "Pew Pew Tower";
	public static String DESCRIPTION = "Upgrade to TowerR. Rapidly attacks targets at medium range.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 2;
	public static final double MAX_HEALTH = Tower.T2G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 25;
	public static final double ATTACK_DELAY = 15;
	public static final double ATTACK_RANGE = 5;
	public static final double PROJECTILE_SPEED = TowerR.PROJECTILE_SPEED;
	public static final double SHOT_ORIGIN_DISTANCE = 0.85;
	
	public TowerRR(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, 
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, true, true, generateShapes(loc));
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRRR(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerRRG(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerRRB(gameState, loc, currNode, spawnFrame);
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		//TODO: update shapes
		int nPoints1 = 4;
		double[] xPoints1 = {0, 0.5, 0, -0.5};
		double[] yPoints1 = {-0.5, 0, 0.5, 0};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 5;
		double[] xPoints2 = {0.5, 0.85, 0.85, 0.5, 0.7};
		double[] yPoints2 = {-0.2, -0.2, 0.2, 0.2, 0.0};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.18, GameState.PROJECTILE_RED);
		
		return result;
	}

}
