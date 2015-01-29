package backEnd;

import java.awt.Color;
import java.awt.Graphics2D;

import util.AttachedAnimation;
import util.PaintableShapes;
import util.Point2d;
import util.Vector2d;

public class TowerRRRG extends SimpleTower {
	
	public static String ID = "TowerRRRG";
	public static String NAME = "Sundering Tower";
	public static String DESCRIPTION =
			"Fires sundering spikes at enemies, increasing all subsequent damage they take.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 90;
	public static final double ATTACK_DELAY = 20;
	public static final double ATTACK_RANGE = 7;
	public static final double PROJECTILE_SPEED = 0.6;
	public static final double SHOT_ORIGIN_DISTANCE = 0.85;
	
	public static final int SUNDER_DURATION = 120;
	public static final double SUNDER_STACK_STRENGTH = 0.2;
	public static final int SUNDER_MAX_STACKS = 5;
	
	public TowerRRRG(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, true, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE,
		      true, true, generateShapes(loc));
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
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints2 = 3;
		double[] xPoints2 = {-0.3, 0.82, -0.4};
		double[] yPoints2 = {0.7, 0.35, 0.25};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 3;
		double[] xPoints3 = {-0.3, 0.82, -0.4};
		double[] yPoints3 = {-0.7, -0.35, -0.25};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		result.addFixedCircle(0, 0, 0.42, GameState.TOWER_GREEN);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0, 0.85, 0};
		double[] yPoints1 = {0.25, 0, -0.25};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected PaintableShapes generateProjectileShapes(Point2d loc) {
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.2, 0.45, -0.2};
		double[] yPoints1 = {0.2, 0, -0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.PROJECTILE_REDGREEN);
		
		return result;
	}
	
	@Override
	protected Buff generateAttackDebuff(){
		return new TowerRRRGDebuff(gameState);
	}
	
}

class TowerRRRGDebuff extends TimedBuff {

	public int stacks;
	
	public TowerRRRGDebuffAnimation animation;
	
	public TowerRRRGDebuff(GameState gameState) {
		super(gameState, "towerrrrgdebuff", "Sundered", 
			  "This enemy takes increased damage from all sources", false, true, TowerRRRG.SUNDER_DURATION);
		stacks = 1;
	}
	
	@Override
	public void handleDuplicate(Buff b){
		super.handleDuplicate(b);
		TowerRRRGDebuff rrrg = (TowerRRRGDebuff) b;
		if(rrrg.stacks < TowerRRRG.SUNDER_MAX_STACKS)
			rrrg.stacks++;
	}

	@Override
	public void apply(Entity e) {
		e.receivedDamageModifier.multBonuses.add(1 + TowerRRRG.SUNDER_STACK_STRENGTH * stacks);
		animation = new TowerRRRGDebuffAnimation(e, stacks);
		gameState.playAnimation(animation);
	}

	@Override
	public void remove(Entity e) {
		e.receivedDamageModifier.multBonuses.remove(1 + TowerRRRG.SUNDER_STACK_STRENGTH * stacks);
		animation.isActive = false;
	}
	
}

class TowerRRRGDebuffAnimation extends AttachedAnimation {

	protected static final int ALPHA_PER_STACK = 40;
	protected static final double MAX_DEVIATION = 0.10;
	
	PaintableShapes shapes;
	
	public TowerRRRGDebuffAnimation(Entity anchor, int stacks) {
		super(anchor, true);
		Color color = new Color(GameState.PROJECTILE_RED.getRed(), GameState.PROJECTILE_RED.getGreen(),
                                GameState.PROJECTILE_RED.getBlue(), ALPHA_PER_STACK * stacks);
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
