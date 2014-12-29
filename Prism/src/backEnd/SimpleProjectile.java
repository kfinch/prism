package backEnd;

import java.util.Set;

import util.PaintableShapes;

public class SimpleProjectile extends Projectile {

	public static final double MAX_HEALTH = 1;
	public static final double HEALTH_REGEN = 0;
	
	public double damage;
	public double aoe;
	public boolean isAOE;
	public Buff appliedDebuff;
	
	public SimpleProjectile(double xLoc, double yLoc, Entity target, double moveSpeed, double damage, double aoe,
							boolean isAOE, Buff appliedDebuff, PaintableShapes shapes) {
		super(xLoc, yLoc, MAX_HEALTH, HEALTH_REGEN, target, moveSpeed, shapes);
		this.damage = damage;
	}

	@Override
	protected void payload(GameState gameState) {
		boolean debuffs = appliedDebuff != null;
		if(isAOE){
			Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, aoe);
			if(debuffs){
				for(Enemy e : enemiesInBlast){
					e.harm(damage);
					e.addBuff(appliedDebuff);
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
				target.addBuff(appliedDebuff);
		}
	}

}
