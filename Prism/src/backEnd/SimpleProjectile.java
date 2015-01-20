package backEnd;

import java.util.Set;

import util.Animation;
import util.PaintableShapes;
import util.Point2d;

public class SimpleProjectile extends Projectile {

	public static final double MAX_HEALTH = 1;
	public static final double HEALTH_REGEN = 0;
	
	public Entity source;
	public double damage;
	public double aoe;
	public boolean isAOE;
	public Buff appliedDebuff;
	public Animation playedAnimation;
	
	public SimpleProjectile(GameState gameState, Point2d loc, Entity source, Entity target, double moveSpeed,
			                double damage, double aoe, boolean isAOE, Buff appliedDebuff, PaintableShapes shapes) {
		super(gameState, loc, MAX_HEALTH, HEALTH_REGEN, target, moveSpeed, shapes);
		this.source = source;
		this.damage = damage;
		this.aoe = aoe;
		this.isAOE = isAOE;
		this.appliedDebuff = appliedDebuff;
	}
	
	@Override
	protected void payload(){
		boolean debuffs = appliedDebuff != null;
		if(isAOE){
			Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(target.loc, aoe);
			//TODO: remove debugging code
			//System.out.println(enemiesInBlast.size() + " enemies caught in " + aoe + " radius payload" +
			//		           " with target @" + target.xLoc + "," + target.yLoc + " on frame " + gameState.frameNumber);
			//if(!target.isActive)
			//	System.out.println("Target was dead!");
			if(debuffs){
				for(Enemy e : enemiesInBlast){
					e.harm(damage, true, source);
					e.addBuff(appliedDebuff);
				}
			}
			else{
				for(Enemy e : enemiesInBlast)
					e.harm(damage, true, source);
			}
		}
		else{
			target.harm(damage, true, source);
			if(debuffs)
				target.addBuff(appliedDebuff);
		}
		if(playedAnimation != null){
			playedAnimation.loc = target.loc;
			gameState.playAnimation(playedAnimation);
		}
	}

}
