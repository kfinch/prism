package backEnd;

public class ActionType {

	private int disableCount;
	private int immuneCount;
	private int suppressCount;
	
	public ActionType(){
		disableCount = 0;
		immuneCount = 0;
		suppressCount = 0;
	}
	
	public boolean canAct(){
		return (suppressCount == 0) && ((disableCount == 0) || (immuneCount != 0));
	}
	
	public boolean isSuppressed(){
		return !(suppressCount == 0);
	}
	
	public boolean isImmune(){
		return !(immuneCount == 0);
	}
	
	public boolean isDisabled(){
		return !(disableCount == 0);
	}
	
	public void startDisable(){
		disableCount++;
	}
	
	public void endDisable(){
		disableCount--;
	}
	
	public void startImmune(){
		immuneCount++;
	}
	
	public void endImmune(){
		immuneCount--;
	}
	
	public void startSuppress(){
		suppressCount++;
	}
	
	public void endSuppress(){
		suppressCount--;
	}
	
}
