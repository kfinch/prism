package backEnd;

import java.util.ArrayList;
import java.util.List;

public class EnemyWaveGeneratorTest extends EnemyWaveGenerator {

	public static final double DEFAULT_BASE_TIER = 0;
	public static final double DEFAULT_TIER_STEP = 1;
	
	private double tierStep;
	private double baseTier;
	private int waveNumber;
	
	private List<Enemy> enemyTypes;
	private List<WaveModifier> modifierTypes;
	
	public EnemyWaveGeneratorTest(double baseTier, double tierStep){
		this.baseTier = baseTier;
		this.tierStep = tierStep;
		this.waveNumber = 0;
		
		enemyTypes = new ArrayList<Enemy>();
		enemyTypes.add(new EnemyNibbler(0));
		enemyTypes.add(new EnemySlinger(0));
		enemyTypes.add(new EnemyAstral(0));
		enemyTypes.add(new EnemyBomber(0));
		enemyTypes.add(new EnemyKamikaze(0));
		enemyTypes.add(new EnemyTurtle(0));
		enemyTypes.add(new EnemySieger(0));
		
		modifierTypes = new ArrayList<WaveModifier>();
		modifierTypes.add(WaveModifier.PACK);
		modifierTypes.add(WaveModifier.HORDE);
		modifierTypes.add(WaveModifier.ELITE);
	}
	
	public EnemyWaveGeneratorTest(){
		this(DEFAULT_BASE_TIER, DEFAULT_TIER_STEP);
	}
	
	@Override
	public EnemyWave generateNewWave() {
		int i = (int) (Math.random()*enemyTypes.size());
		Enemy enemyType = enemyTypes.get(i);
		
		i = (int) (Math.random()*modifierTypes.size());
		WaveModifier modifierType;
		if(waveNumber % 3 == 0)
			modifierType = modifierTypes.get(i);
		else
			modifierType = WaveModifier.NONE;
		
		EnemyWave result = new EnemyWave(waveNumber, enemyType.generateCopy(baseTier + tierStep*waveNumber), modifierType);
		waveNumber++;
		return result;
	}

}
