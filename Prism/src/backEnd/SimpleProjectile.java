package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;

public class SimpleProjectile extends Projectile {

	public static final double MAX_HEALTH = 1;
	public static final double HEALTH_REGEN = 0;
	
	public double damage;
	public double aoe;
	public boolean isAOE;
	public Buff appliedDebuff;
	public Animation playedAnimation;
	
	public SimpleProjectile(double xLoc, double yLoc, Entity target, double moveSpeed, double damage, double aoe,
							boolean isAOE, Buff appliedDebuff, PaintableShapes shapes) {
		super(xLoc, yLoc, MAX_HEALTH, HEALTH_REGEN, target, moveSpeed, shapes);
		this.damage = damage;
		this.aoe = aoe;
		this.isAOE = isAOE;
		this.appliedDebuff = appliedDebuff;
	}
	
	@Override
	protected void payload(GameState gameState) {
		boolean debuffs = appliedDebuff != null;
		if(isAOE){
			Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(target.xLoc, target.yLoc, aoe);
			//TODO: remove debugging code
			//System.out.println(enemiesInBlast.size() + " enemies caught in " + aoe + " radius payload" +
			//		           " with target @" + target.xLoc + "," + target.yLoc + " on frame " + gameState.frameNumber);
			//if(!target.isActive)
			//	System.out.println("Target was dead!");
			if(debuffs){
				for(Enemy e : enemiesInBlast){
					e.harm(damage);
					e.addBuff(appliedDebuff, gameState);
				}
			}
			else{
				for(Enemy e : enemiesInBlast)
					e.harm(damage);
			}
		}
		else{
			target.harm(damage);
			if(debuffs)
				target.addBuff(appliedDebuff, gameState);
		}
		if(playedAnimation != null){
			playedAnimation.setLocation(target.xLoc, target.yLoc);
			gameState.playAnimation(playedAnimation);
		}
	}

}
