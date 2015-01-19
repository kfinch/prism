package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class TowerG extends SimpleTower{

	public static String ID = "TowerG";
	public static String NAME = "Bunker Tower";
	public static String DESCRIPTION = "Tough tower with strong health regen but no attack.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = Tower.T1G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER * 10; //TowerG has bonus regen
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerG(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerG can't attack
	}
	
	protected Tower generateRedUpgrade(){
		return new TowerRG(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateGreenUpgrade(){
		return new TowerGG(gameState, loc, currNode, spawnFrame);
	}
	
	protected Tower generateBlueUpgrade(){
		return new TowerGB(gameState, loc, currNode, spawnFrame);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.5, GameState.TOWER_GREEN);
		
		return result;
	}
}
