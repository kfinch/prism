package backEnd;

import java.awt.Color;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;
import util.SimpleShapeAnimation;

public class TowerRGB extends SimpleTower{
	
	public static final String ID = "TowerRGB";
	public static final String NAME = "Booster Tower";
	public static final String DESCRIPTION =
			"Has no attack, but boosts adjacent towers, granting them bonus attack speed and health regen.";
	
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
	
	public static final double AURA_RANGE = 1.5;
	
	public static final double T3_ATTACK_DELAY_BUFF = 1.3; //TODO: is this too high?
	public static final double T3_HEALTH_REGEN_BUFF = Tower.BASE_HEALTH_REGEN * 4; // ^ ^ ^ 
	
	public static final double T4_ATTACK_DELAY_BUFF = 1.5;
	public static final double T4_HEALTH_REGEN_BUFF = Tower.BASE_HEALTH_REGEN * 8;
	
	public static final int BUFF_PERIOD = 60;
	
	public static final double COSMETIC_ROTATE_SPEED = 0.015;
	
	public TowerRGB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
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
		return new TowerRRGB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Tower generateGreenUpgrade(){
		return new TowerRGGB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	protected Tower generateBlueUpgrade(){
		return new TowerRGBB(gameState, loc, currNode, spawnFrame);
	}
	
	@Override
	public void preStep(){
		super.preStep();
		if(specialAction.canAct() && passiveAction.canAct()){ //should aura be in both these categories?
			if((gameState.frameNumber - spawnFrame) % BUFF_PERIOD == 1){ //only updates aura every several frames
				Animation pingAnim = new SimpleCircleAnimation(15, 0.2, 2.5, 0.4f, 0.2f, Color.lightGray);
				pingAnim.loc = loc;
				gameState.playAnimation(pingAnim);
				
				Set<Tower> towers = gameState.getTowersInEdgeRange(loc, AURA_RANGE);
				for(Tower tower : towers){
					tower.addBuff(new TowerRGBBuff(gameState, tier,
							                       T3_ATTACK_DELAY_BUFF, T3_HEALTH_REGEN_BUFF));
					PaintableShapes flashShape = new PaintableShapes(tower.loc);
					flashShape.addFixedRectangle(-1, -1, 1, 1, Color.white);
					Animation flashAnim = new SimpleShapeAnimation(15, flashShape, 0.15f, 0f, Color.lightGray);
					flashAnim.loc = tower.loc;
					gameState.playAnimation(flashAnim);
				}
			}
		}
	}
	
	@Override
	public void moveStep(){
		if(passiveAction.canAct()){
			shapes.rotate(COSMETIC_ROTATE_SPEED);
		}
	}
}

class TowerRGBBuff extends TimedBuff {

	public int tier;
	public double attackDelayBuff;
	public double healthRegenBuff;
	
	public TowerRGBBuff(GameState gameState, int tier, double attackDelayBuff, double healthRegenBuff) {
		super(gameState, TowerRGB.TOWER_RGB_BUFF_ID, "Boosted",
			  "This tower has bonus attack speed and health regeneration",
			  true, true, TowerRGB.BUFF_PERIOD*2);
		this.tier = tier;
		this.attackDelayBuff = attackDelayBuff;
		this.healthRegenBuff = healthRegenBuff;
	}

	@Override
	public void handleDuplicate(Buff b) {
		TowerRGBBuff rgb = (TowerRGBBuff) b;
		if(rgb.tier > tier){
			rgb.timer = timer;
			rgb.tier = tier;
			rgb.attackDelayBuff = attackDelayBuff;
			rgb.healthRegenBuff = healthRegenBuff;
		}
	}

	@Override
	public void apply(Entity e) {
		Tower t = (Tower) e;
		t.attackDelay.multBonuses.add(attackDelayBuff);
		t.attackDelay.update();
		t.healthRegen.addBonuses.add(healthRegenBuff);
		t.healthRegen.update();
	}

	@Override
	public void remove(Entity e) {
		Tower t = (Tower) e;
		t.attackDelay.multBonuses.remove(attackDelayBuff);
		t.attackDelay.update();
		t.healthRegen.addBonuses.remove(healthRegenBuff);
		t.healthRegen.update();
	}

}
