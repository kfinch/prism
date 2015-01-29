package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;

public class EnemyAstral extends SimpleEnemy {

	public static final String ID = "astralenemy";
	public static final String NAME = "Astrals";
	public static final String DESCRIPTION = "A quick melee foe that is immune to everything but damage.";
	
	public static final int WAVE_SIZE = 40;
	public static final double BASE_KILL_REWARD = 2.2;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 55;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 8;
	public static final double ATTACK_DELAY = 40;
	public static final double ATTACK_RANGE = 0.1;
	public static final double MOVE_SPEED = 0.09;
	
	public static final boolean TRACKS_TARGET = true;
	public static final boolean NODE_ATTRACTION = false;
	public static final double FIRE_ON_THE_MOVE = 0.1;
	public static final double[][] MOVE_PRIORITIES = {{2.5, 3, 2.5},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyAstral(GameState gameState, Point2d loc, double tier, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
			  MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TRACKS_TARGET,
			  NODE_ATTRACTION, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
			  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	public EnemyAstral(double tier){
		this(null, null, tier, null, 0);
	}
	
	@Override
	public Enemy generateCopy(double tier) {
		return new EnemyAstral(tier);
	}
	
	@Override
	public void addBuff(Buff b){
		if(!b.isBeneficial && b.isDispellable){
			//resisted debuff!
		}
		else{
			super.addBuff(b);
		}
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0.25, 0, -0.5, 0};
		double[] yPoints1 = {0, -0.35, 0, 0.35};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.ENEMY_SKYBLUE);
		
		return result;
	}
	
	@Override
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return null; //TODO: make "spike" animation
	}
	
}
