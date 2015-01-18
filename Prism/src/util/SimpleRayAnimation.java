package util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class SimpleRayAnimation extends Animation {

	protected int duration;
	protected int frameCount;
	protected Vector2d rayVec;
	protected double width;
	protected float alpha;
	protected float alphaStep;
	protected Color color;
	
	public SimpleRayAnimation(int duration, Point2d p1, Point2d p2, double width,
			                  float startingAlpha, float endingAlpha, Color color){
		super();
		this.duration = duration;
		this.frameCount = 0;
		this.loc = p1;
		this.rayVec = new Vector2d(p2.x - p1.x, p2.y - p1.y);
		this.width = width;
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
		g2d.setColor(new Color(colorComps[0],colorComps[1], colorComps[2],alpha));
		
		g2d.setStroke(new BasicStroke((float) width*tileSize));
		g2d.drawLine(centerX, centerY, (int)(centerX + rayVec.x*tileSize), (int)(centerY + rayVec.y*tileSize));
		g2d.setStroke(new BasicStroke(1));
	}

}
