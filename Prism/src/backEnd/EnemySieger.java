package backEnd;

import java.util.Set;

import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;
import util.Vector2d;

public class EnemySieger extends SimpleEnemy {

	public static final String ID = "siegerenemy";
	public static final String NAME = "Siegers";
	public static final String DESCRIPTION = "Sturdy but few, these foes rain fire from afar.";
	
	public static final int WAVE_SIZE = 12;
	public static final double BASE_KILL_REWARD = 8;
	
	public static final double PRIORITY = 0;
	public static final double MAX_HEALTH = 130;
	public static final double HEALTH_REGEN = 0;
	public static final double ATTACK_DAMAGE = 4;
	public static final double ATTACK_DELAY = 15;
	public static final double ATTACK_RANGE = 5.5;
	public static final double MOVE_SPEED = 0.05;
	
	public static final boolean TRACKS_TARGET = false;
	public static final boolean NODE_ATTRACTION = true;
	public static final double FIRE_ON_THE_MOVE = 0.5;
	public static final double[][] MOVE_PRIORITIES = {{2.5, 3, 2.5},
		                                              {1, 0, 1},
		                                              {0, 0, 0}};
	public static final boolean USES_PROJECTILE = true;
	public static final double PROJECTILE_SPEED = 0.25;
	public static final double SHOT_ORIGIN_DISTANCE = 0.25;
	public static final boolean APPLIES_DEBUFF = false;
	
	public static final double ATTACK_AOE = 0.7;
	public static final double ATTACK_INACCURACY = 1.5;
	
	public EnemySieger(GameState gameState, Point2d loc, double tier, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, gameState, loc, tier, WAVE_SIZE, BASE_KILL_REWARD, currNode, PRIORITY, spawnFrame,
				  MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY, ATTACK_RANGE, MOVE_SPEED, TRACKS_TARGET,
				  NODE_ATTRACTION, FIRE_ON_THE_MOVE, MOVE_PRIORITIES, USES_PROJECTILE,
				  PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, APPLIES_DEBUFF, generateShapes(loc));
	}
	
	public EnemySieger(double tier){
		this(null, null, tier, null, 0);
	}
	
	@Override
	public Enemy generateCopy(double tier) {
		return new EnemySieger(tier);
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 6;
		double[] xPoints1 = {0.45, -0.35, -0.35, 0.45, 0.20, 0.20};
		double[] yPoints1 = {0.45, 0.45, -0.45, -0.45, -0.35, 0.35};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.ENEMY_PURPLE);
		
		result.addRotatableCircle(0.25, 0.00, 0.20, GameState.ENEMY_PURPLE);
		
		return result;
	}
	
	@Override
	protected Projectile generateProjectile(Point2d projLoc){
		Projectile result = new SiegerInaccurateProjectile(gameState, loc, this, target, projectileSpeed,
				                    attackDamage.modifiedValue, ATTACK_AOE, true, generateAttackDebuff(),
				                    ATTACK_INACCURACY, generateProjectileShapes(projLoc));
		((SimpleProjectile)result).playedAnimation = new SimpleCircleAnimation(12, 0.2, ATTACK_AOE*2, 0.8f, 0.4f,
                                                                               GameState.ENEMY_PROJECTILE_ORANGERED);
		return result;
	}
	
	@Override
	protected PaintableShapes generateProjectileShapes(Point2d projLoc){
		PaintableShapes result = new PaintableShapes(projLoc);
		
		result.addFixedCircle(0, 0, 0.15, GameState.ENEMY_PROJECTILE_ORANGERED);
		
		return result;
	}
	
}

class SiegerInaccurateProjectile extends SimpleProjectile {

	public Point2d modTargetLoc;
	
	public SiegerInaccurateProjectile(GameState gameState, Point2d loc,
			Entity source, Entity target, double moveSpeed, double damage,
			double aoe, boolean isAOE, Buff appliedDebuff, double inaccuracy,
			PaintableShapes shapes) {
		super(gameState, loc, source, target, moveSpeed, damage, aoe, isAOE,
				appliedDebuff, shapes);
		
		double randAngle = Math.random() * 2 * Math.PI;
		double randMagnitude = Math.random() * inaccuracy;
		Vector2d inaccuracyVector = Vector2d.vectorFromAngleAndMagnitude(randAngle, randMagnitude);
		this.modTargetLoc = target.loc.afterTranslate(inaccuracyVector);
	}
	
	@Override
	protected Vector2d vectorToTarget(){
		return new Vector2d(loc, modTargetLoc);
	}
	
	//TODO: this is sloppy, maybe add baseline projectile support for ground targeting?
	//also projectile here is specialized vs enemies, wtf
	@Override
	protected void payload(){
		Set<Tower> enemiesInBlast = gameState.getTowersInEdgeRange(modTargetLoc, aoe);
		for(Tower t : enemiesInBlast)
			t.harm(damage, true, source);
		
		if(playedAnimation != null){
			playedAnimation.loc = modTargetLoc;
			gameState.playAnimation(playedAnimation);
		}
	}
	
}
