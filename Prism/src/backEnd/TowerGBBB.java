package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.SimpleRayAnimation;
import util.Vector2d;

public class TowerGBBB extends SimpleTower{
	
	public static String ID = "TowerGBBB";
	public static String NAME = "Conducting Tower";
	public static String DESCRIPTION =
			"Fires electrical arcs that can shoot down incoming projectiles, or at enemies in a pinch. " +
			"Also conducts health between itself and adjacent towers in an effort to equalize health percentages. " +
			"Finally, tower teleports to or from spots adjacent to this tower proceed at triple speed.";
	
	public static final double PRIORITY = 0;
	public static final int TIER = 4;
	public static final double MAX_HEALTH = Tower.T4G1_HEALTH;
	public static final double HEALTH_REGEN = Tower.BASE_HEALTH_REGEN * TIER;
	public static final double ATTACK_DAMAGE = 24;
	public static final double ATTACK_DELAY = 12;
	public static final double ATTACK_RANGE = 6;
	public static final double PROJECTILE_SPEED = 0;
	public static final double SHOT_ORIGIN_DISTANCE = 0;
	
	//radius at which this tower looks for towers to transfer with
	public static final double HEALTH_TRANSFER_RADIUS = 1.5;
	//rate (per tick) that health is transferred
	public static final double HEALTH_TRANSFER_RATE = Tower.BASE_HEALTH_REGEN * 50; //TODO: TWEAK THIS (PLAYTEST)
	//percent health below which health will not be transferred from
	public static final double HEALTH_TRANSFER_LOWER_CUTOFF = 0.10;
	//percent health difference required before transfer begins
	public static final double HEALTH_TRANSFER_DIFFERENCE_CUTOFF = 0.02;
	
	private static final Vector2d ATK_SRC_1 = new Vector2d(0.6, 0);
	private static final Vector2d ATK_SRC_2 = ATK_SRC_1.afterSetAngle(Math.PI*2/3);
	private static final Vector2d ATK_SRC_3 = ATK_SRC_1.afterSetAngle(Math.PI*4/3);
	public static final Vector2d[] ATK_SRC_OFFSETS = {ATK_SRC_1, ATK_SRC_2, ATK_SRC_3};
	
	private int attackNum;
	
	public TowerGBBB(GameState gameState, Point2d loc, Node currNode, int spawnFrame) {
		super(ID, NAME, DESCRIPTION,
			  gameState, loc, currNode, PRIORITY, spawnFrame, TIER, MAX_HEALTH, HEALTH_REGEN, ATTACK_DAMAGE, ATTACK_DELAY,
			  ATTACK_RANGE, 0, false, false, PROJECTILE_SPEED, SHOT_ORIGIN_DISTANCE, false, false, generateShapes(loc));
		attackNum = 0;
		stickyTargeting = false;
	}
	
	private static PaintableShapes generateShapes(Point2d loc){
		PaintableShapes result = Tower.generateBaseShapes(loc);
		
		result.addFixedCircle(0, 0, 0.75, GameState.TOWER_GREEN);
		
		int nPoints1 = 3;
		double[] xPoints1 = {0.2, 0.82, 0.82};
		double[] yPoints1 = {0, 0.42, -0.42};
		result.addRotatablePolygon(nPoints1, xPoints1, yPoints1, GameState.TOWER_BLUE);
		result.rotate(Math.PI*2/3);
		
		int nPoints2 = 3;
		double[] xPoints2 = {0.2, 0.82, 0.82};
		double[] yPoints2 = {0, 0.42, -0.42};
		result.addRotatablePolygon(nPoints2, xPoints2, yPoints2, GameState.TOWER_BLUE);
		result.rotate(Math.PI*2/3);
		
		int nPoints3 = 3;
		double[] xPoints3 = {0.2, 0.82, 0.82};
		double[] yPoints3 = {0, 0.42, -0.42};
		result.addRotatablePolygon(nPoints3, xPoints3, yPoints3, GameState.TOWER_BLUE);
		
		return result;
	}
	
	@Override
	public void actionStep(){
		super.actionStep();
		
		//Health transfers. Transfers in blocks of HEALTH_TRANSFER_RATE or 1% of tower's max health, whichever is smaller.
		//Will never transfer FROM a tower with < 10% health.
		if(specialAction.canAct() && passiveAction.canAct()){
			Set<Tower> adjacentTowers = gameState.getTowersInEdgeRange(loc, HEALTH_TRANSFER_RADIUS);
			double myHealthPercent = currHealth / maxHealth.modifiedValue;
			double tHealthPercent;
			double tHealthTransferRate;
			double actualTransferAmount;
			for(Tower t : adjacentTowers){
				//health transfer
				tHealthPercent = t.currHealth / t.maxHealth.modifiedValue;
				tHealthTransferRate = Math.min(HEALTH_TRANSFER_RATE, t.maxHealth.modifiedValue * 0.01);
				if(tHealthPercent > myHealthPercent && tHealthPercent > HEALTH_TRANSFER_LOWER_CUTOFF &&
				   tHealthPercent - myHealthPercent >= HEALTH_TRANSFER_DIFFERENCE_CUTOFF){
					actualTransferAmount = t.harm(tHealthTransferRate, false, this);
					this.heal(actualTransferAmount, false, t);
				}
				else if(tHealthPercent < myHealthPercent && myHealthPercent > HEALTH_TRANSFER_LOWER_CUTOFF &&
						myHealthPercent - tHealthPercent >= HEALTH_TRANSFER_DIFFERENCE_CUTOFF){
					actualTransferAmount = this.harm(tHealthTransferRate, false, t);
					t.heal(actualTransferAmount, false, this);
				}
				
				//teleport speed-up (3x speed up)
				TeleportingDebuff teleportBuff = (TeleportingDebuff) t.getBuff(TELEPORT_DEBUFF_ID);
				if(teleportBuff != null)
					teleportBuff.timer-=2;
			}
		}
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
	
	@Override //allows targeting of projectiles (will only target enemy projectiles)
	protected Entity acquireTarget(){
		Set<Projectile> projectilesInRange = gameState.getProjectilesInRange(loc, attackRange.modifiedValue);
		for(Projectile proj : projectilesInRange){
			if(proj.source instanceof Enemy)
				return proj;
		}
		
		return super.acquireTarget();
	}
	
	protected void instantAttack(){
		target.harm(attackDamage.modifiedValue, true, this);
		Animation a = generateAttackAnimation(); //all so it doesn't set origin loc )=
		gameState.playAnimation(a);
	}
	
	protected Animation generateAttackAnimation(){
		attackNum++;
		return new SimpleRayAnimation(5, loc.afterTranslate(ATK_SRC_OFFSETS[attackNum%ATK_SRC_OFFSETS.length]),
				                      target.loc, 0.1, 0.8f, 0.5f, GameState.PROJECTILE_GREENBLUE);
	}
}
