package tmge;

public class TFETileFactory extends TileFactory {
	private static int TFE_TILE_SIZE = 100; 
	private static TFETileFactory tfeTileFactory = new TFETileFactory();
	
	public static TFETileFactory getInstance() {
		return tfeTileFactory;
	}

	@Override
	public TFETile createTile(int row, int col) {
		return new TFETile(row, col, TFE_TILE_SIZE);
	}
	
	public int getRandomValue(int range) {
        return rand.nextInt(range);
    }
	
//	10% change of getting a 4
	public int getFillValue() {
		int probability = getRandomValue(100);
		
		if (probability < 10) {
			return 4;
		} else {
			return 2;
		}
	}

}
