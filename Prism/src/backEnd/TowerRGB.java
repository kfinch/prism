package backEnd;

import java.util.Set;

import util.PaintableShapes;
import util.Point2d;

public class TowerRGB extends SimpleTower{

	public static final String TOWER_RGB_BUFF_ID = "towerrgbbuff";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 3;
	public static final double MAX_HEALTH = Tower.T3G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double AURA_RANGE = 2.5;
	public static final double ATTACK_DELAY_BUFF_STRENGTH = 1.3; //TODO: is this too high?
	public static final double HEALTH_REGEN_BUFF_STRENGTH = Tower.BASE_HEALTH_REGEN * 4; // ^ ^ ^ 
	public static final int BUFF_PERIOD = 10;
	
	public TowerRGB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerRGB can't attack
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0, 0.35, -0.35};
		double[] yPoints1 = {-0.8, -0.2, -0.2};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_GREEN);
		result.rotate(Math.PI * 2 / 3);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0, 0.35, -0.35};
		double[] yPoints2 = {-0.8, -0.2, -0.2};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		result.rotate(Math.PI * 2 / 3);
		
		int nPoints3 = 3;
		double[] xPoints3 = {0, 0.35, -0.35};
		double[] yPoints3 = {-0.8, -0.2, -0.2};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
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
	public void preStep(){
		super.preStep();
		if(specialAction.canAct() && passiveAction.canAct()){ //should aura be in both these categories?
			if((gameState.frameNumber - spawnFrame) % BUFF_PERIOD == 1){ //only updates aura every several frames
				Set<Tower> towers = gameState.getTowersInEdgeRange(loc, AURA_RANGE);
				for(Tower tower : towers)
					tower.addBuff(new TowerRGBBuff(gameState));
			}
		}
	}
}

class TowerRGBBuff extends TimedBuff {

	public TowerRGBBuff(GameState gameState) {
		super(gameState, TowerRGB.TOWER_RGB_BUFF_ID, "Boosted",
			  "This tower has bonus attack speed and health regeneration",
			  true, true, TowerRGB.BUFF_PERIOD*2);
	}

	@Override
	public void handleDuplicate(Buff b) {
		//no overwrite TODO: may need to change this wrt other buffing towers?
	}

	@Override
	public void apply(Entity e) {
		Tower t = (Tower) e;
		t.attackDelay.multBonuses.add(TowerRGB.ATTACK_DELAY_BUFF_STRENGTH);
		t.attackDelay.update();
		t.healthRegen.addBonuses.add(TowerRGB.HEALTH_REGEN_BUFF_STRENGTH);
		t.healthRegen.update();
	}

	@Override
	public void remove(Entity e) {
		Tower t = (Tower) e;
		t.attackDelay.multBonuses.remove(TowerRGB.ATTACK_DELAY_BUFF_STRENGTH);
		t.attackDelay.update();
		t.healthRegen.addBonuses.remove(TowerRGB.HEALTH_REGEN_BUFF_STRENGTH);
		t.healthRegen.update();
	}

}
