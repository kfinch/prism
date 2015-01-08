package backEnd;

import java.util.Iterator;
import java.util.Set;

import util.Animation;
import util.GeometryUtils;
import util.PaintableShapes;
import util.Vector2d;

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
	protected boolean fireOnTheMove;
	protected double[][] movePriorities;
	protected boolean usesProjectile;
	protected double projectileSpeed;
	protected double shotOriginDistance;
	protected boolean appliesDebuff;
	
	protected Entity target;
	protected double facing;
	
	protected boolean shouldMove;
	
	public SimpleEnemy(Node currNode, double xLoc, double yLoc, double priority, int spawnFrame,
			           double maxHealth, double healthRegen, double attackDamage, double attackDelay,
			           double attackRange, double moveSpeed, double towerAffinity, boolean fireOnTheMove,
			           double[][] movePriorities, boolean usesProjectile, double projectileSpeed,
			           double shotOriginDistance, boolean appliesDebuff, PaintableShapes shapes) {
		super(currNode, xLoc, yLoc, priority, spawnFrame, maxHealth, healthRegen,
				attackDamage, attackDelay, attackRange, moveSpeed, shapes);
		this.towerAffinity = towerAffinity;
		this.fireOnTheMove = fireOnTheMove;
		this.movePriorities = movePriorities;
		this.usesProjectile = usesProjectile;
		this.projectileSpeed = projectileSpeed;
		this.shotOriginDistance = shotOriginDistance;
		this.appliesDebuff = appliesDebuff;
		
		this.target = null;
		this.facing = 0;
	}
	
	protected void swapToNextNode(){
		currNode.enemies.remove(this);
		nextNode.enemies.add(this);
		currNode = nextNode;
		nextNode = null;
	}
	
	//TODO: this is a dumb way of doing it )= Think of something better.
	protected void chooseNextNode(GameState gameState){
		//System.out.println("Choosing next node from " + xLoc + " " + yLoc);
		
		boolean[][] validMoveDirections = gameState.getValidMoveDirections(currNode.xLoc, currNode.yLoc);
		double[][] modPriorities = new double[3][3];
		double highestPriority = Double.NEGATIVE_INFINITY;
		double totalPriority = 0;
		
		//TODO: remove debugging code
		/*
		System.out.println("Move priorities: ");
		for(int j=0; j<3; j++){
			for(int i=0; i<3; i++){
				System.out.print(movePriorities[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		*/
		
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
		
		//TODO: remove debugging code
		/*
		System.out.println("Mod priorities before rebalance: ");
		for(int j=0; j<3; j++){
			for(int i=0; i<3; i++){
				System.out.print(modPriorities[i][j] + " ");
			}
			System.out.println();
			
		}
		System.out.println();
		*/
			
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(Double.isNaN(modPriorities[i][j]))
					continue;
				else if(modPriorities[i][j] - highestPriority + 1 <= 0)
					modPriorities[i][j] = Double.NaN;
				else{
					modPriorities[i][j] -= (highestPriority-1);
					modPriorities[i][j] *= gameState.nodes[i][j].attractiveness.modifiedValue;
					totalPriority += modPriorities[i][j];		
				}
			}
		}
		
		//TODO: remove debugging code
		/*
		System.out.println("Mod priorities after rebalance: ");
		for(int j=0; j<3; j++){
			for(int i=0; i<3; i++){
				System.out.print(modPriorities[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		*/
		
		if(towerAffinity != 0){
			//TODO: do tower affinity stuff
		}
			
		double rand = Math.random() * totalPriority;
		
		//TODO: remove debugging code
		//System.out.println("Total priority = " + totalPriority + "   Rand = " + rand);
			
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(!Double.isNaN(modPriorities[i][j])){
					rand -= modPriorities[i][j];
					if(rand <= 0){
						nextNode = gameState.nodeAt((int)xLoc + i - 1, (int)yLoc + j - 1);
						return;
					}
				}
			}
		}
	}
	
	@Override
	public void preStep(GameState gameState){
		super.preStep(gameState);
		
		if(target == null || !canRetainTarget(gameState))
			target = acquireTarget(gameState);
		
		if(attackTimer != -1 && !fireOnTheMove)
			shouldMove = false;
		else
			shouldMove = true;
	}
	
	@Override
	public void moveStep(GameState gameState){
		super.moveStep(gameState);
		if(moveAction.canAct() && shouldMove){
			if(nextNode == null){
				chooseNextNode(gameState); //nextNode can stay null after this call, hence the following if statement
			}
			if(nextNode != null){
				move(gameState);
			}
		}
	}
	
	@Override
	public void actionStep(GameState gameState){
		super.actionStep(gameState);
		if(attackAction.canAct()){
			if(target != null && target.isActive){
				//rotate to face target it's attacking
				Vector2d attackVec = new Vector2d(target.xLoc - xLoc, target.yLoc - yLoc);
				shapes.setAngle(attackVec.angle());
				
				//if attack off cooldown, attack!
				if(attackTimer == -1){
					if(usesProjectile)
						projectileAttack(gameState, target);
					else
						instantAttack(gameState, target);
					attackTimer = 0;
				}
			}
		}
	}
	
	protected void move(GameState gameState){
		if(GeometryUtils.dist(xLoc, yLoc, nextNode.xLoc, nextNode.yLoc) <= moveSpeed.modifiedValue){
			xLoc = nextNode.xLoc;
			yLoc = nextNode.yLoc;
			swapToNextNode();
		}
		else{
			Vector2d moveVec = new Vector2d(nextNode.xLoc - xLoc, nextNode.yLoc - yLoc);
			moveVec.setMagnitude(moveSpeed.modifiedValue);
			moveBy(moveVec.x, moveVec.y);
			shapes.setAngle(moveVec.angle());
		}
	}
	
	protected boolean canRetainTarget(GameState gameState){
		if(!target.isActive)
			return false;
		
		if(target instanceof Tower){
			if(((Tower)target).isGhost)
				return false;
			else if(GeometryUtils.distFromTowerEdge(xLoc, yLoc, target.xLoc, target.yLoc) > attackRange.modifiedValue)
				return false;
			else
				return true;
		}
		else{
			if(GeometryUtils.dist(xLoc, yLoc, target.xLoc, target.yLoc) > attackRange.modifiedValue)
				return false;
			else
				return true;
		}
	}
	
	protected Entity acquireTarget(GameState gameState){
		//TODO: let target prism
		Entity target = null;
		Set<Tower> towersInRange = gameState.getTowersInRange(xLoc, yLoc, attackRange.modifiedValue);
		if(towersInRange.size() == 0)
			return null;
		
		int targetIndex = (int) (Math.random()*towersInRange.size());
		Iterator<Tower> iter = towersInRange.iterator();
		for(int i=-1; i<targetIndex; i++)
			target = iter.next();
		return target;
	}
	
	protected void projectileAttack(GameState gameState, Entity target){
		Vector2d offset = new Vector2d();
		offset.setAngleAndMagnitude(facing, shotOriginDistance);
		double shotOriginX = xLoc + offset.x;
		double shotOriginY = yLoc + offset.y;
		gameState.projectiles.add(generateProjectile(gameState, shotOriginX, shotOriginY));
	}
	
	protected void instantAttack(GameState gameState, Entity target){
		target.harm(attackDamage.modifiedValue);
		if(appliesDebuff)
			target.addBuff(generateAttackDebuff(), gameState);
		Animation a = generateInstantAttackAnimation(gameState);
		if(a != null)
			gameState.playAnimation(a);
	}

	protected Buff generateAttackDebuff(){
		return null;
	}
	
	protected PaintableShapes generateProjectileShapes(double xLoc, double yLoc){
		return null;
	}
	
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return null;
	}
	
	protected Projectile generateProjectile(GameState gameState, double xLoc, double yLoc){
		return new SimpleProjectile(xLoc, yLoc, target, projectileSpeed, attackDamage.modifiedValue,
				                    0, false, generateAttackDebuff(), generateProjectileShapes(xLoc, yLoc));
	}
	
}
