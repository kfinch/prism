package backEnd;

import util.PaintableShapes;

public abstract class SimpleAOETower extends SimpleTower {

	Stat attackAOE;
	
	public SimpleAOETower(Node currNode, double xLoc, double yLoc,
			double priority, int spawnFrame, double maxHealth,
			double healthRegen, double attackDamage, double attackDelay,
			double attackRange, double projectileSpeed, double attackAOE,
			double shotOriginDistance, PaintableShapes shapes) {
		super(currNode, xLoc, yLoc, priority, spawnFrame, maxHealth, healthRegen,
				attackDamage, attackDelay, attackRange, projectileSpeed,
				shotOriginDistance, shapes);
		this.attackAOE = new BasicStat(attackAOE);
	}

	@Override
	protected Projectile generateProjectile(GameState gameState, double xLoc, double yLoc){
		return new SimpleAOEProjectile(xLoc, yLoc, target, projectileSpeed,
                attackDamage.modifiedValue, attackAOE.modifiedValue, generateProjectileShapes(xLoc, yLoc));
	}
}
