package backEnd;

public abstract class TimedBuff extends Buff {
	
	public int initialDuration;
	public int timer;
	
	public TimedBuff(GameState gameState, String id, String name, String description,
			         boolean isBeneficial, boolean isDispellable, int timer){
		super(gameState, id, name, description, isBeneficial, isDispellable);
		this.initialDuration = timer;
		this.timer = timer;
	}
	
	@Override
	public void step(Entity e){
		timer--;
		if(timer <= 0)
			isActive = false;
	}

}
