package backEnd;

public class MoveSlowTowerDebuff extends TimedBuff {

	double strength;
	
	public MoveSlowTowerDebuff(int duration, double strength) {
		super("moveslowtower", "Slowed", "This enemy's movement is slowed.", false, true, duration);
		this.strength = strength;
	}

	/*
	 * (non-Javadoc)
	 * @see backEnd.Buff#handleDuplicate(backEnd.Buff)
	 * 
	 * Blindly overwrites weaker debuff, and fails to apply entirely if stronger already on target.
	 * Refreshes duration of equal strength duplicate debuffs.
	 */
	@Override
	public void handleDuplicate(Buff b) {
		MoveSlowTowerDebuff other = (MoveSlowTowerDebuff) b;
		if(other.strength > strength){
			strength = other.strength;
			timer = other.timer;
		}
		else if(other.strength == strength){
			timer = other.timer > timer ? other.timer : timer;
		}
	}

	@Override
	public void apply(Entity e) {
		Enemy en = (Enemy) e;
		en.moveSpeed.multPenalties.add(strength);
	}

	@Override
	public void remove(Entity e) {
		Enemy en = (Enemy) e;
		en.moveSpeed.multPenalties.remove(strength);
	}

}
