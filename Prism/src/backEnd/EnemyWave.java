package backEnd;

public class EnemyWave {

	public int waveNumber;
	public Enemy enemy;
	public WaveModifier modifier;
	
	public EnemyWave(int waveNumber, Enemy enemy, WaveModifier modifer){
		this.waveNumber = waveNumber;
		this.enemy = enemy;
		this.modifier = modifer;
	}
}