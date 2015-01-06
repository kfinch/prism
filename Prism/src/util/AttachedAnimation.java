package util;

import backEnd.Entity;

public abstract class AttachedAnimation extends Animation {

	Entity anchor;
	boolean removeFromInactive;
	
	public AttachedAnimation(Entity anchor, boolean removeFromInactive){
		super();
		this.anchor = anchor;
		this.removeFromInactive = removeFromInactive;
	}
	
	@Override
	public void step(){
		if(anchor.isActive)
			setLocation(anchor.xLoc, anchor.yLoc);
		else
			this.isActive = false;
	}
	
}
