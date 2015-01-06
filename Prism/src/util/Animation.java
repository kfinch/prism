package util;

import java.awt.Graphics2D;

public abstract class Animation {

	public double xLoc, yLoc;
	
	public boolean isActive;
	
	public Animation(){
		isActive = true;
	}
	
	public void setLocation(double xLoc, double yLoc){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
	
	public abstract void step();
	
	public void paintAnimation(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		paintAnimationFromCenter(g2d, (int)(cornerX + tileSize*xLoc), (int)(cornerY + tileSize*yLoc), tileSize);
	}
	
	public abstract void paintAnimationFromCenter(Graphics2D g2d, int centerX, int centerY, int tileSize);
}
