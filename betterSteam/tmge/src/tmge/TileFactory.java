package tmge;

import java.util.Random;

public abstract class TileFactory {
	Random rand = new Random();
	
	protected abstract Tile createTile(int row, int col);
	
}
