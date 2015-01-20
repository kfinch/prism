package backEnd;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;
import util.Vector2d;

public class TowerRBBB extends SimpleTower{

	public static String ID = "TowerRBBB";
	public static String NAME = "Flamethrower Tower";
	public static String DESCRIPTION = "Projects a cone of flame, damaging all enemies caught by it.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G0_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 40;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 6;
	public static final double ATTACK_AOE = Math.PI * 0.5; //this is the cone's width
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public TowerRBBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION, 
		      gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, true, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(loc));
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0.00, 0.70, 0.30, 0.70};
		double[] yPoints1 = {0.00, 0.55, 0.00, -0.55};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 4;
		double[] xPoints2 = {-0.10, -0.10, 0.35, 0.35};
		double[] yPoints2 = {-0.05, -0.60, -0.60, -0.40};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		int nPoints3 = 4;
		double[] xPoints3 = {-0.10, -0.10, 0.35, 0.35};
		double[] yPoints3 = {0.05, 0.60, 0.60, 0.40};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_BLUE);
		
		int nPoints4 = 4;
		double[] xPoints4 = {-0.65, -0.20, -0.20, -0.65};
		double[] yPoints4 = {-0.20, -0.35, 0.35, 0.20};
		result.addRotatablePolygon(nPoints4, xPoints4, yPoints4, GameState.TOWER_BLUE);
		
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
	protected void instantAttack(){
		Set<Enemy> enemiesInRange = gameState.getEnemiesInRange(loc, attackRange.modifiedValue);
		double angleToTarget = new Vector2d(loc, target.loc).getAngle();
		double angleToE;
		for(Enemy e : enemiesInRange){
			angleToE = new Vector2d(loc, e.loc).getAngle();
			if(Math.abs(Vector2d.deltaAngle(angleToTarget, angleToE)) <= attackAOE.modifiedValue/2)
				e.harm(attackDamage.modifiedValue, true, this);
		}
	}
	
	//draw constant flamethrowing so long as tower is tracking an active target
	@Override
	protected void trackTarget(){
		super.trackTarget();
		
		if(gameState.frameNumber % 3 == 0){
			double attackWidth = Math.sin(attackAOE.modifiedValue / 2) * attackRange.modifiedValue;
			Vector2d maxAttackVec = new Vector2d(loc, target.loc);
			maxAttackVec = Vector2d.vectorFromAngleAndMagnitude(maxAttackVec.getAngle(),
				                                                attackRange.modifiedValue - attackWidth/2);
			gameState.playAnimation(new TowerRBBBAttackAnimation(loc, loc.afterTranslate(maxAttackVec), attackWidth));
		}
	}
}

class TowerRBBBAttackAnimation extends SimpleCircleAnimation {

	protected static final int DURATION = 10;
	
	protected static final double STARTING_WIDTH = 0.3;
	
	protected static final float STARTING_ALPHA = 0.4f;
	protected static final float ENDING_ALPHA = 0.2f;
	
	protected static final Color FLAME_COLOR = GameState.PROJECTILE_RED;
	
	public Vector2d moveVec;
	
	public TowerRBBBAttackAnimation(Point2d startLoc, Point2d endLoc, double endingSize) {
		super(DURATION, STARTING_WIDTH, endingSize, STARTING_ALPHA, ENDING_ALPHA, FLAME_COLOR);
		moveVec = new Vector2d(startLoc, endLoc).afterDivide(DURATION);
		loc = startLoc;
	}
	
	@Override
	public void step(){
		super.step();
		loc = loc.afterTranslate(moveVec);
	}
	
}