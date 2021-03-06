package backEnd;

/**
 * Basic implementation of a Stat that can get only multiplicative bonuses, and for which a lower number is 'better'.
 * An example would be a tower or enemy's attack delay.
 * Buffs are applied in the following order, and all of them stack:
 * Multiplicative bonuses, then multiplicative penalties.
 * 
 * @author Kelton Finch
 *
 */
public class ReverseMultStat extends Stat{

	public ReverseMultStat(double baseValue) {
		super(baseValue);
	}

	@Override
	public void update() {
		modifiedValue = baseValue;
		if(bonusImmunityCount == 0){
			double totalMultBonus = 1;
			for(double d : multBonuses)
				totalMultBonus += d-1;
			modifiedValue /= totalMultBonus;
		}
		if(penaltyImmunityCount == 0){
			double totalMultPenalty = 1;
			for(double d : multPenalties)
				totalMultPenalty += d-1;
			modifiedValue *= totalMultPenalty;
		}
	}
}
