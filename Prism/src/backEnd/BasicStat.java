package backEnd;

/**
 * Basic implementation of Stat.
 * Buffs are applied in the following order, and all of them stack:
 * Additive Bonuses, then multiplicative bonuses, then additive penalties, then multiplicative penalties.
 * 
 * @author Kelton Finch
 *
 */
public class BasicStat extends Stat{

	public BasicStat(double baseValue) {
		super(baseValue);
	}

	@Override
	public void update() {
		modifiedValue = baseValue;
		if(bonusImmunityCount == 0){
			for(double d : addBonuses)
				modifiedValue += d;
			double totalMultBonus = 1;
			for(double d : multBonuses)
				totalMultBonus += d-1;
			modifiedValue *= totalMultBonus;
		}
		if(penaltyImmunityCount == 0){
			for(double d : addPenalties)
				modifiedValue -= d;
			double totalMultPenalty = 1;
			for(double d : multPenalties)
				totalMultPenalty += d-1;
			modifiedValue *= totalMultPenalty;
		}
	}

}
