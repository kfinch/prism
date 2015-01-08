package backEnd;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import util.PaintableShapes;
import util.Point2d;

public class TowerConduit extends SimpleTower implements LightSource {
	
	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = 200;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double MIN_LIGHT_INCREASE = 0.003;
	public static final double LIGHT_INCREASE_RATE = 0.005;
	public static final double RADIANCE = 8.7;
	
	public double currentLightRadius;
	
	public TowerConduit(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(xLoc, yLoc));
		attackAction.startSuppress(); //TowerConduit can't attack
		currentLightRadius = 0;
	}
	
	@Override
	public double lightRadius() {
		return currentLightRadius;
	}
	
	@Override
	public Point2d getLocation(){
		return new Point2d(xLoc, yLoc);
	}
	
	@Override
	public void onSpawn(GameState gameState){
		super.onSpawn(gameState);
		gameState.lightSources.add(this);
	}
	
	@Override
	public void onDespawn(GameState gameState){
		super.onDespawn(gameState);
		gameState.lightSources.remove(this);
	}
	
	@Override
	public void preStep(GameState gameState){
		super.preStep(gameState);
		if(passiveAction.canAct() && currentLightRadius != RADIANCE){
			double lightIncrease = (RADIANCE - currentLightRadius) * LIGHT_INCREASE_RATE;
			if(lightIncrease < MIN_LIGHT_INCREASE)
				lightIncrease = MIN_LIGHT_INCREASE;
			currentLightRadius += lightIncrease;
			if(currentLightRadius > RADIANCE)
				currentLightRadius = RADIANCE;
		}
	}
	
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.7, Color.lightGray);
		
		return result;
	}
}
