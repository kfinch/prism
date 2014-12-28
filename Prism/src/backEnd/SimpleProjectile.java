package backEnd;

import util.PaintableShapes;

public class SimpleProjectile extends Projectile {

	public static final double MAX_HEALTH = 1;
	public static final double HEALTH_REGEN = 0;
	
	public double damage;
	
	public SimpleProjectile(double xLoc, double yLoc, Entity target, double moveSpeed, double damage,
			                PaintableShapes shapes) {
		super(xLoc, yLoc, MAX_HEALTH, HEALTH_REGEN, target, moveSpeed, shapes);
		this.damage = damage;
	}

	@Override
	protected void payload(GameState gameState) {
		target.harm(damage);
	}

}
