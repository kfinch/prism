package backEnd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class EnemyWaveGenerator {

	public static final int DEFAULT_GENERATED_WAVE_BUFFER = 5;
	
	public static final int DEFAULT_WAVE_DURATION = 1200;
	public static final int DEFAULT_WAVE_DOWNTIME = 400;
	
	public static final double DEFAULT_HORDE_DURATION_EXTENSION = 1.5;
	
	public static final double DEFAULT_PACK_DURATION_REDUCTION = 0.75;
	
	public static final double DEFAULT_ELITE_TIER_BONUS = 6;
	public static final double DEFAULT_ELITE_NUMBER_REDUCTION = 0.5;
	public static final double DEFAULT_ELITE_VISUAL_SIZE_INCREASE = 1.25;
	
	public final int waveDuration;
	public final int waveDowntime;
	public final double hordeDurationExtension;
	public final double packDurationReduction;
	public final double eliteTierBonus;
	public final double eliteNumberReduction;
	
	private LinkedList<EnemyWave> incomingWaves;
	
	public int frameNumber;
	public double tier;
	
	private int currWaveStartFrame;
	private int nextWaveStartFrame;
	
	private int spawnPeriod;
	private int totalToSpawn;
	private int spawnedThisWave;
	
	public EnemyWaveGenerator(int waveDuration, int waveDowntime,
							  double hordeDurationExtension, double packDurationReduction,
							  double eliteTierBonus, double eliteNumberReduction){
		this.waveDuration = waveDuration;
		this.waveDowntime = waveDowntime;
		this.hordeDurationExtension = hordeDurationExtension;
		this.packDurationReduction = packDurationReduction;
		this.eliteTierBonus = eliteTierBonus;
		this.eliteNumberReduction = eliteNumberReduction;
		
		this.incomingWaves = new LinkedList<EnemyWave>();
		
		this.frameNumber = 0;
		
		currWaveStartFrame = -1;
		nextWaveStartFrame = 1;
	}
	
	public EnemyWaveGenerator(){
		this(DEFAULT_WAVE_DURATION, DEFAULT_WAVE_DOWNTIME, 
			 DEFAULT_HORDE_DURATION_EXTENSION, DEFAULT_PACK_DURATION_REDUCTION,
			 DEFAULT_ELITE_TIER_BONUS, DEFAULT_ELITE_NUMBER_REDUCTION);
	}
			
	public abstract EnemyWave generateNewWave();
	
	private void prepareCurrentWave(){
		EnemyWave currWave = incomingWaves.getFirst();
		currWaveStartFrame = nextWaveStartFrame;

		int waveLength = waveDuration;
		totalToSpawn = currWave.enemy.getBaseWaveSize();
		switch(currWave.modifier){
		case HORDE:
			waveLength *= hordeDurationExtension;
			totalToSpawn *= hordeDurationExtension;
			break;
		case PACK:
			waveLength *= packDurationReduction;
			break;
		case ELITE:
			totalToSpawn *= eliteNumberReduction;
			//TODO: allow for setting tier and shape size
			break;
		default:
			break;
		}
		nextWaveStartFrame += waveLength + waveDowntime;
		
		spawnedThisWave = 0;
		spawnPeriod = waveLength / totalToSpawn;
		
		//TODO: remove debugging prints
		System.out.println("Curr Wave: " + currWave.enemy + " (tier=" + currWave.enemy.tier + ") " + currWave.modifier);
		System.out.println("curr start frame = " + currWaveStartFrame + "  next start frame = " + nextWaveStartFrame);
		System.out.println("spawn period = " + spawnPeriod + "  total to spawn = " + totalToSpawn);
	}
	
	public List<Enemy> stepAndSpawn() {
		frameNumber++;
		
		while(incomingWaves.size() < DEFAULT_GENERATED_WAVE_BUFFER)
			incomingWaves.addLast(generateNewWave());
			
		if(frameNumber >= nextWaveStartFrame){
			incomingWaves.removeFirst();
			prepareCurrentWave();
		}
		
		// TODO: improve handling of elites (make their stats show up properly)
		List<Enemy> result = new ArrayList<Enemy>();
		if(spawnedThisWave < totalToSpawn && frameNumber % spawnPeriod == 0){
			spawnedThisWave++;
			Enemy enemyTemplate = incomingWaves.getFirst().enemy;
			double enemyTier = enemyTemplate.tier;
			if(incomingWaves.getFirst().modifier == WaveModifier.ELITE)
				enemyTier += eliteTierBonus;
			
			//TODO: remove more debugging
			System.out.println("spawning on frame #" + frameNumber + " @tier=" + enemyTier);
			Enemy enemyResult = enemyTemplate.generateCopy(enemyTier);
			if(incomingWaves.getFirst().modifier == WaveModifier.ELITE)
				enemyResult.shapes.rescale(DEFAULT_ELITE_VISUAL_SIZE_INCREASE);
			
			result.add(enemyResult);
		}
		return result;
	}

	public LinkedList<EnemyWave> getIncomingWaves() {
		return incomingWaves;
	}

}
