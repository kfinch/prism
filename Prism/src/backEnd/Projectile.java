package backEnd;

import util.PaintableShapes;
import util.Point2d;
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
	
	public Projectile(GameState gameState, Point2d loc, double maxHealth, double healthRegen,
			          Entity target, double moveSpeed, PaintableShapes shapes) {
		super(gameState, loc, maxHealth, healthRegen, shapes);
		this.target = target;
		this.triggerPayload = false;
		this.moveSpeed = new BasicStat(moveSpeed);
	}
	
	protected abstract void payload();
	
	protected Vector2d vectorToTarget(){
		return new Vector2d(loc, target.loc);
	}
	
	protected void move(Vector2d moveVec){
		moveVec = Vector2d.vectorFromAngleAndMagnitude(moveVec.getAngle(), moveSpeed.modifiedValue);
		super.moveBy(moveVec);
	}
	
	protected void rotate(Vector2d moveVec){
		shapes.setAngle(moveVec.getAngle());
	}
	
	@Override
	public void moveStep(){
		if(moveAction.canAct()){
			Vector2d moveVec = vectorToTarget();
			if(moveVec.getMagnitude() <= moveSpeed.modifiedValue){
				triggerPayload = true;
			}
			else{
				move(moveVec);
				rotate(moveVec);
			}
		}
	}
	
	@Override
	public void actionStep(){
		if(triggerPayload){
			//a projectile that can't attack when it hits its target still goes away, but doesn't trigger its payload.
			if(attackAction.canAct())
				payload();
			isActive = false;
		}
	}
}
