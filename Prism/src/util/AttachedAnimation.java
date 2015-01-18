package util;

import backEnd.Entity;

public abstract class AttachedAnimation extends Animation {

	protected Entity anchor;
	protected boolean removeFromInactive;
	
	public AttachedAnimation(Entity anchor, boolean removeFromInactive){
		super();
		this.anchor = anchor;
		this.loc = anchor.loc;
		this.removeFromInactive = removeFromInactive;
	}
	
	@Override
	public void step(){
		if(anchor.isActive)
			setLocation(anchor.loc);
		else if(removeFromInactive)
			this.isActive = false;
	}
	
}
