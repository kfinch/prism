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
	
	public abstract void update();
}
