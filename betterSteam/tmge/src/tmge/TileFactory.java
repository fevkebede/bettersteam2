package tmge;

import java.util.Random;

public abstract class TileFactory {
	Random rand = new Random();
	
	public abstract Tile createTile(int row, int col);
	
    public int getRandomValue(int low, int high) {
        return rand.nextInt(high-low) + low;
    }
    
}
