package backEnd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.SimpleCircleAnimation;
import util.Vector2d;

public class TowerRRG extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 25;
	public static final double ATTACK_DELAY = 10;
	public static final double ATTACK_RANGE = 5;
	public static final double ATTACK_AOE = 3.5; //this is only used for the heal AoE, not the attack
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double LIFESTEAL_PERCENT = 0.1;
	
	public TowerRRG(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, ATTACK_AOE, false, false,
		      PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(xLoc, yLoc));
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
	protected void instantAttack(GameState gameState){
		super.instantAttack(gameState);
		double healing = attackDamage.modifiedValue * LIFESTEAL_PERCENT;
		Set<Tower> nearbyTowers = gameState.getTowersInCenterRange(xLoc, yLoc, attackAOE.modifiedValue);
		for(Tower t : nearbyTowers)
			t.heal(healing);
		Animation healingAnimation = new SimpleCircleAnimation(15, 0.6, 2.2, 0.4f, 0.2f, GameState.PROJECTILE_GREEN);
		healingAnimation.setLocation(xLoc, yLoc);
		gameState.playAnimation(healingAnimation);
	}
	
	private static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 7;
		double[] xPoints1 = {-0.4, -0.4, 0.2, 0.6, 0.2, -0.2, -0.2};
		double[] yPoints1 = {0.1, 0.6, 0.6, 0.2, 0.4, 0.4, 0.1};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_RED);
		
		int nPoints2 = 7;
		double[] xPoints2 = {-0.4, -0.4, 0.2, 0.6, 0.2, -0.2, -0.2};
		double[] yPoints2 = {-0.1, -0.6, -0.6, -0.2, -0.4, -0.4, -0.1};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_RED);
		
		int nPoints3 = 4;
		double[] xPoints3 = {-0.15, 0.25, 0.05, 0.25};
		double[] yPoints3 = {0, -0.25, 0, 0.25};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_GREEN);
		
		return result;
	}
	
	@Override
	protected Animation generateAttackAnimation(GameState gameState){
		return new VampAnimation(this, target);
	}
}

//TODO: a lot of custom code for probably no reason? generalize this
class VampAnimation extends Animation {

	static final Color COLOR = GameState.PROJECTILE_RED;
	static final int DURATION = 10;
	static final float STARTING_ALPHA = 0.7f;
	static final float ENDING_ALPHA = 0.1f;
	static final double WIDTH = 0.25;
	
	Vector2d atkVec;
	double width;
	
	Color color;
	int frameCount;
	float alpha, alphaStep;
	
	public VampAnimation(Entity src, Entity dst){
		this.xLoc = src.xLoc;
		this.yLoc = src.yLoc;
		
		this.atkVec = new Vector2d(dst.xLoc - src.xLoc, dst.yLoc - src.yLoc);
		
		this.color = COLOR;
		this.frameCount = 0;
		this.alpha = STARTING_ALPHA;
		this.alphaStep = (ENDING_ALPHA-STARTING_ALPHA)/DURATION;
	}
	
	@Override
	public void step() {
		alpha += alphaStep;
		
		frameCount++;
		if(frameCount >= DURATION)
			isActive = false;
	}

	@Override
	public void paintAnimationFromCenter(Graphics2D g2d, int centerX,
			int centerY, int tileSize) {
		float[] colorComps = new float[3];
		colorComps = color.getColorComponents(colorComps);
		g2d.setColor(new Color(colorComps[0],colorComps[1], colorComps[2],alpha));
		
		g2d.setStroke(new BasicStroke((float) WIDTH*tileSize));
		g2d.drawLine(centerX, centerY, (int)(centerX + atkVec.x*tileSize), (int)(centerY + atkVec.y*tileSize));
		g2d.setStroke(new BasicStroke(1));
	}
	
}