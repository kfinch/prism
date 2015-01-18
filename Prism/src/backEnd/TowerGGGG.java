package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class TowerGGGG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G4_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER * 20; //TowerGGGG has bonus regen
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerGGGG(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerGGGG can't attack
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.85, GameState.TOWER_GREEN);
		result.addFixedCircle(0, 0, 0.55, GameState.TOWER_BASE);
		result.addFixedRectangle(-0.15, -0.90, 0.15, 0.90, GameState.TOWER_BASE);
		result.addFixedCircle(0, 0, 0.40, GameState.TOWER_GREEN);
		result.addFixedRectangle(-0.50, -0.10, 0.50, 0.10, GameState.TOWER_BASE);
		
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
}
