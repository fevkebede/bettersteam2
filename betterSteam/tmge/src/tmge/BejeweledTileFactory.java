package tmge;

import javafx.scene.paint.Color;

public class BejeweledTileFactory extends TileFactory {
	private static int BEJEWELED_TILE_SIZE = 50;
	
	protected static Color[] colors = new Color[] {
			Color.RED, Color.ORANGE, Color.YELLOWGREEN, Color.BLUE, Color.PURPLE, Color.SILVER, Color.LIGHTSKYBLUE 
	};
	
	private static BejeweledTileFactory bejeweledTileFactory = new BejeweledTileFactory();
	
	
	public static BejeweledTileFactory getInstance() {
		return bejeweledTileFactory;
	}

	@Override
	public BejeweledTile createTile(int row, int col) {
		int colorId = getRandomColorId();
		return new BejeweledTile(row, col, colors[colorId], colorId, BEJEWELED_TILE_SIZE);
	}
	
	public int getRandomColorId() {
    	return rand.nextInt(colors.length);
    }
}
