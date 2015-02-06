package backEnd;

import java.awt.Color;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleCircleAnimation;
import util.SimpleShapeAnimation;

public class TowerRGBB extends SimpleTower{

	public static final String ID = "TowerRGBB";
	public static final String NAME = "Azure Booster Tower";
	public static final String DESCRIPTION = "Upgrade to TowerRGB. " + 
			"Has no attack, but boosts adjacent towers, granting a large bonus to attack speed and health regen. " +
			"Also boosts affected tower's AoE";
	
	public static final String TOWER_RGBB_BUFF_ID = "towerrgbbbuff";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 0;
	public static final double ATTACK_DELAY = 1000;
	public static final double ATTACK_RANGE = 0;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	public static final double AOE_BUFF = 1.3; //TODO: too high?
	
	public TowerRGBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackAction.startSuppress(); //TowerRGBB can't attack
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
		
		result.addFixedCircle(0, 0, 0.32, GameState.TOWER_BLUE);
		
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
				Animation pingAnim = new SimpleCircleAnimation(15, 0.2, 2.5, 0.4f, 0.2f, GameState.PROJECTILE_BLUE);
				pingAnim.loc = loc;
				gameState.playAnimation(pingAnim);
				
				Set<Tower> towers = gameState.getTowersInEdgeRange(loc, TowerRGB.AURA_RANGE);
				for(Tower tower : towers){
					tower.addBuff(new TowerRGBBuff(gameState, tier,
		                       TowerRGB.T4_ATTACK_DELAY_BUFF, TowerRGB.T4_HEALTH_REGEN_BUFF));
					tower.addBuff(new TowerRGBBBuff(gameState));
					PaintableShapes flashShape = new PaintableShapes(tower.loc);
					flashShape.addFixedRectangle(-1, -1, 1, 1, Color.white);
					Animation flashAnim = new SimpleShapeAnimation(15, flashShape, 0.15f, 0f, GameState.PROJECTILE_BLUE);
					flashAnim.loc = tower.loc;
					gameState.playAnimation(flashAnim);
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

class TowerRGBBBuff extends TimedBuff {
	
	public TowerRGBBBuff(GameState gameState) {
		super(gameState, TowerRGBB.TOWER_RGBB_BUFF_ID, "Azure Boosted",
			  "This tower has bonus area of effect",
			  true, true, TowerRGB.BUFF_PERIOD*2);
	}

	@Override
	public void apply(Entity e) {
		super.apply(e);
		Tower t = (Tower) buffed;
		t.attackAOE.multBonuses.add(TowerRGBB.AOE_BUFF);
		t.attackAOE.update();
	}

	@Override
	public void remove() {
		Tower t = (Tower) buffed;
		t.attackAOE.multBonuses.remove(TowerRGBB.AOE_BUFF);
		t.attackAOE.update();
	}
	
}
