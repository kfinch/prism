package backEnd;

import java.util.Set;

import util.PaintableShapes;

public class SimplePBAOETower extends SimpleAOETower {

	public SimplePBAOETower(Node currNode, double xLoc, double yLoc,
			double priority, int spawnFrame, double maxHealth,
			double healthRegen, double attackDamage, double attackDelay,
			double attackRange, double projectileSpeed, double attackAOE,
			double shotOriginDistance, PaintableShapes shapes) {
		super(currNode, xLoc, yLoc, priority, spawnFrame, maxHealth, healthRegen,
				attackDamage, attackDelay, attackRange, projectileSpeed, attackAOE,
				shotOriginDistance, shapes);
		attackRange = attackAOE; //the attack's range IS it's AoE
	}

	@Override
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc) {
		return null; //doesn't have a projectile!
	}
	
	@Override
	protected void attack(GameState gameState){
		Set<Enemy> enemiesInBlast = gameState.getEnemiesInRange(xLoc, yLoc, attackAOE.modifiedValue);
		for(Enemy e : enemiesInBlast)
			e.harm(attackDamage.modifiedValue);
	}

}
