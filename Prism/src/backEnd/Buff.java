package backEnd;

public abstract class Buff {

	protected GameState gameState;
	
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
		this.isActive = true;
	}
	
	public abstract void handleDuplicate(Buff b);
	
	public abstract void apply(Entity e);
	
	public abstract void step(Entity e);
	
	public abstract void remove(Entity e);
	
}
