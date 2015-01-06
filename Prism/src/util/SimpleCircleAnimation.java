package util;

import java.awt.Color;
import java.awt.Graphics2D;

public class SimpleCircleAnimation extends Animation {

	protected int duration;
	protected int frameCount;
	protected double size;
	protected double sizeStep;
	protected float alpha;
	protected float alphaStep;
	protected Color color;
	
	public SimpleCircleAnimation(int duration, double startingSize, double endingSize,
			                     float startingAlpha, float endingAlpha, Color color){
		super();
		this.duration = duration;
		this.frameCount = 0;
		this.size = startingSize;
		this.sizeStep = (endingSize-startingSize)/duration;
		this.alpha = startingAlpha;
		this.alphaStep = (endingAlpha-startingAlpha)/duration;
		this.color = color;
	}
	
	@Override
	public void step() {
		size += sizeStep;
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
		
		g2d.fillOval((int)(centerX-(size/2)*tileSize), (int)(centerY-(size/2)*tileSize),
				     (int)(size*tileSize), (int)(size*tileSize));
		//TODO: remove debugging code
		//System.out.println("Playing animation @ pixels " + centerX + " " + centerY + "   @ game " + xLoc + " " + yLoc);
		//System.out.println("pixel size = " + size*tileSize + "   game size = " + size);
	}

}
