package backEnd;

public class EnemyWave {

	public Enemy enemy;
	public WaveModifier modifier;
	
	public EnemyWave(Enemy enemy, WaveModifier modifer){
		this.enemy = enemy;
		this.modifier = modifer;
	}
}