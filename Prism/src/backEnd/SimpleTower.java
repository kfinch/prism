package backEnd;

import java.util.Set;

import util.PaintableShapes;
import util.Vector2d;

/**
 * An abstract implementation of a 'simple tower', this gets most of the basic tower AI out of the way.
 * 
 * @author Kelton Finch
 */
public abstract class SimpleTower extends Tower{

	Enemy target;
	
	double projectileSpeed;
	double shotOriginDistance;
	double facing;
	
	public SimpleTower(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame, double maxHealth,
		     double healthRegen, double attackDamage, double attackDelay, double attackRange,
		     double projectileSpeed, double shotOriginDistance, PaintableShapes shapes){
		super(currNode, xLoc, yLoc, priority, spawnFrame, maxHealth, healthRegen,
			  attackDamage, attackDelay, attackRange, shapes);
		this.projectileSpeed = projectileSpeed;
		this.shotOriginDistance = shotOriginDistance;
		this.facing = 0;
	}
	
	@Override
	public void actionStep(GameState gameState){
		super.actionStep(gameState);
		if(attackAction.canAct()){
			//acquire a new target
			if(target == null){
				Set<Enemy> enemiesInRange = gameState.getEnemiesInRange(xLoc, yLoc, attackRange.modifiedValue);
				Enemy bestTarget = null;
				for(Enemy e : enemiesInRange){
					if(bestTarget == null || e.compareTo(bestTarget) > 0)
						bestTarget = e;
				}
				target = bestTarget;
			}
			//lose an acquired target
			if(target != null && GameState.dist(xLoc, yLoc, target.xLoc, target.yLoc) > attackRange.modifiedValue){
				target = null;
			}
			//track an acquired target
			if(target != null){
				Vector2d vec = new Vector2d(target.xLoc - xLoc, target.yLoc - yLoc);
				double attackAngle = vec.angle();
				shapes.setAngle(attackAngle);
				facing = attackAngle;
				//attack an acquired target
				if(attackTimer == -1){
					attack(gameState);
					attackTimer = 0;
				}
			}
		}
	}
	
	protected abstract PaintableShapes generateProjectileShapes(double xLoc, double yLoc);
	
	protected void attack(GameState gameState){
		Vector2d offset = new Vector2d();
		offset.setAngleAndMagnitude(facing, shotOriginDistance);
		double shotOriginX = xLoc + offset.x;
		double shotOriginY = yLoc + offset.y;
		gameState.projectiles.add(generateProjectile(gameState, shotOriginX, shotOriginY));
	}
	
	protected Projectile generateProjectile(GameState gameState, double xLoc, double yLoc){
		return new SimpleProjectile(xLoc, yLoc, target, projectileSpeed,
                                    attackDamage.modifiedValue, generateProjectileShapes(xLoc, yLoc));
	}
			           
}

