package backEnd;

import java.awt.Color;

import util.PaintableShapes;
import util.Point2d;

public class TowerConduit extends SimpleTower implements LightSource {
	
	public static String ID = "TowerConduit";
	public static String NAME = "Conduit Tower";
	public static String DESCRIPTION = "Projects light around it, allowing towers and other conduits to be built.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 1;
	public static final double MAX_HEALTH = 500;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double MIN_LIGHT_INCREASE = 0.003;
	public static final double LIGHT_INCREASE_RATE = 0.005;
	public static final double RADIANCE = 9.7;
	
	public static final double PERCENT_HEALTH_HARM = 0.10;
	
	public double currentLightRadius;
	
	public TowerConduit(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerConduit can't attack
		currentLightRadius = 0;
	}
	
	public static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.7, Color.lightGray);
		
		return result;
	}
	
	@Override
	public double harm(double damage, boolean isDirectAttack, Entity source){
		if(isDirectAttack){
			double modDamage = maxHealth.modifiedValue * PERCENT_HEALTH_HARM;
			return super.harm(modDamage, isDirectAttack, source);
		}
		else{
			return super.harm(damage, isDirectAttack, source);
		}
	}
	
	@Override
	public boolean teleportTower(Node dst){
		return false; //conduits cant be teleported
	}
	
	@Override
	public double lightRadius() {
		if(isGhost)
			return 0;
		else
			return currentLightRadius;
	}
	
	@Override
	public Point2d getLocation(){
		return loc;
	}
	
	@Override
	public void onSpawn(){
		super.onSpawn();
		gameState.lightSources.add(this);
	}
	
	@Override
	public void onDespawn(){
		super.onDespawn();
		gameState.lightSources.remove(this);
	}
	
	@Override
	public void die(){
		isActive = false; //conduits just die, don't become ghosts
	}
	
	@Override
	public void preStep(){
		super.preStep();
		if(passiveAction.canAct() && currentLightRadius != RADIANCE){
			double lightIncrease = (RADIANCE - currentLightRadius) * LIGHT_INCREASE_RATE;
			if(lightIncrease < MIN_LIGHT_INCREASE)
				lightIncrease = MIN_LIGHT_INCREASE;
			currentLightRadius += lightIncrease;
			if(currentLightRadius > RADIANCE)
				currentLightRadius = RADIANCE;
		}
	}
}
