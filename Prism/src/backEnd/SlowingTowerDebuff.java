package backEnd;

import java.awt.Color;
import java.awt.Graphics2D;

import util.AttachedAnimation;
import util.PaintableShapes;

public class SlowingTowerDebuff extends TimedBuff {

	double strength;
	
	SlowingDebuffAnimation animation;
	
	public SlowingTowerDebuff(int duration, double strength) {
		super("moveslowtower", "Slowed", "This enemy's movement and attacks are slowed.", false, true, duration);
		this.strength = strength;
	}

	/*
	 * (non-Javadoc)
	 * @see backEnd.Buff#handleDuplicate(backEnd.Buff)
	 * 
	 * Blindly overwrites weaker debuff, and fails to apply entirely if stronger already on target.
	 * Refreshes duration of equal strength duplicate debuffs.
	 */
	@Override
	public void handleDuplicate(Buff b, GameState gameState) {
		SlowingTowerDebuff other = (SlowingTowerDebuff) b;
		if(other.strength < strength){
			other.strength = strength;
			other.timer = timer;
		}
		else if(other.strength == strength){
			other.timer = other.timer > timer ? other.timer : timer;
		}
	}

	@Override
	public void apply(Entity e, GameState gameState) {
		Enemy en = (Enemy) e;
		
		en.moveSpeed.multPenalties.add(strength);
		en.moveSpeed.update();
		en.attackDelay.multPenalties.add(strength);
		en.attackDelay.update();
		
		animation = new SlowingDebuffAnimation(en);
		gameState.playAnimation(animation);
	}

	@Override
	public void remove(Entity e, GameState gameState) {
		Enemy en = (Enemy) e;
		
		en.moveSpeed.multPenalties.remove(strength);
		en.moveSpeed.update();
		en.attackDelay.multPenalties.remove(strength);
		en.attackDelay.update();
		
		animation.isActive = false;
	}

}

class SlowingDebuffAnimation extends AttachedAnimation {

	protected static final double ROTATE_SPEED = 0.1;
	protected static final int ALPHA = 100;
	
	PaintableShapes shapes;
	
	public SlowingDebuffAnimation(Entity anchor) {
		super(anchor, true);
		
		shapes = new PaintableShapes(xLoc, yLoc);
		int nPoints = 8;
		double[] xPoints = {0, 0.15, 0.4, 0.15, 0, -0.15, -0.4, -0.15};
		double[] yPoints = {-0.4, -0.15, 0, 0.15, 0.4, 0.15, 0, -0.15};
		Color color = new Color(GameState.PROJECTILE_GREENBLUE.getRed(), GameState.PROJECTILE_GREENBLUE.getGreen(),
				                GameState.PROJECTILE_GREENBLUE.getBlue(), ALPHA);
		shapes.addRotatablePolygon(nPoints, xPoints, yPoints, color);
	}
	 
	@Override
	public void step(){
		super.step();
		shapes.xLoc = xLoc;
		shapes.yLoc = yLoc;
		shapes.rotate(ROTATE_SPEED);
	}
	
	@Override
	public void paintAnimationFromCenter(Graphics2D g2d, int centerX,
			int centerY, int tileSize) {
		shapes.paintShapes(g2d, centerX, centerY, tileSize);
	}
	
}
