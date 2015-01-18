package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class CombatTextAnimation extends Animation {

	public String message;
	public double relativeFontSize;
	public Color color;
	public double riseStep;
	public float alphaStep;
	public float alpha;
	public int remainingDuration;
	
	public CombatTextAnimation(String message, double relativeFontSize, Color color, double rise, int duration){
		this.message = message;
		this.relativeFontSize = relativeFontSize;
		this.color = color;
		this.remainingDuration = duration;
		this.riseStep = rise/duration;
		this.alpha = 1.0f;
		this.alphaStep = 0.9f/duration;
	}
	
	@Override
	public void step() {
		setLocation(new Point2d(loc.x, loc.y - riseStep));
		alpha -= alphaStep;
		remainingDuration--;
		
		if(remainingDuration <= 0)
			isActive = false;
	}

	@Override
	public void paintAnimationFromCenter(Graphics2D g2d, int centerX,
			int centerY, int tileSize) {
		//System.out.println("painting combat text: " + message + " at " + xLoc + " " + yLoc);
		float[] colorComps = new float[3];
		colorComps = color.getColorComponents(colorComps);
		g2d.setColor(new Color(colorComps[0],colorComps[1], colorComps[2],alpha));
		
		g2d.setFont(new Font(Font.SANS_SERIF,Font.BOLD,(int)(relativeFontSize*tileSize)));
		
		Rectangle2D symbolDims = g2d.getFontMetrics().getStringBounds(message, g2d);
		g2d.drawString(message, centerX - (int)(symbolDims.getWidth()/2), centerY);
	}

}
