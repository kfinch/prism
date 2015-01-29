package backEnd;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;

public class EnemyBomber extends SimpleEnemy {

	public static final String ID = "bomberenemy";
	public static final String NAME = "Bombers";
	public static final String DESCRIPTION = "A flying enemy that rains destruction beneath it.";
	
	public static final int WAVE_SIZE = 25;
	public static final double BASE_KILL_REWARD = 4;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 120;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 4;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 0.7;
	public static final double MOVE_SPEED = 0.05;
	
	public static final boolean TRACKS_TARGET = false;
	public static final boolean NODE_ATTRACTION = false;
	public static final double FIRE_ON_THE_MOVE = 1;
	public static final double[][] MOVE_PRIORITIES = {{2, 3, 2},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = false;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final boolean APPLIES_DEBUFF = false;
	
	public EnemyBomber(GameState gameState, Point2d loc, double tier, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
				  MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TRACKS_TARGET,
				  NODE_ATTRACTION, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	public EnemyBomber(double tier){
		this(null, null, tier, null, 0);
	}
	
	@Override
	public Enemy generateCopy(double tier) {
		return new EnemyBomber(tier);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		result.addFixedCircle(0, 0, 0.25, GameState.ENEMY_ORANGE);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.4, 0.6, -0.4};
		double[] yPoints1 = {-0.25, 0, 0.25};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.ENEMY_ORANGE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {-0.15, 0.15, -0.4};
		double[] yPoints2 = {0, 0, -0.5};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.ENEMY_ORANGE);
		
		int nPoints3 = 3;
		double[] xPoints3 = {-0.15, 0.15, -0.4};
		double[] yPoints3 = {0, 0, 0.5};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.ENEMY_ORANGE);
		
		return result;
	}
	
	@Override
	protected void chooseNextNode(){ //always just goes forward, no collision, this is how it 'flies'
		if(loc.x != 0)
			nextNode = gameState.nodeAt(currNode.xLoc-1, currNode.yLoc);
	}
	
	@Override
	protected void instantAttack(Entity target){
		for(Entity e : gameState.getTowersAndPrismInRange(loc, attackRange.modifiedValue))
			e.harm(attackDamage.modifiedValue, true, this);
		
		Animation a = generateInstantAttackAnimation(gameState);
		a.loc = loc;
		gameState.playAnimation(a);
	}
	
	@Override
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return new SimpleCircleAnimation(12, 0.2, attackRange.modifiedValue*2, 0.8f, 0.4f,
				                         GameState.ENEMY_PROJECTILE_ORANGERED);
	}
	
}
