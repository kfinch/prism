package backEnd;

import java.util.HashSet;
import java.util.Set;

import util.GeometryUtils;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleRayAnimation;
import util.Vector2d;

public class TowerRRB extends SimpleTower implements AttractSource {

	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 16;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 6.5;
	public static final double PROJECTILE_SPEED = 0.4;
	public static final double SHOT_ORIGIN_DISTANCE = 0.4;
	
	public static final double REPULSE_STRENGTH = 15; //TODO make more reasonable numbers after testing
	public static final double REPULSE_FALLOFF = 1;
	
	public static final int SHOT_BOUNCES = 2;
	public static final double BOUNCE_RANGE = 2.5;
	
	public TowerRRB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(xLoc, yLoc));
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
	public void onSpawn(GameState gameState){
		gameState.attractSources.add(this);
	}
	
	@Override
	public void onDespawn(GameState gameState){
		gameState.attractSources.remove(this);
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 6;
		double[] xPoints1 = {0.8, 0.2, 0.2, 0.5, 0.2, 0.2};
		double[] yPoints1 = {0, 0.6, 0.3, 0, -0.3, -0.6};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 6;
		double[] xPoints2 = {-0.8, -0.2, -0.2, -0.5, -0.2, -0.2};
		double[] yPoints2 = {0, 0.6, 0.3, 0, -0.3, -0.6};
		result.addFixedPolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 4;
		double[] xPoints3 = {0.4, -0.2, -0.1, -0.2};
		double[] yPoints3 = {0, -0.25, 0, 0.25};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_BLUE);
		
		return result;
	}
	
	@Override
	protected void instantAttack(GameState gameState){
		Set<Entity> hitTargets = new HashSet<Entity>();
		Entity src = this;
		Entity currTarget = target;
		double currDamage = attackDamage.modifiedValue;
		for(int i=0; i<=SHOT_BOUNCES; i++){
			gameState.playAnimation(new SimpleRayAnimation(5, new Point2d(src.xLoc, src.yLoc),
					                                       new Point2d(currTarget.xLoc, currTarget.yLoc),
					                                       0.22, 0.8f, 0.2f, GameState.PROJECTILE_REDBLUE));
			currTarget.harm(currDamage, this);
			currDamage /= 2;
			
			hitTargets.add(currTarget);
			Set<Enemy> targetsInBounceRange = gameState.getEnemiesInRange(currTarget.xLoc, currTarget.yLoc, BOUNCE_RANGE);
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
		double strength = REPULSE_STRENGTH - GeometryUtils.dist(xLoc, yLoc, point.x, point.y)*REPULSE_FALLOFF;
		if(strength <= 0)
			return new Vector2d(0,0);
		
		Vector2d repulseVector = new Vector2d(point.x - xLoc, point.y - yLoc); //reversed so repels instead of attracts
		repulseVector.setMagnitude(strength);
		return repulseVector;
	}

}
