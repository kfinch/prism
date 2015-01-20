package util;

import java.awt.Graphics2D;

public abstract class Animation {

	public Point2d loc;
	
	public boolean isActive;
	
	public Animation(){
		this.loc = new Point2d(0,0);
		isActive = true;
	}
	
	public abstract void step();
	
	public void paintAnimation(Graphics2D g2d, int cornerX, int cornerY, int tileSize){
		paintAnimationFromCenter(g2d, (int)(cornerX + tileSize*loc.x), (int)(cornerY + tileSize*loc.y), tileSize);
	}
	
	public abstract void paintAnimationFromCenter(Graphics2D g2d, int centerX, int centerY, int tileSize);
}
