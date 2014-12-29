package backEnd;

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
	
	protected double facing;
	
	public SimpleTower(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, int tier,
			 double maxHealth, double healthRegen, double attackDamage, double attackDelay, double attackRange,
			 double attackAOE, boolean canAOE, double projectileSpeed, double shotOriginDistance,
			 boolean usesProjectile, boolean tracksTarget, PaintableShapes shapes){
		super(currNode, xLoc, yLoc, priority, spawnFrame, tier, maxHealth, healthRegen,
			  attackDamage, attackDelay, attackRange, attackAOE, canAOE, shapes);
		this.projectileSpeed = projectileSpeed;
		this.shotOriginDistance = shotOriginDistance;
		this.usesProjectile = usesProjectile;
		this.tracksTarget = tracksTarget;
		this.facing = 0;
	}
	
	@Override
	public void actionStep(GameState gameState){
		super.actionStep(gameState);
		if(attackAction.canAct()){
			//acquire a new target
			if(target == null){
				target = acquireTarget(gameState);
			}
			//lose an acquired target
			if(target != null && GameState.dist(xLoc, yLoc, target.xLoc, target.yLoc) > attackRange.modifiedValue){
				target = null;
			}
			//track an acquired target
			if(target != null){
				if(tracksTarget)
					trackTarget();
				//attack an acquired target
				if(attackTimer == -1){
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
		Enemy bestTarget = null;
		for(Enemy e : enemiesInRange){
			if(bestTarget == null || e.compareTo(bestTarget) > 0)
				bestTarget = e;
		}
		return bestTarget;
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
		if(canAOE)
			gameState.projectiles.add(generateAOEProjectile(gameState, shotOriginX, shotOriginY));
		else
			gameState.projectiles.add(generateProjectile(gameState, shotOriginX, shotOriginY));
	}
	
	protected void instantAttack(GameState gameState){
		if(canAOE){
			Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, attackAOE.modifiedValue);
			for(Enemy e : enemiesInBlast)
				e.harm(attackDamage.modifiedValue);
		}
		else{
			target.harm(attackDamage.modifiedValue);
		}
		gameState.playAnimation(generateInstantAttackAnimation(gameState));
	}
	
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc){
		return null;
	}
	
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return null;
	}
	
	protected Projectile generateProjectile(GameState gameState, double xLoc, double yLoc){
		return new SimpleProjectile(xLoc, yLoc, target, projectileSpeed,
                                    attackDamage.modifiedValue, generateProjectileShapes(xLoc, yLoc));
	}
	
	protected Projectile generateAOEProjectile(GameState gameState, double xLoc, double yLoc){
		return new SimpleAOEProjectile(xLoc, yLoc, target, projectileSpeed, attackDamage.modifiedValue,
				                       attackAOE.modifiedValue, generateProjectileShapes(xLoc, yLoc));
	}
			           
}

