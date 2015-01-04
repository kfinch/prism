package backEnd;

import util.PaintableShapes;
import util.Point2d;

public class Prism extends Entity implements LightSource {

	protected static final double MAX_HEALTH = 2000;
	protected static final double HEALTH_REGEN = 0.1;
	
	public double radiance;
	
	public Prism(double xLoc, double yLoc, double radiance) {
		super(xLoc, yLoc, MAX_HEALTH, HEALTH_REGEN, generatePaintableShapes(xLoc, yLoc));
		this.radiance = radiance;
	}

	private static PaintableShapes generatePaintableShapes(double xLoc, double yLoc){
		return new PaintableShapes(xLoc, yLoc);
	}

	@Override
	public void onSpawn(GameState gameState) {}

	@Override
	public void onDespawn(GameState gameState) {}

	@Override
	public double lightRadius() {
		return radiance;
	}

	@Override
	public Point2d getLocation() {
		return new Point2d(xLoc, yLoc);
	}
}
