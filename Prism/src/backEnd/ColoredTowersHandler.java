package backEnd;

/*
 * Handler for the various upgrade paths of the main group of towers.
 */
public class ColoredTowersHandler {
	
	/*
	public static Tower getTowerType(int red, int green, int blue, int spawnFrame){
		int tier = red + green + blue;
		
		switch(tier){
		case 1 :
			if(red == 1)
				return new TowerR(null, 0, 0, spawnFrame);
			else if(green == 1)
				return new TowerG(null, 0, 0, spawnFrame);
			else if(blue == 1)
				return new TowerB(null, 0, 0, spawnFrame);
			break;
		case 2 :
			if(red == 2)
				return new TowerRR(null, 0, 0, spawnFrame);
			else if(red == 1 && green == 1)
				return new TowerRG(null, 0, 0, spawnFrame);
			else if(green == 2)
				return new TowerGG(null, 0, 0, spawnFrame);
			else if(green == 1 && blue == 1)
				return new TowerGB(null, 0, 0, spawnFrame);
			else if(blue == 2)
				return new TowerBB(null, 0, 0, spawnFrame);
			else if(red == 1 && blue == 1)
				return new TowerRB(null, 0, 0, spawnFrame);
			break;
		case 3 :
			if(red == 3)
				return new TowerRRR(null, 0, 0, spawnFrame);
			else if(red == 2 && green == 1)
				return new TowerRRG(null, 0, 0, spawnFrame);
			else if(red == 1 && green == 2)
				return new TowerRGG(null, 0, 0, spawnFrame);
			else if(green == 3)
				return new TowerGGG(null, 0, 0, spawnFrame);
			else if(green == 2 && blue == 1)
				return new TowerGGB(null, 0, 0, spawnFrame);
			else if(green == 1 && blue == 2)
				return new TowerGBB(null, 0, 0, spawnFrame);
			else if(blue == 3)
				return new TowerBBB(null, 0, 0, spawnFrame);
			else if(red == 1 && blue == 2)
				return new TowerRBB(null, 0, 0, spawnFrame);
			else if(red == 2 && blue == 1)
				return new TowerRRB(null, 0, 0, spawnFrame);
			else if(red == 1 && green == 1 && blue == 1)
				return new TowerRGB(null, 0, 0, spawnFrame);
			break;
		}
		throw new IllegalArgumentException("No tower with RGB = " + red + " " + green + " " + blue);
	}
	*/
	
}
