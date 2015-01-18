package backEnd;

import util.PaintableShapes;
import util.Point2d;

public abstract class EntityWithAttack extends Entity {

	public Stat attackDamage;
	public Stat attackDelay;
	public Stat attackRange;
	public int attackTimer;
	
	public EntityWithAttack(GameState gameState, Point2d loc, double maxHealth, double healthRegen,
			                double attackDamage, double attackDelay, double attackRange, PaintableShapes shapes) {
		super(gameState, loc, maxHealth, healthRegen, shapes);
		this.attackDamage = new BasicStat(attackDamage);
		this.attackDelay = new ReverseMultStat(attackDelay);
		this.attackRange = new BasicStat(attackRange);
		this.attackTimer = -1;
	}
	
	@Override
	public void preStep(){
		super.preStep();
		if(passiveAction.canAct()){
			if(attackTimer >= 0)
				attackTimer++;
			if(attackTimer >= attackDelay.modifiedValue)
				attackTimer = -1;
		}
	}

}
