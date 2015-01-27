package backEnd;

public class EnemyWaveGeneratorTest extends EnemyWaveGenerator {

	public static final double DEFAULT_STARTING_TIER = 1;
	public static final double DEFAULT_TIER_STEP = 1;
	
	private double tierStep;
	
	private double currentTier;
	
	public EnemyWaveGeneratorTest(double startingTier, double tierStep){
		this.currentTier = startingTier - tierStep;
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
