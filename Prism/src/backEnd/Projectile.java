package backEnd;

import util.PaintableShapes;
import util.Vector2d;

/**
 * This class represents a basic projectile:
 * An Entity that zooms unerringly towards a targeted Entity until it consumes itself in hitting it.
 * 
 * Most projectiles will probably just damage their target, but they can be extended to do pretty much whatever.
 * 
 * @author Kelton Finch
 */
public abstract class Projectile extends Entity {

	protected Entity target; //the target this projectile is heading for
	private boolean triggerPayload; //flagged if projectile hits its target
	
	protected Stat moveSpeed;
	
	public Projectile(double xLoc, double yLoc, double maxHealth, double healthRegen, Entity target, double moveSpeed,
					  PaintableShapes shapes) {
		super(xLoc, yLoc, maxHealth, healthRegen, shapes);
		
		this.target = target;
		this.triggerPayload = false;
		this.moveSpeed = new BasicStat(moveSpeed);
	}
	
	protected abstract void payload(GameState gameState);
	
	protected void move(Vector2d moveVec){
		moveVec.setMagnitude(moveSpeed.modifiedValue);
		super.moveBy(moveVec.x, moveVec.y);
	}
	
	protected void rotate(Vector2d moveVec){
		shapes.setAngle(moveVec.angle());
	}
	
	@Override
	public void moveStep(GameState gameState){
		if(moveAction.canAct()){
			Vector2d moveVec = new Vector2d(target.xLoc - xLoc, target.yLoc - yLoc);
			if(moveVec.magnitude() <= moveSpeed.modifiedValue){
				triggerPayload = true;
			}
			else{
				move(moveVec);
				rotate(moveVec);
			}
		}
	}
	
	@Override
	public void actionStep(GameState gameState){
		if(triggerPayload){
			//a projectile that can't attack when it hits its target still goes away, but doesn't trigger its payload.
			if(attackAction.canAct())
				payload(gameState);
			isActive = false;
		}
	}
}
