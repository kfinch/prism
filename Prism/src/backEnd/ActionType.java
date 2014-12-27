package backEnd;

public class ActionType {

	private int disableCount;
	private int immuneCount;
	
	public ActionType(){
		disableCount = 0;
		immuneCount = 0;
	}
	
	public boolean canAct(){
		return (disableCount == 0) || (immuneCount != 0);
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
	
}
