package backEnd;

import java.awt.Color;

import util.PaintableShapes;
import util.Point2d;

public class DarkPrism extends Entity {

	protected static final double MAX_HEALTH = 800;
	protected static final double HEALTH_REGEN = 0;
	
	public DarkPrism(GameState gameState, Point2d loc) {
		super(gameState, loc, MAX_HEALTH, HEALTH_REGEN, generatePaintableShapes(loc));

		this.showHealthBar = true;
		this.healthBarOffset = -2.8;
		this.healthBarWidth = 2.0;
		this.healthBarHeight = 0.8;
	}
	
	private static PaintableShapes generatePaintableShapes(Point2d loc){
		PaintableShapes result = new PaintableShapes(loc);
		
		int nPoints1 = 4;
		double[] xPoints1 = {0,2,0,-2};
		double[] yPoints1 = {-2,0,2,0};
		result.addFixedPolygon(nPoints1, xPoints1, yPoints1, Color.black);
		
		return result;
	}
	
	@Override
	public void postStep(){
		if(gameState.isLit(loc)){
			harm(1, false, null);
		}
	}
}
