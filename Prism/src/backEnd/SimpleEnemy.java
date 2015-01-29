package backEnd;

import java.util.Iterator;
import java.util.Set;

import util.Animation;
import util.GeometryUtils;
import util.PaintableShapes;
import util.Point2d;
import util.Vector2d;

/**
 * A simple enemy implementation. Allows user to specify a wide variety of behavioral stats on top of the basic stats.
 * 
 * @author Kelton Finch
 */
public abstract class SimpleEnemy extends Enemy {
	
	/*
	 * SimpleEnemy behavior:
	 * 
	 * Simple enemies always move in one of the three forward directions, or one of the two side to side directions.
	 * They marginally prefer moving forwards.
	 * 
	 * "nodeAttraction" tracks if this enemy is affected by attracting/repulsing effects.
	 * 
	 * "fireOnTheMove" is the modification to this enemy's speed while it is attacking.
	 * Its movement speed is multiplied by fireOnTheMove if it tries to move while attacking or reloading.
	 * It should be between 1 and 0 (though it could be higher than 1 if you want your enemy to be faster while attacking)
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
	
	protected int waveSize;
	protected double baseKillReward;
	
	protected boolean tracksTarget;
	protected boolean nodeAttraction;
	protected double fireOnTheMove;
	protected double[][] movePriorities;
	protected boolean usesProjectile;
	protected double projectileSpeed;
	protected double shotOriginDistance;
	protected boolean appliesDebuff;
	
	protected Entity target;
	protected double facing;
	
	public SimpleEnemy(String id, String name, String description,
			           GameState gameState, Point2d loc, double tier, int waveSize, double baseKillReward, Node currNode,
			           double priority, int spawnFrame, double maxHealth, double healthRegen,
			           double attackDamage, double attackDelay, double attackRange, double moveSpeed, boolean tracksTarget,
			           boolean nodeAttraction, double fireOnTheMove, double[][] movePriorities,
			           boolean usesProjectile, double projectileSpeed,
			           double shotOriginDistance, boolean appliesDebuff, PaintableShapes shapes) {
		super(id, name, description, gameState, loc, tier, currNode, priority, spawnFrame, maxHealth, healthRegen,
				attackDamage, attackDelay, attackRange, moveSpeed, shapes);
		this.waveSize = waveSize;
		this.baseKillReward = baseKillReward;
		
		this.tracksTarget = tracksTarget;
		this.nodeAttraction = nodeAttraction;
		if(fireOnTheMove < 0)
			throw new IllegalArgumentException("Fire on the Move value cannot be less than 0");
		this.fireOnTheMove = fireOnTheMove;
		this.movePriorities = movePriorities;
		this.usesProjectile = usesProjectile;
		this.projectileSpeed = projectileSpeed;
		this.shotOriginDistance = shotOriginDistance;
		this.appliesDebuff = appliesDebuff;
		
		this.target = null;
		this.facing = 0;
	}
	
	@Override
	public int getBaseWaveSize(){
		return waveSize;
	}
	
	@Override
	public double getKillReward(){
		return baseKillReward * (1 + Enemy.TIER_STAT_MULTIPLIER*2*tier);
	}
	
	protected void swapToNextNode(){
		currNode.enemies.remove(this);
		nextNode.enemies.add(this);
		currNode = nextNode;
		nextNode = null;
	}
	
	//TODO: this is a dumb way of doing it )= Think of something better.
	protected void chooseNextNode(){
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
		
		//eliminate invalid move directions
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
		
		//eliminate priorities not within 1 of highest, normalize around 1 as highest priority
		//then weight based on attractiveness
		
		Vector2d attractionVector = gameState.getAttractionAtPoint(loc);
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(Double.isNaN(modPriorities[i][j]))
					continue;
				else if(modPriorities[i][j] - highestPriority + 1 <= 0)
					modPriorities[i][j] = Double.NaN;
				else{
					modPriorities[i][j] -= (highestPriority-1);
					Vector2d moveVec = new Vector2d(i-1,j-1);
					double dot = attractionVector.dot(moveVec);
					double multiplier = dot >= 0 ? 1 + dot : 1 / (1-dot);
					if(nodeAttraction)
						modPriorities[i][j] *= multiplier;
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
			
		double rand = Math.random() * totalPriority;
		
		//TODO: remove debugging code
		//System.out.println("Total priority = " + totalPriority + "   Rand = " + rand);
			
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(!Double.isNaN(modPriorities[i][j])){
					rand -= modPriorities[i][j];
					if(rand <= 0){
						nextNode = gameState.nodeAt((int)loc.x + i - 1, (int)loc.y + j - 1);
						return;
					}
				}
			}
		}
	}
	
	@Override
	public void preStep(){
		super.preStep();
		
		if(target == null || !canRetainTarget())
			target = acquireTarget();
	}
	
	@Override
	public void moveStep(){
		super.moveStep();
		if(moveAction.canAct()){
			if(nextNode == null){
				chooseNextNode(); //nextNode can stay null after this call, hence the following if statement
			}
			if(nextNode != null){
				move();
			}
		}
	}
	
	@Override
	public void actionStep(){
		super.actionStep();
		if(attackAction.canAct()){
			if(target != null && target.isActive){
				if(tracksTarget){
					//rotate to face target it's attacking
					Vector2d attackVec = new Vector2d(loc, target.loc);
					shapes.setAngle(attackVec.getAngle());
				}
				
				//if attack off cooldown, attack!
				if(attackTimer == -1){
					if(usesProjectile)
						projectileAttack(target);
					else
						instantAttack(target);
					attackTimer = 0;
				}
			}
		}
	}
	
	protected void move(){
		double modMoveSpeed = moveSpeed.modifiedValue;
		if(target != null && target.isActive)
			modMoveSpeed *= fireOnTheMove;
		if(GeometryUtils.dist(loc.x, loc.y, nextNode.xLoc, nextNode.yLoc) <= modMoveSpeed){
			loc = new Point2d(nextNode.xLoc, nextNode.yLoc);
			swapToNextNode();
		}
		else{
			Vector2d moveVec = new Vector2d(nextNode.xLoc - loc.x, nextNode.yLoc - loc.y);
			moveVec = Vector2d.vectorFromAngleAndMagnitude(moveVec.getAngle(), modMoveSpeed);
			moveBy(moveVec);
			shapes.setAngle(moveVec.getAngle());
		}
	}
	
	protected boolean canRetainTarget(){
		if(!target.isActive)
			return false;
		
		if(target instanceof Tower){
			if(((Tower)target).isGhost)
				return false;
			else if(GeometryUtils.distFromTowerEdge(loc, target.loc) > attackRange.modifiedValue)
				return false;
			else
				return true;
		}
		else{
			if(GeometryUtils.dist(loc, target.loc) > attackRange.modifiedValue)
				return false;
			else
				return true;
		}
	}
	
	protected Entity acquireTarget(){
		Entity target = null;
		Set<Entity> targetsInRange = gameState.getTowersAndPrismInRange(loc, attackRange.modifiedValue);
		if(targetsInRange.size() == 0)
			return null;
		
		int targetIndex = (int) (Math.random()*targetsInRange.size());
		Iterator<Entity> iter = targetsInRange.iterator();
		for(int i=-1; i<targetIndex; i++)
			target = iter.next();
		return target;
	}
	
	protected void projectileAttack(Entity target){
		Vector2d offset = Vector2d.vectorFromAngleAndMagnitude(facing, shotOriginDistance);
		Point2d shotOrigin = loc.afterTranslate(offset);
		gameState.projectiles.add(generateProjectile(shotOrigin));
	}
	
	protected void instantAttack(Entity target){
		target.harm(attackDamage.modifiedValue, true, this);
		if(appliesDebuff)
			target.addBuff(generateAttackDebuff());
		Animation a = generateInstantAttackAnimation(gameState);
		if(a != null){
			a.loc = loc;
			gameState.playAnimation(a);
		}
	}

	protected Buff generateAttackDebuff(){
		return null;
	}
	
	protected PaintableShapes generateProjectileShapes(Point2d projLoc){
		return null;
	}
	
	protected Animation generateInstantAttackAnimation(GameState gameState){
		return null;
	}
	
	protected Projectile generateProjectile(Point2d projLoc){
		return new SimpleProjectile(gameState, loc, this, target, projectileSpeed, attackDamage.modifiedValue,
				                    0, false, generateAttackDebuff(), generateProjectileShapes(projLoc));
	}
	
}
