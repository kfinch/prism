package backEnd;

import java.awt.Color;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;
import util.SimpleShapeAnimation;

public class TowerRGGB extends SimpleTower{

	public static final String ID = "TowerRGGB";
	public static final String NAME = "Emerald Booster Tower";
	public static final String DESCRIPTION = "Upgrade to TowerRGB. " + 
			"Has no attack, but boosts adjacent towers, granting a large bonus to attack speed and " +
			" a very large bonus to health regen. Also periodically cleanses harmful effects from adjacent towers.";
	
	public static final String TOWER_RGGB_BUFF_ID = "towerrggbbuff";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G2_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double HEALTH_REGEN_BUFF = Tower.BASE_HEALTH_REGEN * 8;
	
	public static final double CLEANSE_PERIOD = 80;
	
	public TowerRGGB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerRGGB can't attack
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0.00, 0.40, -0.40};
		double[] yPoints1 = {-0.95, -0.40, -0.40};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_GREEN);
		result.rotate(Math.PI * 2 / 3);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0.00, 0.40, -0.40};
		double[] yPoints2 = {-0.95, -0.40, -0.40};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		result.rotate(Math.PI * 2 / 3);
		
		int nPoints3 = 3;
		double[] xPoints3 = {0.00, 0.40, -0.40};
		double[] yPoints3 = {-0.95, -0.40, -0.40};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_RED);
		
		result.addFixedCircle(0, 0, 0.32, GameState.TOWER_GREEN);
		
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
			if((gameState.frameNumber - spawnFrame) % TowerRGB.BUFF_PERIOD == 1){ //only updates aura every several frames
				Animation pingAnim = new SimpleCircleAnimation(15, 0.2, 2.5, 0.4f, 0.2f, GameState.PROJECTILE_GREEN);
				pingAnim.loc = loc;
				gameState.playAnimation(pingAnim);
				
				Set<Tower> towers = gameState.getTowersInEdgeRange(loc, TowerRGB.AURA_RANGE);
				for(Tower tower : towers){
					tower.addBuff(new TowerRGBBuff(gameState, tier,
		                       TowerRGB.T4_ATTACK_DELAY_BUFF, TowerRGB.T4_HEALTH_REGEN_BUFF));
					tower.addBuff(new TowerRGGBBuff(gameState));
					PaintableShapes flashShape = new PaintableShapes(tower.loc);
					flashShape.addFixedRectangle(-1, -1, 1, 1, Color.white);
					Animation flashAnim = new SimpleShapeAnimation(15, flashShape, 0.15f, 0f, GameState.PROJECTILE_GREEN);
					flashAnim.loc = tower.loc;
					gameState.playAnimation(flashAnim);
				}
			}
			//now apply cleanse //TODO: make sure this is actually working as intended (test it)
			if((gameState.frameNumber - spawnFrame) % CLEANSE_PERIOD == 1){
				Set<Tower> towers = gameState.getTowersInEdgeRange(loc, TowerRGB.AURA_RANGE);
				for(Tower tower : towers){
					Buff buffToDispel = null;
					for(Buff b : tower.buffs.values()){
						if(!b.isBeneficial && b.isDispellable){
							System.out.println("Dispelled a buff: " + b.name); //TODO: remove debugging code
							buffToDispel = b;
							break;
						}
					}
					if(buffToDispel != null)
						tower.dispelBuff(buffToDispel);
				}
			}
		}
	}
	
	@Override
	public void moveStep(){
		if(passiveAction.canAct()){
			shapes.rotate(TowerRGB.COSMETIC_ROTATE_SPEED);
		}
	}
}

class TowerRGGBBuff extends TimedBuff {

	
	public TowerRGGBBuff(GameState gameState) {
		super(gameState, TowerRRGB.TOWER_RRGB_BUFF_ID, "Emerald Boosted",
			  "This tower has additional bonus health regeneration " +
		      "and will also periodically dispel negative effects on itself.",
			  true, true, TowerRGB.BUFF_PERIOD*2);
	}

	@Override
	public void apply(Entity e) {
		Tower t = (Tower) e;
		t.healthRegen.addBonuses.add(TowerRGGB.HEALTH_REGEN_BUFF);
		t.healthRegen.update();
	}

	@Override
	public void remove(Entity e) {
		Tower t = (Tower) e;
		t.healthRegen.addBonuses.remove(TowerRGGB.HEALTH_REGEN_BUFF);
		t.healthRegen.update();
	}
	
}
