package backEnd;

public class EnemyWaveGeneratorTest extends EnemyWaveGenerator {

	public static final double DEFAULT_STARTING_TIER = 1;
	public static final double DEFAULT_TIER_STEP = 1;
	
	public final double startingTier;
	public final double tierStep;
	
	private double currentTier;
	
	public EnemyWaveGeneratorTest(double startingTier, double tierStep){
		this.startingTier = startingTier;
		this.currentTier = startingTier;
		this.tierStep = tierStep;
	}
	
	public EnemyWaveGeneratorTest(){
		this(DEFAULT_STARTING_TIER, DEFAULT_TIER_STEP);
	}
	
	@Override
	public EnemyWave generateNewWave() {
		EnemyWave result = new EnemyWave(new EnemyNibbler(currentTier), WaveModifier.NONE);
		currentTier += tierStep;
		return result;
	}

}
