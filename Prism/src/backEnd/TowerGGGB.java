package backEnd;

import java.awt.Color;
import java.awt.Graphics2D;

import util.AttachedAnimation;
import util.GeometryUtils;
import util.PaintableShapes;
import util.Point2d;
import util.Vector2d;

public class TowerGGGB extends SimpleTower implements AttractSource {
	
	public static String ID = "TowerGGGB";
	public static String NAME = "Honeydick Tower";
	public static String DESCRIPTION = "Upgrade to TowerGGB. " + 
			"Attracts nearby enemies, making them more likely to move towards this tower. " +
			"In addition, enemies harm themselves and are briefly paralyzed when attacking this tower.";
	
	public static final String TOWER_GGGB_DEBUFF_ID = "towergggbdebuff";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G3_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 150; //can't attack, but this is used for thorns strength
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	public static final double ATTACK_AOE = 0;
	
	public static final double ATTRACT_STRENGTH = 18; //TODO make more reasonable numbers after testing
	public static final double ATTRACT_FALLOFF = 1;
	
	public static final int PARALYZE_DEBUFF_DURATION = 20; //TODO make more reasonable numbers after testing
	
	public TowerGGGB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerGGGB cannot attack
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.85, GameState.TOWER_GREEN);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0.7, 0, -0.7};
		double[] yPoints1 = {0.9, 0, 0.9};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BASE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0.7, 0, -0.7};
		double[] yPoints2 = {-0.9, 0, -0.9};
		result.addFixedPolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BASE);
		
		int nPoints3 = 4;
		double[] xPoints3 = {0.5, 0, -0.5, 0};
		double[] yPoints3 = {0, 0.7, 0, -0.7};
		result.addFixedPolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_BLUE);
		
		result.addFixedCircle(0, 0, 0.30, GameState.TOWER_GREEN);
		
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
	
	@Override
	public double harm(double damage, boolean isDirectAttack, Entity source){
		double result = super.harm(damage, isDirectAttack, source);
		if(isDirectAttack && source != null && specialAction.canAct()){
			source.harm(attackDamage.modifiedValue, false, this);
			source.addBuff(new TowerGGGBBuff(gameState));
		}
		return result;
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

class TowerGGGBBuff extends TimedBuff {

	TowerGGGBBuffAnimation animation;
	
	public TowerGGGBBuff(GameState gameState) {
		super(gameState, TowerGGGB.TOWER_GGGB_DEBUFF_ID, "Paralyzed",
			  "This enemy cannot move or act", false, true, TowerGGGB.PARALYZE_DEBUFF_DURATION);
	}

	@Override
	public void apply(Entity e) {
		e.moveAction.startDisable();
		e.attackAction.startDisable();
		e.specialAction.startDisable();
		
		animation = new TowerGGGBBuffAnimation(e);
		gameState.playAnimation(animation);
	}

	@Override
	public void remove(Entity e) {
		e.moveAction.endDisable();
		e.attackAction.endDisable();
		e.specialAction.endDisable();
		
		animation.isActive = false;
	}
	
}

class TowerGGGBBuffAnimation extends AttachedAnimation {

	protected static final int ALPHA = 175;
	protected static final double MAX_DEVIATION = 0.10;
	
	PaintableShapes shapes;
	
	public TowerGGGBBuffAnimation(Entity anchor) {
		super(anchor, true);
		Color color = new Color(GameState.PROJECTILE_GREENBLUE.getRed(), GameState.PROJECTILE_GREENBLUE.getGreen(),
                GameState.PROJECTILE_GREENBLUE.getBlue(), ALPHA);
		shapes = new PaintableShapes(loc);
		shapes.addFixedRectangle(-0.30, -0.30, 0.30, 0.30, color);
	}

	@Override
	public void step(){
		super.step();
		double xDev = Math.random()*MAX_DEVIATION*2 - MAX_DEVIATION;
		double yDev = Math.random()*MAX_DEVIATION*2 - MAX_DEVIATION;
		loc = loc.afterTranslate(new Vector2d(xDev, yDev));
	}
	
	@Override
	public void paintAnimationFromCenter(Graphics2D g2d, int centerX,
			int centerY, int tileSize) {
		shapes.paintShapes(g2d, centerX, centerY, tileSize);
	}
	
}
