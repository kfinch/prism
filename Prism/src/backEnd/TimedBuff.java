package backEnd;

public abstract class TimedBuff extends Buff {
	
	public int initialDuration;
	public int timer;
	
	public TimedBuff(String id, String name, String description, boolean isBeneficial, boolean isDispellable, int timer){
		super(id, name, description, isBeneficial, isDispellable);
		this.initialDuration = timer;
		this.timer = timer;
	}
	
	@Override
	public void step(Entity e, GameState gameState){
		timer--;
		if(timer <= 0)
			isActive = false;
	}

}
