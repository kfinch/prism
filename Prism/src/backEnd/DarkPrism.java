package backEnd;

import java.awt.Color;

import util.PaintableShapes;

public class DarkPrism extends Entity {

	protected static final double MAX_HEALTH = 800;
	protected static final double HEALTH_REGEN = 0;
	
	public DarkPrism(double xLoc, double yLoc) {
		super(xLoc, yLoc, MAX_HEALTH, HEALTH_REGEN, generatePaintableShapes(xLoc, yLoc));

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
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, Color.black);
		
		return result;
	}
	
	@Override
	public void postStep(GameState gameState){
		if(gameState.isLit(xLoc, yLoc)){
			harm(1);
		}
	}
}
