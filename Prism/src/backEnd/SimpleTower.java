package backEnd;

import java.util.Iterator;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;
import util.Vector2d;

/**
 * An abstract implementation of a 'simple tower', this gets most of the basic tower AI out of the way.
 * Most of the tower's actions are compartmentalized, so some but not all of the features could be used
 * by an implementing class.
 * 
 * @author Kelton Finch
 */
public abstract class SimpleTower extends Tower{

	protected Entity target;
	
	protected double projectileSpeed;
	protected double shotOriginDistance;
	protected boolean usesProjectile;
	protected boolean tracksTarget;
	protected boolean appliesDebuff;
	
	protected double facing;
	
	public SimpleTower(String id, String name, String description,
			 GameState gameState, Point2d loc, Node currNode, double priority, int spawnFrame, int tier,
			 double maxHealth, double healthRegen, double attackDamage, double attackDelay, double attackRange,
			 double attackAOE, boolean canAOE, boolean appliesDebuff, double projectileSpeed, double shotOriginDistance,
			 boolean usesProjectile, boolean tracksTarget, PaintableShapes shapes){
		super(id, name, description, gameState, loc, currNode, priority, spawnFrame, tier, maxHealth, healthRegen,
			  attackDamage, attackDelay, attackRange, attackAOE, canAOE, shapes);
		this.projectileSpeed = projectileSpeed;
		this.shotOriginDistance = shotOriginDistance;
		this.usesProjectile = usesProjectile;
		this.tracksTarget = tracksTarget;
		this.appliesDebuff = appliesDebuff;
		this.facing = 0;
	}
	
	@Override
	public void actionStep(){
		super.actionStep();
		if(attackAction.canAct()){
			//acquire a new target
			if(target == null || !target.isActive){
				//System.out.println("Turret spawned on frame " + spawnFrame + " attempting to acquire target..."); //TODO:
				target = acquireTarget();
			}
			//lose an acquired target
			if((target != null) &&
			   (loc.distanceTo(target.loc) > attackRange.modifiedValue || !target.isActive)){
				//System.out.println("Turret spawned on frame " + spawnFrame + " lost acquired target..."); //TODO:
				target = null;
			}
			//track an acquired target
			if(target != null && target.isActive){
				//System.out.println("Turret spawned on frame " + spawnFrame + " tracking target..."); //TODO:
				if(tracksTarget)
					trackTarget();
				//attack an acquired target
				if(attackTimer == -1){
					//System.out.println("Turret spawned on frame " + spawnFrame + " attacking target..."); //TODO:
					if(usesProjectile)
						projectileAttack();
					else
						instantAttack();
					attackTimer = 0;
				}
			}
		}
	}
	
	protected Entity acquireTarget(){
		Set<Enemy> enemiesInRange = gameState.getEnemiesInRange(loc, attackRange.modifiedValue);
		//TODO: remove debugging code
		//System.out.println(enemiesInRange.size() + " enemies detected in firing range (" + attackRange.modifiedValue + ")");
		
		double highestPriority = Double.NEGATIVE_INFINITY;
		for(Enemy e : enemiesInRange)
			highestPriority = (e.priority > highestPriority) ? e.priority : highestPriority;
		
		Iterator<Enemy> eIter = enemiesInRange.iterator();
		Enemy e;
		while(eIter.hasNext()){
			e = eIter.next(); 
			if(e.priority < highestPriority)
				eIter.remove();
		}
		
		int numTargets = enemiesInRange.size();
		if(numTargets == 0)
			return null;
		
		int rand = (int) (Math.random()*numTargets);
		eIter = enemiesInRange.iterator();
		Enemy result = null;
		for(int i=-1; i<rand; i++)
			result = eIter.next();
		
		return result;
	}
	
	protected void trackTarget(){
		Vector2d vec = new Vector2d(loc, target.loc);
		double attackAngle = vec.getAngle();
		shapes.setAngle(attackAngle);
		facing = attackAngle;
	}
	
	protected void projectileAttack(){
		Vector2d offset = Vector2d.vectorFromAngleAndMagnitude(facing, shotOriginDistance);
		Point2d shotOrigin = loc.afterTranslate(offset);
		
		Projectile proj = generateProjectile(shotOrigin);
		if(proj instanceof SimpleProjectile)
			((SimpleProjectile) proj).playedAnimation = generateAttackAnimation();
		gameState.projectiles.add(proj);
	}
	
	protected void instantAttack(){
		if(canAOE){
			Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(target.loc, attackAOE.modifiedValue);
			if(appliesDebuff){
				for(Enemy e : enemiesInBlast){
					e.harm(attackDamage.modifiedValue, true, this);
					e.addBuff(generateAttackDebuff());
				}
			}
			else{
				for(Enemy e : enemiesInBlast)
					e.harm(attackDamage.modifiedValue, true, this);
			}
		}
		else{
			target.harm(attackDamage.modifiedValue, true, this);
			if(appliesDebuff)
				target.addBuff(generateAttackDebuff());
		}
		Animation a = generateAttackAnimation();
		if(a != null){
			a.loc = loc;
			gameState.playAnimation(a);
		}
	}
	
	protected Buff generateAttackDebuff(){
		return null;
	}
	
	protected PaintableShapes generateProjectileShapes(Point2d projLoc){
		return null;
	}
	
	protected Animation generateAttackAnimation(){
		return null;
	}
	
	protected Projectile generateProjectile(Point2d projLoc){
		return new SimpleProjectile(gameState, projLoc, this, target, projectileSpeed, attackDamage.modifiedValue,
				                    attackAOE.modifiedValue, canAOE, generateAttackDebuff(),
				                    generateProjectileShapes(projLoc));
	}           
}

