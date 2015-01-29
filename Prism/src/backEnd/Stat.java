package backEnd;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a game "Stat", like the tower's damage or range.
 * Handles additive / multiplicative bonuses / penalties.
 * @author Kelton Finch
 *
 */
public abstract class Stat {

	public double baseValue;
	public double modifiedValue;
	public List<Double> addBonuses;
	public List<Double> multBonuses;
	public List<Double> addPenalties;
	public List<Double> multPenalties;
	public int penaltyImmunityCount;
	public int bonusImmunityCount;
	
	public Stat(double baseValue){
		this.baseValue = baseValue;
		this.modifiedValue = baseValue;
		addBonuses = new ArrayList<Double>();
		multBonuses = new ArrayList<Double>();
		addPenalties = new ArrayList<Double>();
		multPenalties = new ArrayList<Double>();
		penaltyImmunityCount = 0;
		bonusImmunityCount = 0;
	}
	
	public static void copyStatMods(Stat src, Stat dst){
		for(Double d : src.addBonuses)
			dst.addBonuses.add(d);
		for(Double d : src.multBonuses)
			dst.multBonuses.add(d);
		for(Double d : src.addPenalties)
			dst.addPenalties.add(d);
		for(Double d : src.multPenalties)
			dst.multPenalties.add(d);
		dst.update();
	}
	
	public abstract void update();
	
	@Override
	public String toString(){
		String result = "base=" + baseValue + " add+=" + addBonuses.size() + " mult+=" + multBonuses.size() + 
				        " add-=" + addPenalties.size() + " mult-=" + multPenalties.size();
		return result;
	}
}
