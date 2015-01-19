package util;

import java.awt.Color;
import java.awt.Graphics2D;

public class SimpleShapeAnimation extends Animation {

	protected int duration;
	protected int frameCount;
	
	protected PaintableShapes shapes;
	
	protected float alpha;
	protected float alphaStep;
	
	protected Color color;
	
	public SimpleShapeAnimation(int duration, PaintableShapes shapes,
			                    float startingAlpha, float endingAlpha, Color color){
		super();
		this.duration = duration;
		this.frameCount = 0;
		this.shapes = shapes;
		this.alpha = startingAlpha;
		this.alphaStep = (endingAlpha-startingAlpha)/duration;
		this.color = color;
	}
	
	@Override
	public void step() {
		alpha += alphaStep;
		
		frameCount++;
		if(frameCount >= duration)
			isActive = false;
	}

	@Override
	public void paintAnimationFromCenter(Graphics2D g2d, int centerX,
			int centerY, int tileSize) {
		float[] colorComps = new float[3];
		colorComps = color.getColorComponents(colorComps);
		shapes.setColor(new Color(colorComps[0],colorComps[1], colorComps[2],alpha));
		shapes.paintShapes(g2d, centerX, centerY, tileSize);
	}

}
