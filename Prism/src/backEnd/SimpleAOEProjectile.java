package backEnd;

import java.util.Set;

import util.PaintableShapes;

public class SimpleAOEProjectile extends SimpleProjectile{

	public double aoe;
	
	public SimpleAOEProjectile(double xLoc, double yLoc, Entity target,
			double moveSpeed, double damage, double aoe, PaintableShapes shapes) {
		super(xLoc, yLoc, target, moveSpeed, damage, shapes);
		this.aoe = aoe;
	}
	
	@Override
	protected void payload(GameState gameState){
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, aoe);
		for(Enemy e : enemiesInBlast)
			e.hurt(damage);
	}

}
