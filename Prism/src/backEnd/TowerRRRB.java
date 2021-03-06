package backEnd;

import java.util.HashSet;
import java.util.Set;

import util.GeometryUtils;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleRayAnimation;
import util.Vector2d;

public class TowerRRRB extends SimpleTower implements AttractSource {

	public static final String ID = "TowerRRRB";
	public static final String NAME = "Tesla Tower II";
	public static final String DESCRIPTION = "Upgrade to TowerRRB. " +
			"Repels nearby enemies, making them less likely to move towards this tower. " +
			"Also shoots chain lightning at targets, bouncing up to 3 times to nearby enemies";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 32;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 8;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0.4; //TODO: update
	
	public static final double REPULSE_STRENGTH = 18; //TODO make more reasonable numbers after testing
	public static final double REPULSE_FALLOFF = 1;
	
	public static final int SHOT_BOUNCES = 3;
	public static final double BOUNCE_RANGE = 3;
	
	public TowerRRRB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 6;
		double[] xPoints1 = {0.9, 0.25, 0.25, 0.55, 0.25, 0.25};
		double[] yPoints1 = {0, 0.8, 0.4, 0, -0.4, -0.8};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 6;
		double[] xPoints2 = {-0.9, -0.25, -0.25, -0.55, -0.25, -0.25};
		double[] yPoints2 = {0, 0.8, 0.4, 0, -0.4, -0.8};
		result.addFixedPolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints4 = 4;
		double[] xPoints4 = {0.5, -0.2, -0.1, -0.2};
		double[] yPoints4 = {0, -0.32, 0, 0.32};
		result.addRotatablePolygon(nPoints4, xPoints4, yPoints4, GameState.TOWER_BLUE);
		
		int nPoints3 = 3;
		double[] xPoints3 = {-0.35, 0.15, 0.15};
		double[] yPoints3 = {0, 0.12, -0.12};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		return result;
	}
	
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
	public void onSpawn(){
		gameState.attractSources.add(this);
	}
	
	@Override
	public void onDespawn(){
		gameState.attractSources.remove(this);
	}
	
	@Override
	protected void instantAttack(){
		Set<Entity> hitTargets = new HashSet<Entity>();
		Entity src = this;
		Entity currTarget = target;
		double currDamage = attackDamage.modifiedValue;
		for(int i=0; i<=SHOT_BOUNCES; i++){
			gameState.playAnimation(new SimpleRayAnimation(5, src.loc, currTarget.loc, 0.22, 0.8f, 0.2f,
					                                       GameState.PROJECTILE_REDBLUE));
			currTarget.harm(currDamage, true, this);
			currDamage /= 2;
			
			hitTargets.add(currTarget);
			Set<Enemy> targetsInBounceRange = gameState.getEnemiesInRange(currTarget.loc, BOUNCE_RANGE);
			boolean foundTarget = false;
			for(Enemy nextTarget : targetsInBounceRange){
				if(hitTargets.contains(nextTarget))
					continue;
				src = currTarget;
				currTarget = nextTarget;
				foundTarget = true;
				break;
			}
			if(!foundTarget)
				break;
		}
	}

	@Override
	public Vector2d getAttractionVectorFromPoint(Point2d point) {
		if(!passiveAction.canAct() || !specialAction.canAct())
			return new Vector2d(0,0);
		double strength = REPULSE_STRENGTH - GeometryUtils.dist(loc, point)*REPULSE_FALLOFF;
		if(strength <= 0)
			return new Vector2d(0,0);
		
		Vector2d repulseVector = new Vector2d(loc, point); //reversed so repels instead of attracts
		return Vector2d.vectorFromAngleAndMagnitude(repulseVector.getAngle(), strength);
	}

}
