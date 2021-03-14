package tmge;

import javafx.scene.paint.Color;

public class BejeweledTileFactory extends TileFactory {
	private static BejeweledTileFactory bejeweledTileFactory = new BejeweledTileFactory();
	public static int BEJEWELED_TILE_SIZE = 50;
//	static Color[] colors = new Color[] {
//            Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.GREEN, Color.PURPLE, Color.SILVER, Color.CORAL 
//    };
	
	private BejeweledTileFactory() {}
	
	public static BejeweledTileFactory getInstance() {
		return bejeweledTileFactory;
	}
	

	@Override
	public Tile createTile(int row, int col) {
		int colorId = getRandomColorId();
		return new BejeweledTile(row, col, colors[colorId], colorId, BEJEWELED_TILE_SIZE);
	}
	
//	public int getRandomColorId() {
//    	return rand.nextInt(colors.length);
//    }
}