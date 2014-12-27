package backEnd;

public abstract class Buff {

	public String id;
	public String name;
	public String description;
	public boolean isActive;
	public boolean isBeneficial;
	
	public Buff(String id, String name, String description, boolean isBeneficial){
		this.id = id;
		this.isBeneficial = isBeneficial;
		this.isActive = true;
	}
	
	public abstract void apply(Entity e);
	
	public abstract void step(Entity e);
	
	public abstract void remove(Entity e);
	
}
