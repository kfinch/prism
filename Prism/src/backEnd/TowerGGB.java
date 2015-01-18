package backEnd;

import util.GeometryUtils;
import util.PaintableShapes;
import util.Point2d;
import util.Vector2d;

public class TowerGGB extends SimpleTower implements AttractSource {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G2_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 60; //can't attack, but this is used for thorns strength
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final double ATTACK_AOE = 0;
	
	public static final double ATTRACT_STRENGTH = 13; //TODO make more reasonable numbers after testing
	public static final double ATTRACT_FALLOFF = 1;
	
	public TowerGGB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerGGB cannot attack
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.8, GameState.TOWER_GREEN);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0.7, 0, -0.7};
		double[] yPoints1 = {0.9, 0, 0.9};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BASE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0.7, 0, -0.7};
		double[] yPoints2 = {-0.9, 0, -0.9};
		result.addFixedPolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BASE);
		
		int nPoints3 = 4;
		double[] xPoints3 = {0.3, 0, -0.3, 0};
		double[] yPoints3 = {0, 0.6, 0, -0.6};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_BLUE);
		
		return result;
	}
	
	@Override
	protected Tower generateRedUpgrade(){
		return new TowerRGGB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Tower generateGreenUpgrade(){
		return new TowerGGGB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Tower generateBlueUpgrade(){
		return null;
	}
	
	@Override
	public void harm(double damage, boolean isDirectAttack, Entity source){
		super.harm(damage, isDirectAttack, source);
		if(isDirectAttack && source != null && specialAction.canAct())
			source.harm(attackDamage.modifiedValue, false, this);
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
	public Vector2d getAttractionVectorFromPoint(Point2d point) {
		if(!passiveAction.canAct() || !specialAction.canAct())
			return new Vector2d(0,0);
		double strength = ATTRACT_STRENGTH - GeometryUtils.dist(loc, point)*ATTRACT_FALLOFF;
		if(strength <= 0)
			return new Vector2d(0,0);
		
		Vector2d attractVector = new Vector2d(point, loc);
		return Vector2d.vectorFromAngleAndMagnitude(attractVector.getAngle(), strength);
	}


}
