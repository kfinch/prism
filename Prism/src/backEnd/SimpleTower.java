package backEnd;

import java.util.Iterator;
import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Vector2d;

/**
 * An abstract implementation of a 'simple tower', this gets most of the basic tower AI out of the way.
 * Most of the tower's actions are compartmentalized, so some but not all of the features could be used
 * by an implementing class.
 * 
 * @author Kelton Finch
 */
public abstract class SimpleTower extends Tower{

	protected Enemy target;
	
	protected double projectileSpeed;
	protected double shotOriginDistance;
	protected boolean usesProjectile;
	protected boolean tracksTarget;
	protected boolean appliesDebuff;
	
	protected double facing;
	
	public SimpleTower(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, int tier,
			 double maxHealth, double healthRegen, double attackDamage, double attackDelay, double attackRange,
			 double attackAOE, boolean canAOE, boolean appliesDebuff, double projectileSpeed, double shotOriginDistance,
			 boolean usesProjectile, boolean tracksTarget, PaintableShapes shapes){
		super(currNode, xLoc, yLoc, priority, spawnFrame, tier, maxHealth, healthRegen,
			  attackDamage, attackDelay, attackRange, attackAOE, canAOE, shapes);
		this.projectileSpeed = projectileSpeed;
		this.shotOriginDistance = shotOriginDistance;
		this.usesProjectile = usesProjectile;
		this.tracksTarget = tracksTarget;
		this.appliesDebuff = appliesDebuff;
		this.facing = 0;
	}
	
	@Override
	public void actionStep(GameState gameState){
		super.actionStep(gameState);
		if(attackAction.canAct()){
			//acquire a new target
			if(target == null || !target.isActive){
				//System.out.println("Turret spawned on frame " + spawnFrame + " attempting to acquire target..."); //TODO:
				target = acquireTarget(gameState);
			}
			//lose an acquired target
			if((target != null) &&
			   (GameState.dist(xLoc, yLoc, target.xLoc, target.yLoc) > attackRange.modifiedValue || !target.isActive)){
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
						projectileAttack(gameState);
					else
						instantAttack(gameState);
					attackTimer = 0;
				}
			}
		}
	}
	
	protected Enemy acquireTarget(GameState gameState){
		Set<Enemy> enemiesInRange = gameState.getEnemiesInRange(xLoc, yLoc, attackRange.modifiedValue);
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
		Vector2d vec = new Vector2d(target.xLoc - xLoc, target.yLoc - yLoc);
		double attackAngle = vec.angle();
		shapes.setAngle(attackAngle);
		facing = attackAngle;
	}
	
	protected void projectileAttack(GameState gameState){
		Vector2d offset = new Vector2d();
		offset.setAngleAndMagnitude(facing, shotOriginDistance);
		double shotOriginX = xLoc + offset.x;
		double shotOriginY = yLoc + offset.y;
		
		Projectile proj = generateProjectile(gameState, shotOriginX, shotOriginY);
		if(proj instanceof SimpleProjectile)
			((SimpleProjectile) proj).playedAnimation = generateAttackAnimation(gameState);
		gameState.projectiles.add(proj);
	}
	
	protected void instantAttack(GameState gameState){
		if(canAOE){
			Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(target.xLoc, target.yLoc, attackAOE.modifiedValue);
			if(appliesDebuff){
				for(Enemy e : enemiesInBlast){
					e.harm(attackDamage.modifiedValue);
					e.addBuff(generateAttackDebuff(), gameState);
				}
			}
			else{
				for(Enemy e : enemiesInBlast)
					e.harm(attackDamage.modifiedValue);
			}
		}
		else{
			target.harm(attackDamage.modifiedValue);
			if(appliesDebuff)
				target.addBuff(generateAttackDebuff(), gameState);
		}
		Animation a = generateAttackAnimation(gameState);
		if(a != null){
			a.setLocation(xLoc, yLoc);
			gameState.playAnimation(a);
		}
	}
	
	protected Buff generateAttackDebuff(){
		return null;
	}
	
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc){
		return null;
	}
	
	protected Animation generateAttackAnimation(GameState gameState){
		return null;
	}
	
	protected Projectile generateProjectile(GameState gameState, double xLoc, double yLoc){
		return new SimpleProjectile(xLoc, yLoc, target, projectileSpeed, attackDamage.modifiedValue,
				                    attackAOE.modifiedValue, canAOE, generateAttackDebuff(),
				                    generateProjectileShapes(xLoc, yLoc));
	}           
}

