package backEnd;

import util.PaintableShapes;

/**
 * A simple enemy implementation. Allows user to specify a wide variety of behavioral stats on top of the basic stats.
 * 
 * @author Kelton Finch
 */
public abstract class SimpleEnemy extends Enemy{
	
	/*
	 * SimpleEnemy behavior:
	 * 
	 * Simple enemies always move in one of the three forward directions, or one of the two side to side directions.
	 * They marginally prefer moving forwards.
	 * 
	 * "towerAffinity" measure their preference towards moving towards nearby towers.
	 * A value of 0 indicates no preference, a positive value indicates more likely to move towards a nearby tower,
	 * a negative value indicates *less* likely to move towards a nearby tower.
	 * The enemy doesn't "see" a tower until within 5 nodes, and it is more likely to move towards closer towers.
	 * A value of +100 or -100 makes this enemy roughly twice as likely to move towards or away from towers, respectively.
	 * 
	 * "bravery" measures this enemy's willingness to continue advancing once inside attacking range.
	 * A value of 0 indicates it will never advance (or move at all) while within attack range.
	 * A value of 1 indicates it will always advance / move.
	 * Values outside of 0 to 1 (inclusive) have undefined behavior.
	 * 
	 * "fireOnTheMove" tracks if this enemy will keep on attacking while moving.
	 */
	protected double towerAffinity; 
	protected double bravery;
	protected boolean fireOnTheMove;
	
	public SimpleEnemy(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame,
			           double maxHealth, double healthRegen, double attackDamage, double attackDelay,
			           double attackRange, double moveSpeed, double towerAffinity, double bravery,
			           boolean fireOnTheMove, PaintableShapes shapes) {
		super(currNode, xLoc, yLoc, priority, spawnFrame, maxHealth, healthRegen,
				attackDamage, attackDelay, attackRange, moveSpeed, shapes);
		this.towerAffinity = towerAffinity;
		this.bravery = bravery;
		this.fireOnTheMove = fireOnTheMove;
	}
	
	

}
