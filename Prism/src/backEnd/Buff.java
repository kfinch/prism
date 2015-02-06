package backEnd;

public abstract class Buff {

	protected GameState gameState;
	protected Entity buffed;
	
	public String id;
	public String name;
	public String description;
	
	public boolean isActive;
	public boolean isBeneficial;
	public boolean isDispellable;
	
	public Buff(GameState gameState, String id, String name, String description,
			    boolean isBeneficial, boolean isDispellable){
		this.gameState = gameState;
		this.id = id;
		this.isBeneficial = isBeneficial;
		this.isDispellable = isDispellable;
		this.isActive = true;
	}
	
	public abstract void handleDuplicate(Buff b);
	
	public void apply(Entity buffed){
		this.buffed = buffed;
	}
	
	public abstract void step();
	
	public abstract void remove();
	
}
