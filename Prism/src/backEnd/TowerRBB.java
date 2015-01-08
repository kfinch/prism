package backEnd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Set;

import util.Animation;
import util.GeometryUtils;
import util.PaintableShapes;
import util.Vector2d;

public class TowerRBB extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = 400;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 180;
	public static final double ATTACK_DELAY = 80;
	public static final double ATTACK_RANGE = 10;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double RAIL_WIDTH = 0.2; //rail width not counted as an AOE (so can't be modified by buffs)
	
	public TowerRBB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
		      ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, true, generateShapes(xLoc, yLoc));
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
	
	//TODO: update
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {-0.6, 0.2, 0.2};
		double[] yPoints1 = {-0.2, -0.6, -0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		
		int nPoints2 = 3;
		double[] xPoints2 = {-0.6, 0.2, 0.2};
		double[] yPoints2 = {0.2, 0.6, 0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		
		int nPoints3 = 3;
		double[] xPoints3 = {-0.2, 0.8, -0.2};
		double[] yPoints3 = {-0.18, 0, 0.18};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		return result;
	}

	@Override
	protected void instantAttack(GameState gameState){
		Vector2d attackVec = new Vector2d(target.xLoc-xLoc, target.yLoc-yLoc);
		attackVec.setMagnitude(attackRange.modifiedValue);
		double endX = xLoc + attackVec.x;
		double endY = yLoc + attackVec.y;
		
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, attackRange.modifiedValue);
		for(Enemy e : enemiesInBlast){
			if(GeometryUtils.distPointToLine(e.xLoc, e.yLoc, xLoc, yLoc, endX, endY) <= RAIL_WIDTH)
				e.harm(attackDamage.modifiedValue);
		}
		
		//could be width RAIL_WIDTH*2, but drawing smaller than actual AoE, because actual AoE checks only vs center of enemy
		gameState.playAnimation(new RailAnimation(this, attackVec, RAIL_WIDTH));
	}
	
}

//TODO: a lot of custom code for probably no reason? generalize this
class RailAnimation extends Animation {

	static Color COLOR = GameState.PROJECTILE_REDBLUE;
	static int DURATION = 7;
	static float STARTING_ALPHA = 0.8f;
	static float ENDING_ALPHA = 0.3f;
	
	Vector2d railVec;
	double width;
	
	Color color;
	int frameCount;
	float alpha, alphaStep;
	
	public RailAnimation(Entity src, Vector2d railVec, double width){
		this.xLoc = src.xLoc;
		this.yLoc = src.yLoc;
		
		this.railVec = railVec;
		this.width = width;
		
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
		
		g2d.setStroke(new BasicStroke((float) width*tileSize));
		g2d.drawLine(centerX, centerY, (int)(centerX + railVec.x*tileSize), (int)(centerY + railVec.y*tileSize));
		g2d.setStroke(new BasicStroke(1));
	}
	
}
