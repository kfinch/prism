package backEnd;

import java.awt.Color;
import java.awt.Graphics2D;

import util.PaintableShapes;
import util.Point2d;

public class Prism extends Entity implements LightSource {

	protected static final double MAX_HEALTH = 2000;
	protected static final double HEALTH_REGEN = 0.01;
	
	protected static final float COLOR_CYCLE_RATE = 0.002f;
	
	public double radiance;
	public double radianceSetBack;
	
	public float hue;
	
	public Prism(double xLoc, double yLoc, double radiance, double radianceSetBack) {
		super(xLoc, yLoc, MAX_HEALTH, HEALTH_REGEN, generatePaintableShapes(xLoc, yLoc));
		this.radiance = radiance;
		this.radianceSetBack = radianceSetBack;
		
		this.hue = 0.0f;
		
		this.showHealthBar = true;
		this.healthBarOffset = -2.8;
		this.healthBarWidth = 2.0;
		this.healthBarHeight = 0.8;
	}

	private static PaintableShapes generatePaintableShapes(double xLoc, double yLoc){
		PaintableShapes result = new PaintableShapes(xLoc, yLoc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0,2,0,-2};
		double[] yPoints1 = {-2,0,2,0};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, GameState.UI_GOLD);
		
		return result;
	}

	@Override
	public void onSpawn(GameState gameState) {
		gameState.lightSources.add(this);
	}

	@Override
	public void onDespawn(GameState gameState) {}

	@Override
	public void preStep(GameState gameState){
		shapes.setColor(Color.getHSBColor(hue, 1f, 1f));
		hue += COLOR_CYCLE_RATE;
	}
	
	@Override
	public double lightRadius() {
		return radiance;
	}

	@Override
	public Point2d getLocation() { //this is for light source. Gives location further back.
		return new Point2d(-radianceSetBack, yLoc);
	}
}
