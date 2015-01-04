package backEnd;

import java.util.Set;

import util.PaintableShapes;

public class TowerRGB extends SimpleTower{

	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = 750;
	public static final double HEALTH_REGEN = MAX_HEALTH / 1000;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double AURA_RANGE = 3;
	
	public TowerRGB(Node currNode, double xLoc, double yLoc, int spawnFrame) {
		super(currNode, xLoc, yLoc, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(xLoc, yLoc));
		attackAction.startSuppress(); //TowerRGB can't attack
	}
	
	@Override
	public String addRed(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public String addGreen(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public String addBlue(GameState gameState){
		return Tower.CANT_UPGRADE_MAX_LEVEL;
	}
	
	@Override
	public void preStep(GameState gameState){
		super.preStep(gameState);
		if(specialAction.canAct() && passiveAction.canAct()){ //should aura be in both these categories?
			if(gameState.frameNumber - spawnFrame % 10 == 0){ //only updates aura every 10 frames
				Set<Tower> towers = gameState.getTowersInRange(xLoc, yLoc, AURA_RANGE);
				for(Tower tower : towers)
					tower.addBuff(null); //TODO: implement buff
			}
		}
	}
	
	//TODO: update shapes
	public static PaintableShapes generateShapes(double xLoc, double yLoc){
		PaintableShapes result = Tower.generateBaseShapes(xLoc, yLoc);
		
		result.addFixedCircle(0, 0, 0.5, GameState.TOWER_GREEN);
		
		return result;
	}
}
