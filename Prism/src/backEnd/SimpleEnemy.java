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
	 * 
	 * "movePriorities" tracks the enemy's likelihood of choosing a given direction while moving.
	 * It must be a 3x3 array, with each cell representing a move in the corresponding direction
	 * (and the middle cell representing no-move). Example: movePriorities[0][2] is a -x, +y move priority.
	 * The algorithm works as follows:
	 * First, all impossible moves are ruled out.
	 * Next, all moves not within 1 of the highest priority remaining are ruled out.
	 * Next, the remaining directions are additionally weighted by towerAffinity and the target node's attractiveness.
	 * Finally, the target node is chosen based on the remaining weights.
	 * Note that if all moves are impossible, the algorithm will return a "no move" i.e. nextNode = currNode.
	 * Example: [2, 1, NaN]
	 *          [3, 0, NaN]
	 *          [2, 1, NaN]
	 *          
	 * This grid means the enemy will always travel west if possible, otherwise it will travel north-west or south-west
	 * with equal probability, if neither of those are possible it will travel north or south with equal probability,
	 * if neither of those are available it will not move.
	 */
	protected double towerAffinity; //TODO: NYI
	protected double bravery;
	protected boolean fireOnTheMove;
	protected double[][] movePriorities;
	
	//the (default) enemy's likelyhood of choosing a given direction when moving.
	//See chooseNextNode() below for details
	
	public SimpleEnemy(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame,
			           double maxHealth, double healthRegen, double attackDamage, double attackDelay,
			           double attackRange, double moveSpeed, double towerAffinity, double bravery,
			           boolean fireOnTheMove, double[][] movePriorities, PaintableShapes shapes) {
		super(currNode, xLoc, yLoc, priority, spawnFrame, maxHealth, healthRegen,
				attackDamage, attackDelay, attackRange, moveSpeed, shapes);
		this.towerAffinity = towerAffinity;
		this.bravery = bravery;
		this.fireOnTheMove = fireOnTheMove;
		this.movePriorities = movePriorities;
	}
	
	//TODO: this is a dumb way of doing it )= Think of something better.
	protected void chooseNextNode(GameState gameState){
		boolean[][] validMoveDirections = gameState.getValidMoveDirections(currNode.xLoc, currNode.yLoc);
		double[][] modPriorities = new double[3][3];
		double highestPriority = Double.NEGATIVE_INFINITY;
		double totalPriority = 0;
			
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(validMoveDirections[i][j]){
					modPriorities[i][j] = movePriorities[i][j];
					if(modPriorities[i][j] > highestPriority)
						highestPriority = modPriorities[i][j];
				}
				else
					modPriorities[i][j] = Double.NaN;
			}
		}
			
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(highestPriority - modPriorities[i][j] <= -1)
					modPriorities[i][j] = Double.NaN;
				else{
					modPriorities[i][j] -= (highestPriority-1);
					modPriorities[i][j] *= gameState.nodes[i][j].attractiveness.modifiedValue;
					totalPriority += modPriorities[i][j];		
				}
			}
		}
			
		double rand = Math.random() * totalPriority;
			
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(!Double.isNaN(modPriorities[i][j])){
					rand -= modPriorities[i][j];
					if(rand <= 0){
						nextNode = gameState.nodes[currNode.xLoc + i - 1][currNode.yLoc + j - 1];
						return;
					}
				}
			}
		}
	}

}
