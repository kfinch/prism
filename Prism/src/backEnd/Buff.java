package backEnd;

public abstract class Buff {

	public String id;
	public String name;
	public String description;
	public boolean isActive;
	public boolean isBeneficial;
	public boolean isDispellable;
	
	public Buff(String id, String name, String description, boolean isBeneficial, boolean isDispellable){
		this.id = id;
		this.isBeneficial = isBeneficial;
		this.isActive = true;
	}
	
	public abstract void handleDuplicate(Buff b, GameState gameState);
	
	public abstract void apply(Entity e, GameState gameState);
	
	public abstract void step(Entity e, GameState gameState);
	
	public abstract void remove(Entity e, GameState gameState);
	
}
