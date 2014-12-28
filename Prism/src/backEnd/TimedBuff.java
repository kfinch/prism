package backEnd;

public abstract class TimedBuff extends Buff {
	
	public int timer;
	
	public TimedBuff(String id, String name, String description, boolean isBeneficial, boolean isDispellable, int timer){
		super(id, name, description, isBeneficial, isDispellable);
		this.timer = timer;
	}
	
	@Override
	public void step(Entity e){
		timer--;
		if(timer == 0)
			isActive = false;
	}

}
